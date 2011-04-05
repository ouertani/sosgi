package slim.ouertani.osgi.security.impl


case class RichConditionInfo( tipe:java.lang.String,
                         args:List[String]){



  println ("tipe" + tipe)
  println( "args" + args)
  override def toString =   tipe +" " +  args.mkString(" ")
}
