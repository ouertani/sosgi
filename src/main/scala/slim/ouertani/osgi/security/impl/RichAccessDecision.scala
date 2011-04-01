
package slim.ouertani.osgi.security.impl

sealed abstract class RichAccessDecision

object AllowAccessDecision extends RichAccessDecision {
  override def toString ="+"
}
object DenyAccessDecision extends RichAccessDecision {
override def toString = "-"
}