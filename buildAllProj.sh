#!/bin/sh

#
# Build all subdirectories with pom.xml files
#
# Use whenever your enlistment contains multiple (but not necessarily all) VB modules.
#
# @Author: Haokun Luo
#

# What do you want Maven to do?
GOALS=$*
if [ -z "$GOALS" ]; then
	echo Runs maven on a temporary aggregator pom file, built to include whatever you\'re enlisted in
	echo Usage: $0 \<goal\> ...
	exit
fi

# Create a temp file to hold the "aggregator" POM
TMP_POM=$0_pom.xml
echo Creating temporary POM file $TMP_POM ...
cp /dev/null $TMP_POM

# Everything before the list of pom files
# TODO: Get the editor to stop replacing tabs with spaces, then use <<- and indent the contents
# TODO: Add XML comments with a timestamp and the source directory
cat >> $TMP_POM <<- END
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
    <groupId>com.vb</groupId>
    <version>vb-project-SNAPSHOT</version>
	<artifactId>BuildAllProj</artifactId>
	<packaging>pom</packaging>

	<modules>
END

# Look for POM files and note each
for x in `ls */pom.xml | sed -e "s/\/pom.xml//" | sort `
do
    echo Pointing to POM file $x ...
    echo "	  <module>$x</module>" >> $TMP_POM
done

# And finish off the aggregator POM file
cat >> $TMP_POM <<- END
	</modules>

	</project>
	END

# Debug: Print the temporary POM file
#echo
#echo Temporary POM file:
#cat $TMP_POM
#echo

# Build Maven command
MVN_COMMAND=mvn
MVN_COMMAND="$MVN_COMMAND -f $TMP_POM"
MVN_COMMAND="$MVN_COMMAND $GOALS"

# Invoke Maven
echo Maven command: $MVN_COMMAND
echo Starting Maven ...
echo
echo
$MVN_COMMAND

# Clean up
rm $TMP_POM
