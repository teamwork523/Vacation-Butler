#!/bin/bash

# Search for the "FAT" jar, and spin off the Jetty server locally
java -jar $(find . -iname vb-api-services*.jar -type f) server
