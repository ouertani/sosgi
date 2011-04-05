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
    


      val exp =RichConditionalPermissionInfos(
         List(
           RichConditionalPermissionInfo(DenyAccessDecision,
                                         List(RichConditionInfo("type", List("args"))),
                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                         "generated"
           )
         ))


   // DenyAccessDecision mustEqual src.richConditionalPermissionInfos(0).accessDecision
     RichConditionInfo("type", List("args")).tipe mustEqual src.richConditionalPermissionInfos(0).richConditionInfos(0).tipe
   // RichConditionInfo("type", List("args")) mustEqual src.richConditionalPermissionInfos(0).richConditionInfos(0)
    }
  }
}
