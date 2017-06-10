#!/bin/bash

#File="/home/bongki/GraphTestFiles/soc-LiveJournal1.txt"
File="/home/bongki/GraphTestFiles/Live_no_self.txt.weighted"
NUM_THREADS="8"
EXP="4 8 12 16"
DELTAS="0.0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0"
WCC_OUTPUT_FILE="/home/bongki/ExperimentResults/WCC/LiveJournal_OP2_WCC.txt"
PageRank_OUTPUT_FILE="/home/bongki/ExperimentResults/PageRank/LiveJournal_PR2.txt"
SSSP_OUTPUT_FILE="/home/bongki/ExperimentResults/SSSP/LiveJournal_SSSP.txt"
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph

mkdir -p /home/bongki/ExperimentResults/WCC/
mkdir -p /home/bongki/ExperimentResults/PageRank/
mkdir -p /home/bongki/ExperimentResults/SSSP/

for thr in $NUM_THREADS; do
    for exp in $EXP; do
        echo "SSSP Thread ${thr} Async ${EXP}" >> ${SSSP_OUTPUT_FILE}
        for i in {1 2 3 4 5}
        do
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/SSSP.jar ${File} ${thr} 20 1 ${exp} 1>> ${SSSP_OUTPUT_FILE}
        done
    done
done
for thr in $NUM_THREADS; do
    for exp in $EXP; do
        echo "SSSP Thread ${thr} Atomic ${EXP}" >> ${SSSP_OUTPUT_FILE}
        for i in {1 2 3 4 5}
        do
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/SSSP.jar ${File} ${thr} 20 0 ${exp} 1>> ${SSSP_OUTPUT_FILE}
        done
    done
done



: << END
echo "Partition Size Different 4 8 12 16 , Thread 8, ConcurrentQueue" >> ${WCC_OUTPUT_FILE}
for thr in $NUM_THREADS; do
    for exp in $EXP; do
        echo "WCC Thread ${thr} Async ${EXP}" >> ${WCC_OUTPUT_FILE}
        for i in {1}
        do
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/WCC.jar ${File} ${thr} 1 ${exp} 1>> ${WCC_OUTPUT_FILE}
        done
    done
done

for thr in $NUM_THREADS; do
    for exp in $EXP; do
        echo "WCC Thread ${thr} Atomic ${EXP}" >> ${WCC_OUTPUT_FILE}
        for i in {1}
        do
            java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/WCC.jar ${File} ${thr} 0 ${exp} 1>> ${WCC_OUTPUT_FILE}
        done
    done
done
END
: << END
for thr in $NUM_THREADS; do
    echo "WCC Thread ${thr} Atomic" >> ${WCC_OUTPUT_FILE}
    for i in {8..16}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/WCC.jar ${File} ${thr} 0 ${i} 1>> ${WCC_OUTPUT_FILE}
    done
done
END
: << END
for thr in $NUM_THREADS; do
    echo "WCC Thread ${thr} Atomic" >> ${WCC_OUTPUT_FILE}
    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ./jar/WCC.jar ${File} ${thr} 0 1>> ${WCC_OUTPUT_FILE}
    done
done
END

: << END
for delta in $DELTAS; do
    echo "PageRank ${delta}" >> ${PageRank_OUTPUT_FILE}
    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ./jar/PageRank.jar ${File} 16 ${delta} 1>> ${PageRank_OUTPUT_FILE}
    done
done
END
: << END
for thr in $NUM_THREADS; do
    echo "WCC Thread ${thr} Async" >> ${WCC_OUTPUT_FILE}
    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ./jar/WCC.jar ${File} ${thr} 1 1>> ${WCC_OUTPUT_FILE}
    done
done

for thr in $NUM_THREADS; do
    echo "WCC Thread ${thr} Atomic" >> ${WCC_OUTPUT_FILE}
    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ./jar/WCC.jar ${File} ${thr} 0 1>> ${WCC_OUTPUT_FILE}
    done
done

for delta in $DELTAS; do
    echo "PageRank ${delta}" >> ${PageRank_OUTPUT_FILE}
    for i in {1..10}
    do
        java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ./jar/PageRank.jar ${File} 16 ${delta} 1>> ${PageRank_OUTPUT_FILE}
    done
done
END
