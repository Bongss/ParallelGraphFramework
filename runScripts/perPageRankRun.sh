#!/bin/bash

#File="/home/bongki/GraphTestFiles/soc-LiveJournal1.txt"
File=$1
NUM_THREADS="4 8 16"
#NUM_THREADS="4"
EXP=$2
PageRank_OUTPUT_FILE="/home/bongki/ExperimentResults/PersonalizedPageRank/LiveJournal_PR.txt"
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph
FILE_DIR=/home/bongki/GraphTestFiles
SEED_FILE=$3
NUM_SEED=$4

mkdir -p /home/bongki/ExperimentResults/PersonalizedPageRank

for thr in $NUM_THREADS; do
    echo " Thread ${thr} Atomic ${EXP}" >> ${PageRank_OUTPUT_FILE}
    for i in {1 2 3 4 5}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/PerPageRank.jar ${FILE_DIR}/${File} ${thr} 0 ${EXP} ${SEED_FILE} ${NUM_SEED} 1>> ${PageRank_OUTPUT_FILE}
    done
done

for thr in $NUM_THREADS; do
    echo " Thread ${thr} Async ${EXP}" >> ${PageRank_OUTPUT_FILE}
    for i in {1 2 3 4 5}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/PerPageRank.jar ${FILE_DIR}/${File} ${thr} 1 ${EXP} ${SEED_FILE} ${NUM_SEED} 1>> ${PageRank_OUTPUT_FILE}
    done
done



