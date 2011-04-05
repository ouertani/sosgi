package slim.ouertani.osgi.security.impl

import org.eclipse.osgi.framework.console.CommandInterpreter
import org.eclipse.osgi.framework.console.CommandProvider
import org.osgi.framework.BundleContext
import org.osgi.framework.Constants._
import com.weiglewilczek.scalamodules._
import com.weiglewilczek.slf4s.Logging
import org.osgi.service.condpermadmin.ConditionalPermissionAdmin
import scala.collection.JavaConversions._
import scala.util.parsing.combinator._
import SecurityManager._
class SecurityCommandProvider(implicit context: BundleContext) extends CommandProvider  with Logging {

  private var _ci:CommandInterpreter=_
  private [this] def println(a:Any="\n") {
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
    
    cmd match {
      case "+" =>  play( updateSecurity (query(bundleId,"ALLOW",permission)) )
      case "-" =>  play (updateSecurity (query(bundleId,"DENY",permission)) )
      case "!" => play(init)
      case "!!" => play (clear)
      case "?" => play (echo)
      case _ => getHelp
    }

  }
  override  def getHelp()= {
    println ("---Security Command Interpreter---")
    val s= new StringBuilder();
    s.append("\tsosgi + - Allow security\n ")
    s.append("\tsosgi !! - Clear security FWK\n ")
    s.append("\tsosgi - - Deny security\n ")
    s.append("\tsosgi ? - Echo security FWK\n ")
    s.append("\tsosgi ! - Init security FWK\n ")
   
    s.toString;
  }



}
