#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/flickr-growth/out.flickr-growth
NUM_THREAD="4 8 16"
ATOMIC=0
ASYNC=-1
EXP_OF_PARTITION_SIZE=16
ITER=10


for thr in ${NUM_THREAD}
do
    echo "PageRank Thread : ${thr}" 
    OUTPUT_FILE_PATH=/home/bongki/Dropbox/ExperimentData/PageRank/Parallel/NO_Partition/Flickr/Thread${thr}/failCounts
    mkdir -p ${OUTPUT_FILE_PATH}

    OUTPUT_ATOMIC_FILE=${OUTPUT_FILE_PATH}/ITR_${ITER}_Atomic.txt

    java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${ATOMIC} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ATOMIC_FILE} ${ITER} | grep FAIL_COUNT | awk -F ' ' ' { print $2 } ' 1>> ${OUTPUT_ATOMIC_FILE}

    for threshold in 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 40000
    do
       OUTPUT_ASYNC_FILE=${OUTPUT_FILE_PATH}/ITR_${ITER}_THR_${threshold}_ASync.txt
       java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${threshold} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ASYNC_FILE} ${ITER} | grep FAIL_COUNT | awk -F ' ' ' { print $2 } ' 1>> ${OUTPUT_ASYNC_FILE}
    done
done
