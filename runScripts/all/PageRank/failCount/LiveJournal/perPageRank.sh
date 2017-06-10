#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PerPageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD="4 8 16"
ASYNC_PERCENTAGE=0
ATOMIC=0
ASYNC=-1
EXP_OF_PARTITION_SIZE=16
SEED_FILE=/home/bongki/GraphDataSets/PR_SeedSetFiles/10000_LJ1.txt
NUM_SEED=10000

ITER=10

for thr in ${NUM_THREAD}
do
    echo "PersonalizedPageRank Thread : ${thr}" 
    OUTPUT_FILE_PATH=/home/bongki/Dropbox/ExperimentData/PersonalizedPageRank/Parallel/NO_Partition/LiveJournal/Thread${thr}/failCounts
    mkdir -p ${OUTPUT_FILE_PATH}

    OUTPUT_ATOMIC_FILE=${OUTPUT_FILE_PATH}/ITR_${ITER}_Atomic.txt

    java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${thr} ${ATOMIC} ${EXP_OF_PARTITION_SIZE} ${SEED_FILE} ${NUM_SEED} ${OUTPUT_ATOMIC_FILE} ${ITER}
done
