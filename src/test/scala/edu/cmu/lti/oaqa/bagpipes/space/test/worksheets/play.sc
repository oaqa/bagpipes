package edu.cmu.lti.oaqa.cse.space.test.worksheets

object play {
	val x = Stream(1,2,3)                     //> x  : scala.collection.immutable.Stream[Int] = Stream(1, ?)
	val y = Stream(1,2,3)                     //> y  : scala.collection.immutable.Stream[Int] = Stream(1, ?)
	y equals x                                //> res0: Boolean = true
 
}