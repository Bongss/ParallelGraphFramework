#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=BFS.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD=8
EXP_OF_TASK_SIZE=16

OUTPUT_PATH=/home/bongki/Dropbox/ExperimentData/BFS/Parallel/LiveJournal/Thread${NUM_THREAD}/Values
OUTPUT_FILE=$1
THRESHOLD=$2

mkdir -p ${OUTPUT_PATH}

#echo "NUM_THREAD : ${NUM_THREAD}"
#echo "GRAPH_FILE : ${GRAPH_FILE}"
#echo "THRESHOLD : ${THRESHOLD}"
#echo "EXP_OF_PARTITION_SIZE : ${EXP_OF_TASK_SIZE}"
#echo "OUTPUT_FILE : ${OUTPUT_FILE}"
#echo " "

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} ${THRESHOLD} ${EXP_OF_TASK_SIZE} ${OUTPUT_FILE} 0
