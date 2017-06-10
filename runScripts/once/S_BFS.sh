PROJECT_DIR=/home/bongki/Dropbox/CodeRepository/jung_bong
PROGRAM=S_BFS.jar
GRAPH_FILE=/home/bongki/GraphDataSets/soc-LiveJournal1.txt
OUTPUT_FILE=S_BFS.txt

java -server -Xms16g -Xmx16g -da -XX:BiasedLockingStartupDelay=0 -XX:MaxInlineSize=256 -XX:FreqInlineSize=256 -jar ${PROJECT_DIR}/jar/${PROGRAM} ${GRAPH_FILE} ${OUTPUT_FILE}
