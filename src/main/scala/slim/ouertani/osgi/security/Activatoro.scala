package com.osgi.osgi1

/**
 * Hello world!
 *
 */
import org.osgi.framework. _
import org.osgi.service.condpermadmin.ConditionalPermissionAdmin
import org.osgi.service.condpermadmin._
import org.osgi.service.permissionadmin._
import org.osgi.framework.ServicePermission
import java.util.Dictionary;
import scala.collection.JavaConversions._
class Activator extends   BundleActivator {

  
   val p0 = new PermissionInfo        (classOf[org.osgi.framework.PackagePermission].getName(),"org.osgi.framework.PackagePermission","*")
    val p1 = new PermissionInfo        (classOf[java.lang.RuntimePermission].getName(),"java.lang.RuntimePermission","exitVM")
   val p2 = new PermissionInfo         (classOf[java.lang.RuntimePermission].getName(),"java.lang.RuntimePermission","*")
  
    val cond =  new ConditionInfo(
      classOf[BundleLocationCondition].getName(),Array[String]{"*/*"})

  def activatecpa( context:BundleContext) {
    val  sr = context.getServiceReference(classOf[ConditionalPermissionAdmin].getName())
  
//
    val cpa = context.getService(sr).asInstanceOf[ConditionalPermissionAdmin]
//    cpa.addConditionalPermissionInfo(
//      Array[ConditionInfo]{cond},
//      Array [PermissionInfo](p0,p1,p2)
//    );

  val ncpu=  cpa.newConditionalPermissionUpdate()
  println (ncpu.getConditionalPermissionInfos() .size  +"-------"  )
  val i:java.util.List[ConditionalPermissionInfo] = ncpu.getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
  i.map(println)


//    val c = """ALLOW { [ org.osgi.service.condpermadmin.BundleLocationCondition "*" ]( java.lang.RuntimePermission "*" )}"""
    val c = """ALLOW { [ BundleNameCondition "*" ]( java.lang.RuntimePermission "*" )}"""
    println (c)
   val info = cpa.newConditionalPermissionInfo( c )
    i.add(info)
    println (ncpu.commit)
     println (cpa.newConditionalPermissionUpdate().getConditionalPermissionInfos() .size  +"-------"  )
  }


  @throws (classOf[ java.lang.Exception])
  def start( context:BundleContext){

    activatecpa(context)
      
    
  }
  @throws (classOf[ java.lang.Exception])
  def start2( context:BundleContext){
   
    System setSecurityManager new SecurityManager() {
      private def log (a:Any, s :String) {
        println("Call  method ------> " + s +" (" + a + ")!");

      }
      override def checkExit( status:Int) {
        log(status,"Exit")
        throw new SecurityException();
      }

      override def checkExec( cmd:String) {
        log(cmd, "Exec")
        throw new SecurityException();
      }

         

//      override def checkPackageAccess( pkg:String) {
//        log(pkg,"PackageAccess")
//      }
//
//      override def checkPackageDefinition( pkg:String) {
//        log(pkg,"PackageDefinition")
//      }
//     override def checkMemberAccess( clazz: Class[_],  which:Int) {
//             println ("clazz ____________>" + clazz)
//             println ("which -------------->" + which)
//     }
    }
  }
  @throws (classOf[ java.lang.Exception])
  def stop( context:BundleContext) {}
}



