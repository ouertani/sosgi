package slim.ouertani.osgi.security.impl



import com.weiglewilczek.slf4s.Logging
import com.weiglewilczek.scalamodules._
import org.osgi.framework.{ BundleActivator, BundleContext }


class Activator  extends   BundleActivator with Logging {

  @throws (classOf[ java.lang.Exception])
  def start( context:BundleContext){
    try {

      context createService new SecurityCommandProvider(context) 
    }catch {
      case e => logger error (e.getMessage)
    }
  }

  @throws (classOf[ java.lang.Exception])
  def stop( context:BundleContext) {}
}
