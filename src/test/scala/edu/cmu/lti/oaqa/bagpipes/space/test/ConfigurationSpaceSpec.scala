package edu.cmu.lti.oaqa.bagpipes.space.test
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import net.liftweb.json.parse
import net.liftweb.json.DefaultFormats
import net.liftweb.json.{ Serialization, ShortTypeHints }
import scala.util.parsing.json.JSONObject
import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.junit._
import org.scalatest.junit.JUnitRunner
import scala.reflect.ClassManifest
import edu.cmu.lti.oaqa.bagpipes.configuration.YAMLParser
import edu.cmu.lti.oaqa.bagpipes.configuration.Parser
import edu.cmu.lti.oaqa.bagpipes.configuration._
import edu.cmu.lti.oaqa.bagpipes.configuration.Implicits._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace 
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._

@RunWith(classOf[JUnitRunner])
class ConfigurationSpaceSpec extends FeatureSpec {
  new confTrees {
    feature("construct simple pipeline trees") {

      scenario("just a collection reader") {
        val testTree0 = ConfigurationSpace(confEx0).getSpace
        assert(testTree0 === confTree0)
      }

      scenario("collection reader + 1 phase with 1 option") {
        val testTree1 = ConfigurationSpace(confEx1).getSpace
        assert(testTree1 === confTree1)
      }

      scenario("collection reader + 1 phase with 2 options") {
        val testTree2 = ConfigurationSpace(confEx2).getSpace
        assert(testTree2 === confTree2)
      }

      scenario("collection reader + 1 phase + 1 phase with 2 options") {
        val testTree3 = ConfigurationSpace(confEx3).getSpace
        assert(testTree3 === confTree3)
      }

      scenario("collection reader + 1 phase with 2 options + 1 phase with 2 options") {
        val testTree4 = ConfigurationSpace(confEx4).getSpace
        assert(testTree4 === confTree4)
      }
    }

    feature("test cross-opted components") {
      scenario("collection reader + 1 phase with 2 crossoptions (3 params + 2 params, expanded)") {
        val testTree5 = ConfigurationSpace(confEx6).getSpace
        assert(testTree5 === confTree5)
      }
      
      /*scenario("collection reader + 1 phase with 2 crossoptions (3 params + 2 params, expanded)") {
        val testTree6  = ConfigurationSpace(confEx5).getSpace
        assert(testTree6 === confTree6)
      }*/
    }
  }
}

