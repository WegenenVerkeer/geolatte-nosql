import com.typesafe.sbt.packager.archetypes.ServerLoader
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._

serverLoading in Debian := ServerLoader.SystemV

//toevoegen van custom entries aan de Debian control file
//dit zorgt ervoor dat de reverse-proxy wordt aangepast
debianControlFile in Debian ~= { (controlFile: File) =>
  IO.append(controlFile,
    """XBS-Private-BaseUrl: http://{{ ip_address }}:8080/nosql
      |XBS-Private-HappyUrl: http://{{ ip_address }}:8080/nosql/loadbal.html
      |XBS-Private-Extra-Mappings-Json: [ { "pad": "nosql", "url": "http://{{ ip_address }}:8080/nosql" } ]
      |""".stripMargin)
  controlFile
}


linuxPackageMappings in Debian <+= (name in Universal, baseDirectory in Debian) map { (name, dir) =>
  (packageMapping(
    (dir / "debian/changelog") -> "/usr/share/doc/nosql/changelog.Debian.gz"
  ) withUser "root" withGroup "root" withPerms "0644" gzipped) asDocs()
}


publishTo <<= version { (v: String) =>
  val nexus = "https://collab.mow.vlaanderen.be/nexus/content/repositories/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("collab snapshots" at nexus + "snapshots")
  else
    Some("collab releases"  at nexus + "releases")
}

publishMavenStyle in Debian := false
publishMavenStyle := true

packageDescription in Debian := "Geolatte NOSQLFS"

maintainer in Debian := "AWV"