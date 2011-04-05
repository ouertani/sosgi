import com.weiglewilczek.bnd4sbt._
import sbt._

object SosgiProject {

  trait UnpublishedProject extends BasicManagedProject {
     override def publishLocalAction = task { None }
     override def deliverLocalAction = task { None }
     override def publishAction = task { None }
     override def deliverAction = task { None }
     override def artifacts = Set.empty
  }
}

import SosgiProject._

class SosgiProject(info: ProjectInfo) extends DefaultProject(info) with BNDPlugin {



object Dependencies {

    // Versions
    val OsgiVersion = "4.2.0"
    val PaxExamVersion = "1.2.3"
    val Slf4jVersion = "1.6.1"
    val Slf4sVersion = "1.0.3"
    val (specsVersion, mockitoVersion) = buildScalaVersion match {
      case "2.8.0" => "1.6.5" -> "1.8.4"
      case "2.8.1" => "1.6.7" -> "1.8.5"
      case _ => error("No clue what versions for specs and mockito to use!")
    }

    // Compile
    val slf4s = "com.weiglewilczek.slf4s" %% "slf4s" % Slf4sVersion withSources

    // Provided
    val osgiCore = "org.osgi" % "org.osgi.core" % OsgiVersion % "provided" withSources
    val osgiCompendium = "org.osgi" % "org.osgi.compendium" % OsgiVersion % "provided" withSources

    // Test
    val specs = "org.scala-tools.testing" %% "specs" % specsVersion % "test" withSources
    val mockito = "org.mockito" % "mockito-all" % mockitoVersion % "test" withSources
    val junitIF = "com.novocode" % "junit-interface" % "0.5" % "test"
    val slf4jSimple = "org.slf4j" % "slf4j-simple" % Slf4jVersion % "test" intransitive

    // Test (Pax Exam)
    val paxExam = "org.ops4j.pax.exam" % "pax-exam" % PaxExamVersion % "test"
    val paxExamJUnit = "org.ops4j.pax.exam" % "pax-exam-junit" % PaxExamVersion % "test"
    val paxExamCD = "org.ops4j.pax.exam" % "pax-exam-container-default" % PaxExamVersion % "test"
  }

 import Dependencies._

  override def managedStyle = ManagedStyle.Maven
  override def deliverAction = super.deliverAction dependsOn(publishLocal) // Fix for issue 99!
  val scalaModulesVersion = "2.0.2" // Or later
  val scalaModulesCore = "com.weiglewilczek.scalamodules" %% "scalamodules-core" % "2.0.2"
  
   //Osgi
  val osgi = "org.osgi" % "org.osgi.core" % "4.2.0"
  val osgiCompendium ="org.osgi" % "org.osgi.compendium" % "4.2.0"
  val eclipseOsgi = "org.eclipse" % "osgi" % "3.5.0.v20090520"


   // Test
    val specs = "org.scala-tools.testing" %% "specs" % specsVersion % "test" withSources
    val mockito = "org.mockito" % "mockito-all" % mockitoVersion % "test" withSources
    val junitIF = "com.novocode" % "junit-interface" % "0.5" % "test"
    val slf4jSimple = "org.slf4j" % "slf4j-simple" % Slf4jVersion % "test" intransitive

   // ===================================================================================================================
  // OSGi stuff
  // ===================================================================================================================

  import ExecutionEnvironment._
  override def bndBundleVendor = Some("slim ouertani")
  override def bndBundleLicense =
    Some("Eclipse Public License v1.0 (http://www.eclipse.org/legal/epl-v10.html)")
  override def bndExecutionEnvironment = Set(Java5, Java6)
  override def bndPrivatePackage = 
    "slim.ouertani.osgi.security.impl;version=\"%s\"".format(projectVersion.value) :: Nil
  override def bndExportPackage =
    "slim.ouertani.osgi.security;version=\"%s\"".format(projectVersion.value) :: Nil
 // override def bndVersionPolicy = Some("[$(@),$(version;=+;$(@)))")
   override def bndBundleActivator=Some("slim.ouertani.osgi.security.impl.Activator")
  override def compileOptions = super.compileOptions ++ compileOptions("-Xelide-below", "-1")



}
