#!/bin/bash
PROGRAM=$1
FILE=$2
NUM_THREAD=$3
DELTA=$4
ASYNC=$5
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph
EXP=$6


java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${FILE} ${NUM_THREAD} ${DELTA} ${ASYNC} ${EXP}
