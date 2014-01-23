import NativePackagerKeys._

mainClass := Some("edu.cmu.lti.oaqa.bagpipes.cmd.CmdParser")

//managedSourceDirectories in Compile += baseDirectory.value / "src/test/"

//managedResourceDirectories in Compile += baseDirectory.value / "src/test/resources/"

//resources += file("./src/test/resources")

packageArchetype.java_application

//scriptClasspath += baseDirectory.value + "src/test/resources"
