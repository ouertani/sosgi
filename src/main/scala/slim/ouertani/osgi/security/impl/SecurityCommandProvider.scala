
package slim.ouertani.osgi.security.impl

import org.eclipse.osgi.framework.console.CommandInterpreter
import org.eclipse.osgi.framework.console.CommandProvider
import org.osgi.framework.BundleContext
import org.osgi.framework.Constants._
import com.weiglewilczek.scalamodules._
import com.weiglewilczek.slf4s.Logging
import org.osgi.service.condpermadmin.ConditionalPermissionAdmin
import org.osgi.service.condpermadmin.ConditionalPermissionInfo
import org.osgi.service.condpermadmin.ConditionalPermissionUpdate
import scala.collection.JavaConversions._
import scala.util.parsing.combinator._
class SecurityCommandProvider(context: BundleContext) extends CommandProvider  with Logging {


  private [this] def play(f : ConditionalPermissionAdmin=>Unit){
    context findService withInterface[ConditionalPermissionAdmin]  andApply {
      (cpa, props) =>  f(cpa)
    }
  }

  private [this] def echo(cpa : ConditionalPermissionAdmin) {
    val conds =cpa.newConditionalPermissionUpdate().getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
    println(SecurityTree read  conds.mkString    )
  }


  private [this] def clear(cpa : ConditionalPermissionAdmin) {
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


  private [this] def init(cpa : ConditionalPermissionAdmin) {

        updateSecurity("""ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition "*/admin/*" ]
(org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
(java.security.AllPermission "*" "*"  )
(org.osgi.framework.AdminPermission  "*" "*")
(org.osgi.framework.PackagePermission "*"  "*")
}""") (cpa)
  }


  private[this] def commit (ncpu :ConditionalPermissionUpdate)=  ncpu.commit match {
    case true =>  logger info "Security operation OK"
    case _ => logger error "Security operation KO"
  }
  

  private[this]  def updateSecurity( s :String )(cpa :ConditionalPermissionAdmin) {
    try {
      val ncpu=  cpa.newConditionalPermissionUpdate()
      
        val info = cpa.newConditionalPermissionInfo(s)       
        val conds:java.util.List[ConditionalPermissionInfo] =ncpu.getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
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

  private [this] def query(bundleId:String, order :String , permission : String )={
    val l  =  context.getBundle(bundleId.toInt).getLocation
    order + """{ [ org.osgi.service.condpermadmin.BundleLocationCondition """" +l + """" ] """ + permission +"""}"""
  }

  private var _ci:CommandInterpreter=_
  def println(a:Any="\n") {
    if(_ci != null)
      _ci print a + "\n"
    else logger.info(""+a)
  }
  def  _sosgi( ci:CommandInterpreter)  {
    _ci =ci
    val cmd = ci.nextArgument()
    val bundleId = ci.nextArgument()
    var a = ci.nextArgument()
    var p: List[String]=List()
    while(a != null){    
      p =a  :: p
      a=ci.nextArgument()
    }
    val permission = p.reverse.mkString(" ")
    println (permission)
    cmd match {
      case "+" =>  play( updateSecurity (query(bundleId,"ALLOW",permission)) )
      case "-" =>  play (updateSecurity (query(bundleId,"DENY",permission)) )
      case "!" => play(init)
      case "!!" => play (clear)
      case "?" => play (echo)
      case _ => getHelp


    }

  }
  override  def getHelp():String = {
    println ("---Security Command Interpreter---")
    val s= new StringBuilder();
    s.append("\tsosgi + - Allow security\n ")
    s.append("\tsosgi !! - Clear security FWK\n ")
    s.append("\tsosgi - - Deny security\n ")
    s.append("\tsosgi ? - Echo security FWK\n ")
    s.append("\tsosgi ! - Init security FWK\n ")
   
    return s.toString;

    
  }



}
