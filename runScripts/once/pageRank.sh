#!/bin/bash
PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=PageRank.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
NUM_THREAD=8
THRESHOLD=0
EXP_OF_PARTITION_SIZE=16
ITER=10
#OUTPUT_FILE=/home/bongki/Dropbox/ExperimentData/PageRank/Parallel/LiveJournal/Thread${NUM_THREAD}/Values/ITR_${ITER}_Atomic.txt

#mkdir -p /home/bongki/Dropbox/ExperimentData/PageRank/Parallel/LiveJournal/Thread${NUM_THREAD}/Values

echo "NUM_THREAD : ${NUM_THREAD}"
echo "GRAPH_FILE : ${GRAPH_FILE}"
echo "THRESHOLD : ${THRESHOLD}"
echo "EXP_OF_PARTITION_SIZE : ${EXP_OF_PARTITION_SIZE}"
echo "OUTPUT_FILE : ${OUTPUT_FILE}"
echo "ITER: ${ITER}"
echo " "

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} ${THRESHOLD} ${EXP_OF_PARTITION_SIZE} "PG_Test.txt" ${ITER}
#java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${NUM_THREAD} ${THRESHOLD} ${EXP_OF_PARTITION_SIZE} "test.txt" ${ITER} | grep FAIL_COUNT | awk -F ' ' ' { print $2 } ' >> abc.txt
