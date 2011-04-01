
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


  private [this] def echo {
    context findService withInterface[ConditionalPermissionAdmin]  andApply { printConds _
    }
  }
  private [this] def clear {

    logger debug "clear security"
    
    context findService withInterface[ConditionalPermissionAdmin]  andApply {
      (cpa, props) => {
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
    }
  }

  private [this] def init {
    println (context.getBundle.getLocation)

    val sec1= """ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition """" +context.getBundle.getLocation+""""  ]
(org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
( java.security.AllPermission "*" "*"  )
(org.osgi.framework.AdminPermission  "*" "*")
(org.osgi.framework.PackagePermission "*"  "*")
}""";

    def buildAllows(s:String) = """ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition """" +s+""""  ]
(org.osgi.framework.ServicePermission, "org.osgi.service.condpermadmin.ConditionalPermissionAdmin", "*")
(org.osgi.framework.ServicePermission, "org.osgi.service.permissionadmin.PermissionAdmin", "*")
(org.osgi.framework.ServicePermission, "org.osgi.service.framework.CompositeBundleFactory", "*")
(org.osgi.framework.ServicePermission, "org.osgi.framework.hooks.service.*", "*")
(org.osgi.framework.ServicePermission, "org.osgi.service.packageadmin.PackageAdmin", "*")
(org.osgi.framework.PackagePermission, "*", "import")
(org.osgi.framework.BundlePermission, "*", "host,provide,fragment")
(java.lang.RuntimePermission, "loadLibrary.*", "*")
(java.lang.RuntimePermission, "queuePrintJob", "*")
(java.net.SocketPermission, "*", "connect")
(java.util.PropertyPermission, "*", "read")
(org.osgi.framework.PackagePermission, "*", "exportonly,import")
(org.osgi.framework.ServicePermission, "*", "get,register")
}""";
    var s:String=""
    context.getBundles.filter(_.getBundleId != 0).map(x=> { println (x.getBundleId) ;s+=buildAllows(x.getLocation)})


    println (s)
     context findService withInterface[ConditionalPermissionAdmin]  andApply {
      (cpa, props) => {
        updateSecurity(cpa,s)
      }
   
     }
  }


  private [this] def init2 {
    //val init ="""  ALLOW { [  BundleLocationCondition " """ +context.getBundle.getLocation+  """ " ]( java.security.AllPermission "*" "*" )}"""
    context findService withInterface[ConditionalPermissionAdmin]  andApply {
      (cpa, props) => {
        //"""ALLOW { [ BundleLocationCondition "*" ]( java.lang.RuntimePermission "*" )}"""
        updateSecurity(cpa,"""ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition "*/scala_2.8.1/*" ]
(org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
( java.security.AllPermission "*" "*"  )
(org.osgi.framework.AdminPermission  "*" "*")
(org.osgi.framework.PackagePermission "*"  "*")
}""",
                       """ALLOW {
[ org.osgi.service.condpermadmin.BundleLocationCondition "*" ]
  (org.osgi.framework.PackagePermission "*" "import,exportonly")
}""")
      }
      println ("security has been add")
    }
  }


  private[this] def commit (ncpu :ConditionalPermissionUpdate)=  ncpu.commit match {
    case true =>  logger info "Security operation OK"
    case _ => logger error "Security operation KO"
  }
  

  private[this]  def updateSecurity(cpa :ConditionalPermissionAdmin, s :String *) {
    try {
      val ncpu=  cpa.newConditionalPermissionUpdate()
      s.foreach { ss =>
        val info = cpa.newConditionalPermissionInfo(ss)       
        val conds:java.util.List[ConditionalPermissionInfo] =ncpu.getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
        var copy:java.util.List[ConditionalPermissionInfo] = new java.util.ArrayList[ConditionalPermissionInfo]()
        copy.addAll(conds)      
        conds.clear
        conds.add(info)
        conds.addAll(copy)
      }
     
      commit( ncpu )
    }catch {
      case e => logger error e.getMessage
    }


  }

  private[this] def printConds(cpa :ConditionalPermissionAdmin){
    val conds:java.util.List[ConditionalPermissionInfo] =cpa.newConditionalPermissionUpdate().getConditionalPermissionInfos().asInstanceOf[java.util.List[ConditionalPermissionInfo]]
   
    var c =""
    conds.map( t => c+= t.toString)   
  
   
    println(SecurityTree read c)


   
   
  }
  private [this] def query(bundleId:String, order :String , permission : String )={
    val bundle =  context.getBundle(bundleId.toInt)
    //"""ALLOW { [ BundleLocationCondition "*" ]( java.lang.RuntimePermission "*" )}"""
    order + """{ [ org.osgi.service.condpermadmin.BundleLocationCondition """" +bundle.getLocation + """" ] """ + permission +"""}"""
  }

  private var _ci:CommandInterpreter=_
//  def println(a:Any) {
//    if(_ci != null)
//      _ci print a + "\n"
//    else logger.info(""+a)
//  }
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
      case "allow" =>   context findService withInterface[ConditionalPermissionAdmin]  andApply {  updateSecurity(_, query(bundleId,"ALLOW",permission))}
      case "deny" =>   context findService withInterface[ConditionalPermissionAdmin]  andApply   {  updateSecurity(_, query(bundleId,"DENY",permission))  }
      case "init" => init2
      case "clear" => clear
      case "echo" => echo
      case _ => println ("exception")
    
    }

  }
  override  def getHelp():String = {
    println ("---Security Command Interpreter---")
    val s= new StringBuilder();
    s.append("\tsosgi allow - Allow security\n ")    
    s.append("\tsosgi clear - Clear security FWK\n ")
    s.append("\tsosgi deny - Deny security\n ")
    s.append("\tsosgi echo - Echo security FWK\n ")
    s.append("\tsosgi init - Init security FWK\n ")
   
    return s.toString;

    
  }



}
