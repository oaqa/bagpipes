package edu.cmu.lti.oaqa.bagpipes.cmd

import org.rogach.scallop._
 
object gen {
  
  object Conf extends ScallopConf(List("-c", "3", "-E", "fruit=apple", "7.2"),"") {
    // all options that are applicable to builder (like description, default, etc)
    // are applicable here as well
    val count: ScallopOption[Int] = opt[Int]("count", descr = "count the trees", required = true)
      .map(1+) // also here work all standard Option methods -
    // evaluation is deferred to after option construction
    val properties = props[String]('E')
    // types (:ScallopOption[Double]) can be omitted, here just for clarity
    val size: ScallopOption[Double] = trailArg[Double](required = false)
  };import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(716); 
println("hello")}
   
  
        
}
