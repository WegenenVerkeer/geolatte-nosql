package nosql.mongodb

import play.api.mvc.BodyParser
import play.api.libs.json._
import play.api.libs.iteratee.Iteratee
import play.Logger
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.DefaultBSONHandlers._

import play.modules.reactivemongo._
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import config.ConfigurationValues
import scala.concurrent.ExecutionContext
import play.api.libs.json.JsSuccess
import play.api.data.validation.ValidationError
import scala.util.Try

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: 8/2/13
 */
object ReactiveGeoJson {

  /**
   * Result for the GeoJson parsing
   * @param msg the state message
   * @param warnings the list with Warning messages
   * @param dataRemaining the remaing data (unparseable final part of the previous chunk)
   */
  case class State(msg: String = "", warnings: List[String] =  List(), dataRemaining: String = "")

  /**
   * Converts a Json validation error sequence for a Feature into a single error message String.
   * @param errors JsonValidation errors
   * @return
   */
  def processErrors(errors: Seq[(JsPath, Seq[ValidationError])]): String = {
    errors map {
      case (jspath, valerrors) => jspath + " :" + valerrors.map(ve => ve.message).mkString("; ")
    } mkString "\n"
  }

  def parseAsString( json: JsValue, state: State, features: List[JsObject]) = json.validate[JsObject] match {
    case JsSuccess(f, _) => (f :: features, state)
    case JsError(seq) => (features, State("With Errors", processErrors(seq) :: state.warnings, ""))
  }

  def processChunk(writer: FeatureWriter, state: State, chunk: Array[Byte]) : State = {
    val chunkAsString = new String(chunk, "UTF8")
    val toProcess = state.dataRemaining + chunkAsString
    val jsonStrings = toProcess.split(ConfigurationValues.jsonSeparator)
    Logger.debug(s"split results in ${jsonStrings.size} elements" )
    val (fs, newState) = jsonStrings.foldLeft(
        (List[JsObject](), state.copy(dataRemaining=""))
    ) ( (res : (List[JsObject], State), fStr : String )  => {
      val (features, curState) = res
      if (!curState.dataRemaining.isEmpty) Logger.warn(s"Invalid JSON: could not parse ${curState.dataRemaining}")
      Try{
        val json = Json.parse(fStr)
        parseAsString(json, curState.copy(dataRemaining = ""), features)
      }.getOrElse( (features, curState.copy(dataRemaining = fStr)) )
    })

    writer.add(fs)
    newState

  }

  def mkStreamingIteratee(writer: FeatureWriter) =
    Iteratee.fold( State() ) { (state: State, chunk: Array[Byte]) => processChunk(writer, state, chunk) } mapDone( state => { writer.updateIndex(); Right(state) } )

  def bodyParser(writer: FeatureWriter)(implicit ec: ExecutionContext) = BodyParser("GeoJSON feature BodyParser") { request =>
    mkStreamingIteratee(writer)
  }

}