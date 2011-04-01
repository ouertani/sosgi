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


  override def managedStyle = ManagedStyle.Maven
  override def deliverAction = super.deliverAction dependsOn(publishLocal) // Fix for issue 99!
  val scalaModulesVersion = "2.0.2" // Or later
  val scalaModulesCore = "com.weiglewilczek.scalamodules" %% "scalamodules-core" % "2.0.2"
  
  val osgi = "org.osgi" % "org.osgi.core" % "4.2.0"
  val osgiCompendium ="org.osgi" % "org.osgi.compendium" % "4.2.0"
  val eclipseOsgi = "org.eclipse" % "osgi" % "3.5.0.v20090520"


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
   override def bndBundleActivator=Some("slim.ouertani.osgi.security.Activator")
  override def compileOptions = super.compileOptions ++ compileOptions("-Xelide-below", "-1")



}
