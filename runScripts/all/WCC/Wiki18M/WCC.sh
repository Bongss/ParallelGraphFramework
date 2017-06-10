#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=WCC.jar
GRAPH_FILE=/home/bongki/Dropbox/GraphDataSets/dbpedia-link/out.dbpedia-link
NUM_THREAD=8
EXP_OF_TASK_SIZE=16

OUTPUT_VALUE_PATH=/home/bongki/Dropbox/ExperimentData/WCC/Parallel/Wiki18M/Thread${NUM_THREAD}/Values
OUTPUT_TIME_PATH=/home/bongki/Dropbox/ExperimentData/WCC/Parallel/Wiki18M/Thread${NUM_THREAD}/Time

mkdir -p ${OUTPUT_VALUE_PATH}
mkdir -p ${OUTPUT_TIME_PATH}

for i in {1..20}
do
    echo "WCC THRESHOLD ${threshold}"
    echo -n "WCC Threshold ${threshold} : " >> ${OUTPUT_TIME_PATH}/WCC_time.txt

    for threshold in {1..20}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} 0 ${EXP_OF_TASK_SIZE} ${OUTPUT_VALUE_PATH}/WCC_${threshold}.txt ${threshold} 1>> ${OUTPUT_TIME_PATH}/WCC_time2.txt
    done
done
