#!/bin/bash

#File="/home/bongki/GraphTestFiles/soc-LiveJournal1.txt"
File=$1
#NUM_THREADS="4"
NUM_THREADS="4 8 16"
PageRank_OUTPUT_FILE="/home/bongki/ExperimentResults/PageRank/LiveJournal_PR4.txt"
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph
FILE_DIR=/home/bongki/GraphTestFiles

mkdir -p /home/bongki/ExperimentResults/PageRank

for thr in $NUM_THREADS; do
    echo " Thread ${thr} Atomic ${EXP}" >> ${PageRank_OUTPUT_FILE}
    for i in {1 2 3 4 5}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/PageRank.jar ${FILE_DIR}/${File} ${thr} 0 1>> ${PageRank_OUTPUT_FILE}
    done
done

for thr in $NUM_THREADS; do
    echo " Thread ${thr} Async ${EXP}" >> ${PageRank_OUTPUT_FILE}
    for i in {1 2 3 4 5}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/PageRank.jar ${FILE_DIR}/${File} ${thr} 1 1>> ${PageRank_OUTPUT_FILE}
    done
done



