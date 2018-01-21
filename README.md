# maven-distinct-local-repository

This Maven extension aims to separate SNAPSHOTs and RELEASEs artifacts and metadata in local repository.

For example, if your local Maven repository is located at `~/.m2/repository`:

 - SNAPSHOTs will be written at `~/.m2/repository/snapshots`,
 - RELEASEs will be written at `~/.m2/repository/releases`
 
It is handy to manage a cache only for released artifacts (on your CI server...) or to just delete all your `SNAPSHOTs` at once.
 
## Installation

Get [maven-distinct-local-repository](https://dl.bintray.com/jcgay/maven/fr/jcgay/maven/extension/maven-distinct-local-repository/1.0/maven-distinct-local-repository-1.0.jar) and copy it in `%M2_HOME%/lib/ext` folder (where `%M2_HOME` targets your local Maven installation).

*or*

Use the new [core extensions configuration mechanism](http://takari.io/2015/03/19/core-extensions.html) by creating a `${maven.multiModuleProjectDirectory}/.mvn/extensions.xml` file with:

```
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
    <extension>
      <groupId>fr.jcgay.maven.extension</groupId>
      <artifactId>maven-distinct-local-repository</artifactId>
      <version>1.0</version>
    </extension>
</extensions>
```

## Usage

Run your build using property `distinct.local.repository=true`:

    mvn install -Ddistinct.local.repository=true

Dependencies will be automatically resolved/stored in distinct folders from/in your local Maven repository.

# Build status

[![Build Status](https://travis-ci.org/jcgay/maven-distinct-local-repository.svg?branch=master)](https://travis-ci.org/jcgay/maven-distinct-local-repository)
[![Coverage Status](https://coveralls.io/repos/jcgay/maven-distinct-local-repository/badge.svg?branch=master)](https://coveralls.io/r/jcgay/maven-distinct-local-repository?branch=master)

# Release

    mvn -B release:prepare release:perform