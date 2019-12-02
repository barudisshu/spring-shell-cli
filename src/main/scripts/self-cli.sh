#!/bin/bash

if [ -z "$JAVA_HOME" ]; then
  export JAVA=$(which java)
else
  export JAVA="../jre/bin/java"
fi

if [ -z "$BASE_DIR" ]; then
  PRG="$0"
  while [ -h "$PRG" ]; do
    ls=$(ls -ld "$PRG")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' >/dev/null; then
      PRG="$link"
    else
      PRG="$(dirname "$PRG")/$link"
    fi
  done
  BASE_DIR=$(dirname "$PRG")/..
  BASE_DIR=$(cd "$BASE_DIR" && pwd)
fi

UEAP_HOME=$BASE_DIR
UEAP_JVM_ARGS="-Xmx512m -Xms256m -server"
UEAP_JVM_ARGS="$UEAP_JVM_ARGS -jar ../lib/cplier-prompt-cli-0.0.1.jar"

if [ -z "$UEAP_ARGS" ]; then
  export UEAP_ARGS="$UEAP_JVM_ARGS"
fi

echo "$JAVA $UEAP_JVM_ARGS"
sleep 1
$JAVA $UEAP_JVM_ARGS
