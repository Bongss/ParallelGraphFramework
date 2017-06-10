#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/flickr-growth/out.flickr-growth
NUM_THREAD="16"
ATOMIC=0
ASYNC=-1
EXP_OF_PARTITION_SIZE=16
ITER=10


for thr in ${NUM_THREAD}
do
    echo "PageRank Thread : ${thr}" 
    OUTPUT_FILE_PATH=/home/bongki/Dropbox/ExperimentData/PageRank/Parallel/NO_Partition/Flickr/Thread${thr}/Time
    mkdir -p ${OUTPUT_FILE_PATH}

    OUTPUT_ATOMIC_FILE=${OUTPUT_FILE_PATH}/2_ITR_${ITER}_Atomic.txt

    for i in {1..10}
    do
        #java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${ATOMIC} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ATOMIC_FILE} ${ITER} 1>> ${OUTPUT_ATOMIC_FILE}

        for threshold in -1 100 200 300 400 1000 2000 3000 4000 5000
        do
            OUTPUT_ASYNC_FILE=${OUTPUT_FILE_PATH}/2_ITR_${ITER}_THR_${threshold}_ASync.txt
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${threshold} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ASYNC_FILE} ${ITER} 1>> ${OUTPUT_ASYNC_FILE}
        done
    done
done
