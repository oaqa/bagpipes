BagPipes
========

BagPipes is a second-generation configuration space exploration framework.  It
enables intelligent, algorithmic exploration of configuration spaces under time
and resource constraints.  A configuration space is defined as a phase of a
pipeline with mutliple components and/or any component with a variable
parameter (discrete- and real-valued, cross products).  The performance of a
generated pipeline is further measured by any number of metrics provided by
scoring annotators.  Furthermore, individual annotators can be scored based on
<some metrics>...

BagPipes can (eventually) parallelize executions for faster evaluation of a
configuration space.

This module is part of the Open Advancement of Question Answering
([OAQA](https://mu.lti.cs.cmu.edu/trac/oaqa2.0)) project.

# Quickstart
BagPipes can be included as a library dependency or executed as standalone project. 
## For Developers

### As a command-line tool:
The bagpipes command-line tool can be used to generate and execute yaml component and pipeline descriptors. To use the command-line tool as a standalone executable:

1. Clone the git repo to your local machine (`git clone https://github.com/oaqa/bagpipes.git`)
2. Go to the root directory of the project. 
3. Run `sbt stage`. This will generate the command-line executable at /path/to/bagpipes/target/universal/stage/bin/

For a full tutorial on how to generate/run configurations or setup as part of an external project, go [here](https://github.com/oaqa/bagpipes/wiki/Command-Line-HOWTO) or run `bagpipes --help` to view more options. 
#### Generating a configuration
To generate the most basic configuration (name, author, and collection-reader):

```bash
bagpipes init
```

This will result in the following yaml configuration file:

```yaml
configuration:
  author: default
  name: default
collection-reader:
  inherit: collection_reader.filesystem-collection-reader
```
#### Example:
BagPipes comes with built-in components based on the [UIMA tutorial](http://uima.apache.org/downloads/releaseDocs/2.1.0-incubating/docs/html/tutorials_and_users_guides/tutorials_and_users_guides.html). To generate the most basic RoomAnnotator pipeline configuration run:

```bash
bagpipes init pl -n --collection-reader "collection_reader.filesystem-collection-reader" --component "tutorial.ex1.RoomNumberAnnotator" Pp1=foo p2=bar 
``` 

This will result in the following configuration:

```yaml
configuration:
  author: default
  name: default
collection-reader:
  inherit: collection_reader.filesystem-collection-reader

pipeline:
  - inherit: tutorial.ex1.RoomNumberAnnotator
    params:
      p1: foo
      p2: bar
```
#### Execution
To execute an arbitrary yaml `myDesc.yaml` configuration, simply run:

```bash
bagpipes exec myDesc
```

For example, to execute the configuration above, you can run,

```bash 
bagpipes init pl -n --collection-reader "collection_reader.filesystem-collection-reader" --component "tutorial.ex1.RoomNumberAnnotator" Pp1=foo p2=bar > myDesc.yaml
```
Then execute with,

`bagpipes exec myDesc`  

### As a dependency
To include BagPipes as a maven dependency, (for now) clone it to your local machine and add this to your `pom.xml`:

```xml
<dependency>
	<groupId> edu.cmu.lti.oaqa </groupId>
	<artifactId>bagpipes</artifactId>
	<version> 0.0.1 </version>
</dependency>
```

To use within a Java or Scala application use this API call to execute an arbitrary YAML configuration:

```java
BagPipesRun.run(myYaml);
```
## For Users
