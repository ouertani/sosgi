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
[condition1 "jar"]
 (java.lang.RuntimePermission "*") } "id1"
allow {
[condition2 "*"]
(org.osgi.framework.PackagePermission "*" "import,exportonly") } "id2"
"""
    """two condition imput shoud be converted" """ in {

      val src = read(input)



      val exp =RichConditionalPermissionInfos(
        List(
          RichConditionalPermissionInfo(DenyAccessDecision,
                                        List(RichConditionInfo("condition1", List("jar"))),
                                        List(RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                        "id1"
          ),
          RichConditionalPermissionInfo(AllowAccessDecision,
                                        List(RichConditionInfo("condition2", List("*"))),
                                        List(RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("import,exportonly"),None)))),
                                        "id2"
          )
        ))


      src mustEqual exp
    }
  }
  """"conditions three""" should {
    val input = """
    deny {
[org.osgi.service.condpermadmin.BundleLocationCondition "file:/osgi1e-1.0-SNAPSHOT.jar"]
 (java.lang.RuntimePermission "*") } "id1"
allow {
 [org.osgi.service.condpermadmin.BundleLocationCondition "*"]
 (org.osgi.framework.PackagePermission "*" "import,exportonly") } "id2"
allow {
[org.osgi.service.condpermadmin.BundleLocationCondition "*/scala_2.8.1/*"]
 (org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
 (java.security.AllPermission "*" "*")
 (org.osgi.framework.AdminPermission "*" "*")
(org.osgi.framework.PackagePermission "*" "*")
} "id3" """


    """ 4 permissions imput shoud be converted" """ in {

      val src = read(input)

      val exp =RichConditionalPermissionInfos(
        List(
          RichConditionalPermissionInfo(DenyAccessDecision,
                                        List(RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("file:/osgi1e-1.0-SNAPSHOT.jar"))),
                                        List(
              RichPermissionInfo("java.lang.RuntimePermission",Some(RichPermissionInfoDetails("*",None,None)))),
                                        "id1"
          ),
          RichConditionalPermissionInfo(AllowAccessDecision,
                                        List(RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("*"))),
                                        List(
              RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("import,exportonly"),None)))),
                                        "id2"
          )
          ,
          RichConditionalPermissionInfo(AllowAccessDecision,
                                        List(RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("*/scala_2.8.1/*"))),
                                        List(
              RichPermissionInfo("org.osgi.framework.ServicePermission",Some(RichPermissionInfoDetails("org.eclipse.osgi.framework.console.CommandProvider",Some("register"),None))),
              RichPermissionInfo("java.security.AllPermission",Some(RichPermissionInfoDetails("*",Some("*"),None))),
              RichPermissionInfo("org.osgi.framework.AdminPermission",Some(RichPermissionInfoDetails("*",Some("*"),None))),
              RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("*"),None)))
            ),
                                        "id3"
          )

        ))


      src mustEqual exp
    }
  }
    """"conditions full""" should {
     val input = """
  allow {
[org.osgi.service.condpermadmin.BundleLocationCondition "*"]
 (org.osgi.framework.PackagePermission "*" "import,exportonly") }
"generated_1301671939043"allow {
[org.osgi.service.condpermadmin.BundleLocationCondition "*/scala_2.8.1/*"]
[org.osgi.service.condpermadmin.BundleLocationCondition2 "*/admin/*"]
 (org.osgi.framework.ServicePermission "org.eclipse.osgi.framework.console.CommandProvider" "register")
 (java.security.AllPermission "*" "*")
 (org.osgi.framework.AdminPermission "*" "*")
 (org.osgi.framework.PackagePermission "*" "*")
 } "generated_1301671939044"
    """
    """multi condition mutli permissions imput shoud be converted" """ in {

      val src = read(input)


      val rc1=RichConditionalPermissionInfo(AllowAccessDecision,
                                        List(RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("*"))),
                                        List(
              RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("import,exportonly"),None)))),
                                        "generated_1301671939043"
          )

      val rc2= RichConditionalPermissionInfo(AllowAccessDecision,
                                        List(
              RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", List("*/scala_2.8.1/*")),
              RichConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition2", List("*/admin/*"))
            ),
                                        List(
              RichPermissionInfo("org.osgi.framework.ServicePermission",Some(RichPermissionInfoDetails("org.eclipse.osgi.framework.console.CommandProvider",Some("register"),None))),
              RichPermissionInfo("java.security.AllPermission",Some(RichPermissionInfoDetails("*",Some("*"),None))),
              RichPermissionInfo("org.osgi.framework.AdminPermission",Some(RichPermissionInfoDetails("*",Some("*"),None))),
              RichPermissionInfo("org.osgi.framework.PackagePermission",Some(RichPermissionInfoDetails("*",Some("*"),None)))
            ),
                                        "generated_1301671939044"
          )

      val exp =RichConditionalPermissionInfos( List(rc1   , rc2 ))
 
      src mustEqual exp
    }
  }


}
