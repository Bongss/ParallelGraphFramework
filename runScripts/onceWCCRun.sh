#!/bin/bash
PROGRAM=WCC.jar
FILE=$1
NUM_THREAD=$2
ASYNC=$3
EXP=$4
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph
SEED=-1
NUM_CHECK=-1


java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} /home/bongki/GraphTestFiles/${FILE} ${NUM_THREAD} ${ASYNC} ${EXP} ${SEED} ${NUM_CHECK}
