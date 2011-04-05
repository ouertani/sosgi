package slim.ouertani.osgi.security
package impl
import org.osgi.framework.BundleContext
import org.specs.Specification
import SecurityParser._

class ParserSpec extends Specification {
  
  """"conditions one""" should {
     val input = """deny { [type "args"] (java.lang.RuntimePermission "*")  } "generated" """
    """simple imput shoud be converted" """ in {

      val src = read(input)
    


      val exp =RichConditionalPermissionInfos(
         List(
           RichConditionalPermissionInfo(DenyAccessDecision,
                                         List(RichConditionInfo("type", List("args"))),
                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                         "generated"
           )
         ))


    src mustEqual exp
    }
  }


    """"conditions two""" should {
     val input = """
deny {
[condition "jar"]
 (java.lang.RuntimePermission "*") } "id1"
allow { [org.osgi.service.condpermadmin.BundleLocationCondition "*"]
(org.osgi.framework.PackagePermission "*" "import,exportonly") } "id2"
"""
    """two condition imput shoud be converted" """ in {

      val src = read(input)



      val exp =RichConditionalPermissionInfos(
         List(
           RichConditionalPermissionInfo(DenyAccessDecision,
                                         List(RichConditionInfo("condition", List("jar"))),
                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                         "id1"
           ),
           RichConditionalPermissionInfo(AllowAccessDecision,
                                         List(RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("jar"))),
                                         List(RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("import,exportonly"),None)))),
                                         "id2"
           )
         ))


    src mustEqual exp
    }
  }
//    """"conditions three""" should {
//     val input = """
//    deny { [org.osgi.service.condpermadmin.BundleLocationCondition "file:/home/slim/dev/test/osgi1/target/osgi1e-1.0-SNAPSHOT.jar"] (java.lang.RuntimePermission "*") } "generated_1301647829464"allow { [org.osgi.service.condpermadmin.BundleLocationCondition "*"] (org.osgi.framework.PackagePermission "*" "import,exportonly") } "generated_1301647829462"allow { [org.osgi.service.condpermadmin.BundleLocationCondition "*/scala_2.8.1/*"] (org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register") (java.security.AllPermission "*" "*") (org.osgi.framework.AdminPermission "*" "*") (org.osgi.framework.PackagePermission "*" "*") } "generated_1301647829463" """
//
//    """simple imput shoud be converted" """ in {
//
//      val src = read(input)
//
//
//
//      val exp =RichConditionalPermissionInfos(
//         List(
//           RichConditionalPermissionInfo(DenyAccessDecision,
//                                         List(RichConditionInfo("type", List("args"))),
//                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
//                                         "generated"
//           )
//         ))
//
//
//    src mustEqual exp
//    }
//  }
//    """"conditions full""" should {
//     val input = """
//  allow { [org.osgi.service.condpermadmin.BundleLocationCondition "*"] (org.osgi.framework.PackagePermission "*" "import,exportonly") } "generated_1301671939043"allow { [org.osgi.service.condpermadmin.BundleLocationCondition "*/scala_2.8.1/*"] (org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register") (java.security.AllPermission "*" "*") (org.osgi.framework.AdminPermission "*" "*") (org.osgi.framework.PackagePermission "*" "*") } "generated_1301671939044"
//    """
//    """simple imput shoud be converted" """ in {
//
//      val src = read(input)
//
//
//
//      val exp =RichConditionalPermissionInfos(
//         List(
//           RichConditionalPermissionInfo(DenyAccessDecision,
//                                         List(RichConditionInfo("type", List("args"))),
//                                         List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
//                                         "generated"
//           )
//         ))
//
//
//    src mustEqual exp
//    }
//  }


}
