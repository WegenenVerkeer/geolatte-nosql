package models

import play.api.mvc.{Result, Action, Controller}
import repositories.{Metadata, MongoRepository}
import play.api.libs.json.{JsValue, Writes, Json}
import org.geolatte.nosql.mongodb.{EnvelopeSerializer, MetadataIdentifiers}
import controllers.routes


trait RenderableResource;


trait Jsonable extends RenderableResource {
  def toJson: JsValue
}

trait Csvable extends RenderableResource {
  def toCsv: String
}

case class DatabasesResource(dbNames: Traversable[String]) extends Jsonable {
  lazy val intermediate = dbNames map (name => Map("name" -> name, "url" -> routes.Databases.getDb(name).url))

  def toJson = Json.toJson(intermediate)
}

case class DatabaseResource(db: String, collections: Traversable[String]) extends Jsonable {
  lazy val intermediate = collections map (name => Map("name" -> name, "url" -> routes.Databases.getCollection(db, name).url))

  def toJson = Json.toJson(intermediate)
}

case class CollectionResource(md: Metadata) extends Jsonable {

  implicit object MetadataWrites extends Writes[Metadata] {

    import MetadataIdentifiers._

    def writes(md: Metadata): JsValue = Json.obj(

      CollectionField -> md.name,
      ExtentField -> EnvelopeSerializer(md.envelope),
      IndexLevelField -> md.level,
      IndexStatsField -> Json.toJson(md.stats)
    )
  }

  def toJson: JsValue = Json.toJson(md)
}