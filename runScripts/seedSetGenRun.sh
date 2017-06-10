PROGRAM=SeedSetGen.jar
PROJECT_DIR=/home/bongki/CodeRepository/PartitionBasedGraph
INPUT_FILE=$1
OUTPUT_FILE=$2

for seed in 5000 6000 7000 8000 9000
do
java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} /home/bongki/GraphTestFiles/${INPUT_FILE} ${seed}_${OUTPUT_FILE} ${seed}
done
