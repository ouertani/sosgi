
package slim.ouertani.osgi.security.impl

case class RichPermissionInfoDetails( tipe: String, name: Option[String], actions:Option[String])
case class RichPermissionInfo( tipe: String, name: Option[RichPermissionInfoDetails])