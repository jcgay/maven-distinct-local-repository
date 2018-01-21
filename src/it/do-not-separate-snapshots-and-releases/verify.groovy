#!/usr/bin/env groovy
String buildLog = new File(basedir, 'build.log').text

def lines = buildLog.readLines()
    .findAll { it.startsWith('[INFO] Installing') }
    .collect { it =~ /\[INFO] Installing .* to (.*)/ }
    .findAll { it.matches() }
    .collect { it[0][1] }

assert lines != []
lines.each { assert it.startsWith("$localRepositoryPath/fr/jcgay/maven/extension/mdlr/not-activated/1.0-SNAPSHOT/") }

return true
