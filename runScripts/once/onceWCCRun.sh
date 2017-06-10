#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=WCC.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD=8
EXP_OF_TASK_SIZE=16

OUTPUT_PATH=/home/bongki/Dropbox/ExperimentData/WCC/Parallel/LiveJournal/Thread${NUM_THREAD}/Values
OUTPUT_FILE=$1
THRESHOLD=$2

mkdir -p ${OUTPUT_PATH}

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} 0 ${EXP_OF_TASK_SIZE} ${OUTPUT_FILE} ${THRESHOLD}

