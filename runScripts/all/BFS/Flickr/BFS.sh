#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=BFS.jar
GRAPH_FILE=/home/bongki/GraphDataSets/flickr-growth/out.flickr-growth
NUM_THREAD=8
EXP_OF_TASK_SIZE=16

OUTPUT_VALUE_PATH=/home/bongki/Dropbox/ExperimentData/BFS/Parallel/Flickr/Thread${NUM_THREAD}/Values
OUTPUT_TIME_PATH=/home/bongki/Dropbox/ExperimentData/BFS/Parallel/Flickr/Thread${NUM_THREAD}/Time

mkdir -p ${OUTPUT_VALUE_PATH}
mkdir -p ${OUTPUT_TIME_PATH}

for threshold in {1..20}
do
    echo "BFS THRESHOLD ${threshold}"
    echo -n "BFS Threshold ${threshold} : " >> ${OUTPUT_TIME_PATH}/BFS_time.txt
    java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} 0 ${EXP_OF_TASK_SIZE} ${OUTPUT_VALUE_PATH}/BFS_${threshold}.txt ${threshold} 1 1>> ${OUTPUT_TIME_PATH}/BFS_time.txt
done
