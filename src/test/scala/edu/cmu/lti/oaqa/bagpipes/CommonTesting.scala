package edu.cmu.lti.oaqa.bagpipes
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters._

object CommonTesting {
  trait yamls {
    val ex0 = """ 
configuration:
  name: oaqa-tutorial
  author: oaqa 
  
collection-reader:
  inherit: collection_reader.filesystem-collection-reader"""
    val ex1 = ex0 + """
pipeline:
  - phase: RoomNumberAnnotators
    options: 
     - inherit: tutorial.ex1.RoomNumberAnnotator"""
    val ex2 = ex1 + """
     - inherit: tutorial.ex2.RoomNumberAnnotator"""
    val exPhase2 = """ 
  - phase: DateTimeAnnotators
    options: 
     - inherit: tutorial.ex3.SimpleTutorialDateTime
     - inherit: tutorial.ex3.TutorialDateTime"""
    val ex3 = ex1 + exPhase2
    val ex4 = ex2 + exPhase2
    val ex5 = ex0 + """
pipeline:
  inherit: tutorial.ex1.RoomNumberAnnotator"""
    val ex6 = ex0 + """
pipeline:
  - phase: CrossOpted
    options: 
      - inherit: components.crossOpted"""
  }

  trait progConfigs {
    //Collection reader
    val collectionReaderParams = Map("InputDirectory" -> StringParameter("src/test/resources/data/"), "Language" -> StringParameter("en"), "BrowseSubdirectories" -> BooleanParameter(false), "Encoding" -> StringParameter("UTF-8"))
    val collectionReader = CollectionReaderDescriptor("uima.components.collection.FileSystemCollectionReader", collectionReaderParams)

    val config = Configuration("oaqa-tutorial", "oaqa")

    //params
    val testParams = Map("test" -> StringParameter("param1"))
    val crossOptsParams = Map(
      "opt1" -> ListParameter(List(StringParameter("a"), StringParameter("b"), StringParameter("c"))),
      "opt2" -> ListParameter(List(IntegerParameter(1), IntegerParameter(2))))
    val locations = List("Watson - Yorktown", "Watson - Hawthorne I", "Watson - Hawthorne II")
    val patterns = List("\\b[0-4]\\d[0-2]\\d\\d\\b")

    val roomannotator2Params = Map[String, Parameter]("Locations" -> locations, "Patterns" -> patterns)

    //annotators
    //Ex1: RoomNumberAnnotator: 
    val roomAnnotator1 = ComponentDescriptor(getPath(1, "RoomNumberAnnotator"))//, testParams)
    //Ex2: RoomNumberAnnotator
    val roomAnnotator2 = ComponentDescriptor(getPath(2, "RoomNumberAnnotator"), roomannotator2Params)

    val simpleDateTimeAnnotator = ComponentDescriptor(getPath(3, "SimpleTutorialDateTime"), testParams)
    val dateTimeAnnotator = ComponentDescriptor(getPath(3, "TutorialDateTime"), testParams)

    val crossParams1: List[CrossOptParam] = ("opt1", List("a", "b", "c")) :: ("opt2", List(1, 2)) :: Nil
    //cross-opts descriptor
    val crossOptAnnotator1 = CrossComponentDescriptor("components.CrossOpted", testParams,
      // convert list of tuples to map. (Note: seems verbose, maybe there is a nicer idiom for this).
      crossParams1.foldLeft(Map[String, ListParameter]())((x, y) => x ++ Map((y._1, ListParameter(y._2.map(primitive2Parameter))))))
    //expanded cross-opted configured descriptors (i.e., the result of expanding these annotators)  
    val expandedCrossOptAnnotators1: List[ComponentDescriptor] = for (param <- expandCrossOpts(crossParams1)) yield ComponentDescriptor("components.CrossOpted", testParams ++ param)

    //phases
    val phase1 = PhaseDescriptor("RoomNumberAnnotators", List(roomAnnotator1))
    val phase2 = PhaseDescriptor("RoomNumberAnnotators", List(roomAnnotator1, roomAnnotator2))
    val phase3 = PhaseDescriptor("DateTimeAnnotators", List(simpleDateTimeAnnotator, dateTimeAnnotator))
    val phase4 = PhaseDescriptor("CrossOpted", crossOptAnnotator1 :: Nil)
    val phase4Expanded = PhaseDescriptor("CrossOpted", expandedCrossOptAnnotators1)
    //examples 
    val confEx0 = ConfigurationDescriptor(config, collectionReader, Nil)
    val confEx1 = ConfigurationDescriptor(config, collectionReader, phase1 :: Nil)
    val confEx2 = ConfigurationDescriptor(config, collectionReader, phase2 :: Nil)
    val confEx3 = ConfigurationDescriptor(config, collectionReader, phase1 :: phase3 :: Nil)
    val confEx4 = ConfigurationDescriptor(config, collectionReader, phase2 :: phase3 :: Nil)
    val confEx5 = ConfigurationDescriptor(config, collectionReader, phase4 :: Nil) // List(roomAnnotator1))
    val confEx6 = ConfigurationDescriptor(config, collectionReader, phase4Expanded :: Nil) // List(crossOptedAnnotator))
  }
  //convenience methods:
  // get file path from common classpath of the uima tutorial example directory
  private val baseClassPath = "uima.components.examples"
  private def getPath(exNum: Int, name: String) = baseClassPath + ".ex%d.%s" format (exNum, name)

  //cross-opts
  // convenience method for generating the cross opt from a list of Tuple2[String,List[_] for Testing purposes.
  type CrossOptParam = (String, List[_])
  private def expandCrossOpts(paramPairs: List[CrossOptParam]): List[Map[String, Parameter]] = {
    def genAllPairs(cParam: CrossOptParam): List[(String, Parameter)] = cParam._2.map(v => (cParam._1, primitive2Parameter(v)))
    paramPairs match {
      case Nil => List(Map())
      case head :: tail =>
        for {
          pair <- genAllPairs(head)
          next <- expandCrossOpts(tail)
        } yield (Map(pair) ++ next)
    }
  }
}