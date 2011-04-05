package slim.ouertani.osgi.security.impl


case class RichConditionInfo( tipe:java.lang.String,
                         args:List[String]){


  override def toString =   tipe + args.mkString(" ")
}
