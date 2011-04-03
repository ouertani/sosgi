

package slim.ouertani.osgi.security.impl



case class RichConditionalPermissionInfo(accessDecision:RichAccessDecision,
                                         richConditionInfos: List[RichConditionInfo],
                                         richPermissionInfos: List[RichPermissionInfo] ,
                                         name:String )
{
  val ligne1="\n"
  val space="\t"
  val space2=space + space


  override def toString = "\n\nFOR \n" + accessDecision+ " [" +name + "] "+ligne1 +
                         "If " + ligne1 +
                         space+ richConditionInfos.mkString(" AND \n" + space) + ligne1 +
                         "Then  "  + ligne1 +
                         space2+  richPermissionInfos.mkString(" AND \n " + space2)   + "\nEND FI"
                         ligne1

}

case class RichConditionalPermissionInfos( richConditionalPermissionInfos: List[RichConditionalPermissionInfo]) {
  override def toString =  richConditionalPermissionInfos.mkString(" AND \n")
}