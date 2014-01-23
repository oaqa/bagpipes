package edu.cmu.lti.oaqa.bagpipes.configuration
import Implicits._
import ParserUtils._
import scala.io.Source._
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import scala.collection.JavaConverters._

/**
 * A concrete implementation of [[$packagePath.Parser]] for loading in YAML
 * configuration descriptors.
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

class YAMLParser(baseDir: Option[String] = None) extends Parser(baseDir) {

  /**
   * Returns a [[scala.collections.Map]] representation of a Yaml configuration
   * descriptor file from the specified file path.
   *
   * Note: implicitly converts [[java.util.Map]] to [[scala.collections.Map]]
   * using the implicit methods defined in [[[$packagePath.Implicits]].
   *
   * @param file path of the yaml configuration descriptor.
   */
  override def getConfigurationMap(res: String): Map[String, Any] =
    new Yaml().load(res).asInstanceOf[java.util.Map[String, Any]]

  /**
   * Returns a [[scala.collections.Map]] representation of a yaml configuration
   * descriptor file from the specified file path.
   *
   * @param file path of the yaml configuration descriptor.
   */

  override def getConfigurationMapFromFile(path: String): Map[String, Any] = {
    val classpath = { if (baseDir != None) baseDir.get + "/" else "" } + path + ".yaml"
    val content = loadFileContent(classpath)
    getConfigurationMap(content)
  }

}

object YAMLParser {
  def apply(baseDir: Option[String] = None) = new YAMLParser(baseDir)
}