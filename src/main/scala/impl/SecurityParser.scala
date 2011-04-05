
package slim.ouertani.osgi.security.impl


import scala.util.parsing.combinator._



object SecurityParser extends JavaTokenParsers {

  def read(s:String):RichConditionalPermissionInfos = parse (conditionalPermissionInfos, s) get

  private [this]  val qname : Parser[String]= ("""([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""").r

  private [this]  def quotedString : Parser[String] = "\"" ~> qname <~"\""
  private [this]  def quotedStringSep: Parser[List[String]] = rep(quotedString )

  private [this]  def name: Parser[String]=quotedString

  private [this]  def acces: Parser[RichAccessDecision] =  (
    "ALLOW"  ^^ (x => AllowAccessDecision)
    | "allow" ^^   (x => AllowAccessDecision)
    | "DENY" ^^   (x => DenyAccessDecision)
    | "deny" ^^  (x =>  DenyAccessDecision)
  )


  private [this]  def condition: Parser[RichConditionInfo]= "[" ~> qname ~ quotedStringSep <~ "]" ^^  {
    case  x ~ y  => RichConditionInfo(x.trim,y)
  }
   private [this]  def permissionDetails: Parser[(RichPermissionInfoDetails)]  = quotedString ~ opt(quotedString) ~ opt(quotedString) ^^  {
    case  x ~ y ~ z => RichPermissionInfoDetails(x,y,z)
  }

   private [this]  def permission: Parser[RichPermissionInfo]= "(" ~> qname  ~ opt(permissionDetails) <~ ")" ^^ {
     case x ~ y => RichPermissionInfo(x.trim , y)
  }

   private [this]  def conditions: Parser[List[RichConditionInfo]]= rep  (condition )
   private [this]  def permissions: Parser[List[RichPermissionInfo]]= permission ~ rep (permission) ^^ {
    case  x ~ y => x :: y
  }

   private [this]  def  conditionalPermissionInfo : Parser[RichConditionalPermissionInfo]= acces ~ "{" ~ conditions ~ permissions ~ "}" ~ quotedString ^^ {
    case x ~ "{" ~ y ~ z ~ "}" ~ t => RichConditionalPermissionInfo(x,y,z,t)
  }
   private [this]  def  conditionalPermissionInfos : Parser[RichConditionalPermissionInfos]= rep (conditionalPermissionInfo) ^^  { case x => RichConditionalPermissionInfos( x ) }


}
