credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "AWV Snapshots" at "https://collab.mow.vlaanderen.be/nexus/content/repositories/snapshots/",
  "AWV Releases" at "https://collab.mow.vlaanderen.be/nexus/content/repositories/releases/"
)

// Comment to get more information during initialization
logLevel := Level.Warn

// Use the Play sbt plugin for Play project
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.2")

