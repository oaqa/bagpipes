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

#### Generating a configuration

#### Executing

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
