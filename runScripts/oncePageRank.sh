#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/PartitionBasedGraph
PROGRAM=PageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD=16
ASYNC_PERCENTAGE=0
EXP_OF_PARTITION_SIZE=16

echo "NUM_THREAD : ${NUM_THREAD}"
echo "GRAPH_FILE : ${GRAPH_FILE}"
echo "ASYNC_PERCENTAGE : ${ASYNC_PERCENTAGE}"
echo "EXP_OF_PARTITION_SIZE : ${EXP_OF_PARTITION_SIZE}"
echo " "

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} ${ASYNC_PERCENTAGE} ${EXP_OF_PARTITION_SIZE}
