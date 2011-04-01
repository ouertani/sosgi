

package slim.ouertani.osgi.security.impl



case class RichConditionalPermissionInfo(accessDecision:RichAccessDecision,
                                         richConditionInfos: List[RichConditionInfo],
                                         richPermissionInfos: List[RichPermissionInfo] ,
                                         name:String )
{


}