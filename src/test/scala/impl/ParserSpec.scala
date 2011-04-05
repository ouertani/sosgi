package slim.ouertani.osgi.security
package impl
import org.osgi.framework.BundleContext
import org.specs.Specification
import SecurityParser._

class ParserSpec extends Specification {
  
  """"one condition""" should {
     val simpleInput = """deny {
 [type "args"]
 (java.lang.RuntimePermission "*")
    } "generated"
"""
    """simple imput shoud be converted" """ in {

      val src = read(simpleInput)
      println (src)


      val exp =RichConditionalPermissionInfos(
         List(
           RichConditionalPermissionInfo(DenyAccessDecision,
                                         List(RichConditionInfo("type", List("args"))),
                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                         "generated"
           )
         ))


      println ("------------")

      println(exp)
     src mustEqual exp
    }
  }
}
