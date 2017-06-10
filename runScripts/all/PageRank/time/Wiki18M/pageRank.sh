#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PageRank.jar
GRAPH_FILE=/home/bongki/Dropbox/Graph/dbpedia-link/out.dbpedia-link
NUM_THREAD="16"
ATOMIC=0
ASYNC=-1
EXP_OF_PARTITION_SIZE=16
ITER=10


for thr in ${NUM_THREAD}
do
    echo "PageRank Thread : ${thr}" 
    OUTPUT_FILE_PATH=/home/bongki/Dropbox/ExperimentData/PageRank/Parallel/NO_Partition/Wiki18M/Thread${thr}/Time
    mkdir -p ${OUTPUT_FILE_PATH}

    OUTPUT_ATOMIC_FILE=${OUTPUT_FILE_PATH}/ITR_${ITER}_Atomic.txt

    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${ATOMIC} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ATOMIC_FILE} ${ITER} 1>> ${OUTPUT_ATOMIC_FILE}

        for threshold in -1 100 200 300 400 1000 2000 3000 10000 20000 30000 40000 100000 200000 300000 400000
        do
            OUTPUT_ASYNC_FILE=${OUTPUT_FILE_PATH}/ITR_${ITER}_THR_${threshold}_ASync.txt
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${threshold} ${EXP_OF_PARTITION_SIZE} ${OUTPUT_ASYNC_FILE} ${ITER} 1>> ${OUTPUT_ASYNC_FILE}
        done
    done
done
