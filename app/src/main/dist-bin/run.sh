#!/bin/sh

CURRENT_PATH="$(dirname $0)"
THE_CLASSPATH=$CURRENT_PATH
for JAR in $(ls $CURRENT_PATH/lib/*.jar); do
  THE_CLASSPATH=${THE_CLASSPATH}:${JAR}
done

java -cp "$THE_CLASSPATH" net.sympower.kmtronic.tester.KMTronicTestRunnerMain $@
