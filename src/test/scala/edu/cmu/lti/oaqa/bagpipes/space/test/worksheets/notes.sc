package edu.cmu.lti.oaqa.cse.space.test.worksheets

object notes {

  Map(1 -> 2) ++ List(Map("a" -> "b", "c" -> "d")).flatten
                                                  //> res0: scala.collection.immutable.Map[Any,Any] = Map(1 -> 2, a -> b, c -> d)
                                                  //| 
  def crossParamsExpander[K, V](m: Map[K, List[V]]): List[Map[K, V]] = m.keys.toList match {
    case Nil => List(Map())
    case head :: tail =>
      for {
        p <- m(head)
        pMap <- crossParamsExpander(m.tail.toMap)
      } yield  Map(head -> p) ++ pMap
  }                                               //> crossParamsExpander: [K, V](m: Map[K,List[V]])List[Map[K,V]]

  def combineMaps[K, V](map: Map[K, V], rest: List[Map[K, V]]) = for (pMap <- rest) yield (map ++ pMap)
                                                  //> combineMaps: [K, V](map: Map[K,V], rest: List[Map[K,V]])List[scala.collectio
                                                  //| n.immutable.Map[K,V]]

  crossParamsExpander(Map("opt1" -> List("a", "b", "c"), "opt2" -> List("1", "2")))
                                                  //> res1: List[Map[String,String]] = List(Map(opt1 -> a, opt2 -> 1), Map(opt1 ->
                                                  //|  a, opt2 -> 2), Map(opt1 -> b, opt2 -> 1), Map(opt1 -> b, opt2 -> 2), Map(op
                                                  //| t1 -> c, opt2 -> 1), Map(opt1 -> c, opt2 -> 2))

}