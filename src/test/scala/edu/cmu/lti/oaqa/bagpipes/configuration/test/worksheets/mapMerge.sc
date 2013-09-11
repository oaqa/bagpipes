package edu.cmu.lti.oaqa.cse.configuration.test.worksheets

object mapMerge {
  val map1 = Map("a" -> 1, "b" -> Map("foo" -> 2, "bar" -> 3))
                                                  //> map1  : scala.collection.immutable.Map[String,Any] = Map(a -> 1, b -> Map(fo
                                                  //| o -> 2, bar -> 3))
  val map2 = Map("a" -> 2, "b" -> Map("foo" -> 4, "baz" -> 5))
                                                  //> map2  : scala.collection.immutable.Map[String,Any] = Map(a -> 2, b -> Map(fo
                                                  //| o -> 4, baz -> 5))
  map1.map {
    case (k, v) => (k -> map2(k))
  //  case (k, Map[String, Any]) => map2(k).asInstanceOf[Map[String, Any]]
  }                                               //> res0: scala.collection.immutable.Map[String,Any] = Map(a -> 2, b -> Map(foo 
                                                  //| -> 4, baz -> 5))

}