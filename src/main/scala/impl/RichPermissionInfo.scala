package slim.ouertani.osgi.security.impl

case class RichPermissionInfoDetails( tipe: String, name: Option[String], actions:Option[String]) {
  override def toString=tipe + " " +name.getOrElse("")+ " " + actions.getOrElse("")
}
case class RichPermissionInfo( tipe: String, name: Option[RichPermissionInfoDetails]) {
   override def toString=tipe + " " + name.getOrElse("")
}