
package slim.ouertani.osgi.security.impl


import scala.util.parsing.combinator._


object SecurityTree extends SecurityParser {
  def read(s:String):RichConditionalPermissionInfos = parse (conditionalPermissionInfos, s) get
}

class SecurityParser extends JavaTokenParsers {

val qname : Parser[String]= ("""([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""").r

  def quotedString : Parser[String] = "\"" ~> qname <~"\""
  def quotedStringSep: Parser[List[String]] = rep(quotedString )

  def name: Parser[String]=quotedString

  def acces: Parser[RichAccessDecision] =  (
    "ALLOW"  ^^ (x => AllowAccessDecision)
    | "allow" ^^   (x => AllowAccessDecision)
    | "DENY" ^^   (x => AllowAccessDecision)
    | "deny" ^^  (x =>  AllowAccessDecision)
  )


  def condition: Parser[RichConditionInfo]= "[" ~> qname ~ quotedStringSep <~ "]" ^^  {
    case  qname ~ quotedStringSep  => RichConditionInfo(qname,quotedStringSep)
  }
  val permissionDetails: Parser[(RichPermissionInfoDetails)]  = quotedString ~ opt(quotedString) ~ opt(quotedString) ^^  {
    case  x ~ y ~ z => RichPermissionInfoDetails(x,y,z)
  }

  def permission: Parser[RichPermissionInfo]= "(" ~> qname  ~ opt(permissionDetails) <~ ")" ^^ {
    case x ~ y => RichPermissionInfo(x , y)
  }

  def conditions: Parser[List[RichConditionInfo]]= rep  (condition )
  def permissions: Parser[List[RichPermissionInfo]]= permission ~ rep (permission) ^^ {
    case  x ~ y => x :: y
  }

  def conditionalPermissionInfo : Parser[RichConditionalPermissionInfo]= acces ~ "{" ~ conditions ~ permissions ~ "}" ~ quotedString ^^ {
    case x ~ "{" ~ y ~ z ~ "}" ~ t => RichConditionalPermissionInfo(x,y,z,t)
  }
  def conditionalPermissionInfos : Parser[RichConditionalPermissionInfos]= rep (conditionalPermissionInfo) ^^  { case x => RichConditionalPermissionInfos( x ) }


}
