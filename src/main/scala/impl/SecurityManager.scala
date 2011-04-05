
package slim.ouertani.osgi.security
package impl

import org.osgi.framework.BundleContext
import org.osgi.service.condpermadmin.ConditionalPermissionAdmin
import org.osgi.service.condpermadmin.ConditionalPermissionInfo
import org.osgi.service.condpermadmin.ConditionalPermissionUpdate
import com.weiglewilczek.slf4s.Logging
import scala.collection.JavaConversions._
import scala.util.parsing.combinator._
import com.weiglewilczek.scalamodules._

object SecurityManager extends  Logging  {


  def play(f : ConditionalPermissionAdmin=>Unit)(implicit context:BundleContext ){
    context findService withInterface[ConditionalPermissionAdmin]  andApply {
      (cpa, props) =>  f(cpa)
    }
  }

  def echo(cpa : ConditionalPermissionAdmin)(implicit context:BundleContext ) {
    val conds =cpa.newConditionalPermissionUpdate().getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
    println(SecurityParser read  conds.mkString    )
  }


 def clear(cpa : ConditionalPermissionAdmin)(implicit context:BundleContext ) {
    try {
      val ncpu=  cpa.newConditionalPermissionUpdate()
      ncpu.getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]].clear
      ncpu.commit match {
        case true =>   logger info "Security operation OK"
        case false => logger error "Security operation KO"
      }
    }catch {
      case e => logger error e.getMessage
    }
  }


  def init(cpa : ConditionalPermissionAdmin) (implicit context:BundleContext ){

    val d = context  getProperty("ADMIN_DIR") match {
      case s:String => s
      case _ =>  "*/admin/*"
    }

    val initQ="""ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition """"  +d +"""" ]
(org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
(java.security.AllPermission "*" "*"  )
(org.osgi.framework.AdminPermission  "*" "*")
(org.osgi.framework.PackagePermission "*"  "*")
}"""

    updateSecurity(initQ) (cpa)
  }


  def commit (ncpu :ConditionalPermissionUpdate)=  ncpu.commit match {
    case true =>  logger info "Security operation OK"
    case _ => logger error "Security operation KO"
  }


  def updateSecurity( s :String )(cpa :ConditionalPermissionAdmin) {
    try {
      val ncpu=  cpa.newConditionalPermissionUpdate()
      val info = cpa.newConditionalPermissionInfo(s)
      val conds =ncpu.getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
      var copy = new java.util.ArrayList[ConditionalPermissionInfo]()
      copy.addAll(conds)
      conds.clear
      conds.add(info)
      conds.addAll(copy)
      commit( ncpu )
    } catch {
      case e => logger error e.getMessage
    }


  }

  def query(bundleId:String, order :String , permission : String )(implicit context:BundleContext )={
    val l  =  context.getBundle(bundleId.toInt).getLocation
    order + """{ [ org.osgi.service.condpermadmin.BundleLocationCondition """" +l + """" ] """ + permission +"""}"""
  }
}
