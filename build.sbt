import play.Project._
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._

/**
 * Dit mapt de deploy directory in de zip file van het artifact dat via het 'dist' commando gebouwd wordt via de univeral packager. Deze
 * mapping MOET in het root build.sbt bestand staan! Deze mapping is statisch t.o.v. sbt, dus na het wijzigen van bestanden onder de
 * deploy folder moet je een reload uitvoeren op het sbt project voor je het bouwen van de dist start.
 *
 * sbt> reload
 * sbt> dist
 *
 */
mappings in Universal ++= mapDirectoryAndContents((baseDirectory.value / "deploy") -> "deploy")

packageArchetype.java_server

mappings in Universal ++= mapDirectoryAndContents((baseDirectory.value / "conf") -> "conf")

testOptions in Test += Tests.Argument("junitxml")