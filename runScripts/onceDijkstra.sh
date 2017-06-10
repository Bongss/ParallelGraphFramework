#!/bin/bash
PROGRAM=$1
FILE=$2
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph


java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${FILE}
