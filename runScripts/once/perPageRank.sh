#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PerPageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD=16
# IF THRESHOLD == -1, then ASYNC 100 % , 0 ATOMIC 100%
THRESHOLD=-1
EXP_OF_PARTITION_SIZE=16
SEED_FILE=/home/bongki/GraphDataSets/PR_SeedSetFiles/10000_LJ1.txt
NUM_SEED=10000
ITER=10
OUTPUT_FILE=/home/bongki/Dropbox/ExperimentData/PageRank/Parallel/LiveJournal/Thread${NUM_THREAD}/Values/ITR_${ITER}_Atomic.txt

#mkdir -p /home/bongki/Dropbox/ExperimentData/PageRank/Parallel/LiveJournal/Thread${NUM_THREAD}/Values

echo "NUM_THREAD : ${NUM_THREAD}"
echo "GRAPH_FILE : ${GRAPH_FILE}"
echo "ASYNC_PERCENTAGE : ${ASYNC_PERCENTAGE}"
echo "EXP_OF_PARTITION_SIZE : ${EXP_OF_PARTITION_SIZE}"
echo "SEED_FILE : ${SEED_FILE}"
echo "NUM_SEED : ${NUM_SEED}"
echo "OUTPUT_FILE : ${OUTPUT_FILE}"
echo "ITER: ${ITER}"
echo " "

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} ${THRESHOLD} ${EXP_OF_PARTITION_SIZE} ${SEED_FILE} ${NUM_SEED} ' ' ${ITER}
