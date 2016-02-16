import com.typesafe.sbt.packager.archetypes.ServerLoader

serverLoading in Debian := ServerLoader.SystemV

javaOptions in Universal ++= Seq(
  "-J-Xmx768m",
  "-Dfile.encoding=UTF-8",
  "-Dhttp.port=8080",
  "-Dconfig.file=/usr/share/geolatte-nosqlfs/conf/nosql.conf"
)

//toevoegen van custom entries aan de Debian control file
//dit zorgt ervoor dat de reverse-proxy wordt aangepast
debianControlFile in Debian ~= { (controlFile: File) =>
  IO.append(controlFile,
    """XBS-Private-BaseUrl: http://{{ ip_address }}:8080/geolatte-nosqlfs
      |XBS-Private-HappyUrl: http://{{ ip_address }}:8080/geolatte-nosqlfs/loadbal.html
      |""".stripMargin)
  controlFile
}


linuxPackageMappings in Debian <+= (name in Universal, baseDirectory in Debian) map { (name, dir) =>
  (packageMapping(
    (dir / "debian/changelog") -> "/usr/share/doc/geolatte-nosqlfs/changelog.Debian.gz"
  ) withUser "root" withGroup "root" withPerms "0644" gzipped) asDocs()
}

linuxPackageMappings in Debian <+= (name in Universal, baseDirectory in Debian) map { (name, dir) =>
  packageMapping(
    (dir / "debian/geolatte-nosqlfs_beaver.conf") -> "/etc/beaver/conf.d/geolatte-nosqlfs_beaver.conf"
  ) withUser "root" withGroup "root" withPerms "0644"
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

maintainer in Debian := "AWV"