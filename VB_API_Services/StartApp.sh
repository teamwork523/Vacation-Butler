#!/bin/bash

export JVM_OPTIONS="-Xmx1g -XX:MaxPermSize=256m -agentlib:jdwp=transport=dt_socket,server=y,address=8001,suspend=n"
# Search for the "FAT" jar, and spin off the Jetty server locally
#java $JVM_OPTIONS -jar $(find . -iname vb-api-services*.jar -type f) server 
java $JVM_OPTIONS -jar $(find . -iname vb-api-services*.jar -type f) server src/main/resources/vb-api-services.yml 
