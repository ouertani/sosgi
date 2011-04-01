

package slim.ouertani.osgi.security

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.Bundle._

import org.osgi.service.condpermadmin.ConditionalPermissionAdmin


import com.weiglewilczek.scalamodules._

import org.osgi.framework.{ BundleActivator, BundleContext }
import impl._
class Activator  extends   BundleActivator  {

  @throws (classOf[ java.lang.Exception])
  def start( context:BundleContext){
 
//    val bt = new BundleTracker(context, RESOLVED,new SecurityBundleTracker )
//    bt.open
 try {
    context.createService(new SecurityCommandProvider(context));
 }catch {
   case e => println (e.getMessage)
 }
   

  }

 
  
  @throws (classOf[ java.lang.Exception])
  def stop( context:BundleContext) {}
}
