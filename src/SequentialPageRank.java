import gnu.trove.list.array.TIntArrayList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class Node {
    TIntArrayList neighborNodeList;
}

public class SequentialPageRank {
    int numNodes = 4847571;
    int maxNodeId = 0;
    int iteration = 10;
    double d = 0.85;

    double[] newPR;
    double[] oldPR;
    int[] outDeg;
    Node[] nodes;

    private SequentialPageRank() {
    }

    private void doPR(String inFile, String outFile) throws IOException {
        long start = System.currentTimeMillis();

        nodes = new Node[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodes[i] = null;
        }

        oldPR = new double[numNodes];
        newPR = new double[numNodes];

        outDeg = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            outDeg[i] = 0;
        }

        FileInputStream fstream = new FileInputStream(inFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // load edge data from text file (node 1, node 2, dist)
        String strLine;
        int eCnt = 0;
        int nCnt = 0;
        while ((strLine = br.readLine()) != null) {
            String[] nums = strLine.trim().split("\t");
            int source = Integer.parseInt(nums[0]);
            int sink = Integer.parseInt(nums[1]);

            if (source == sink) {
                continue;
            }

            maxNodeId = Math.max(maxNodeId, Math.max(source, sink));

            outDeg[source]++;

            if (nodes[source] == null) {
                Node n = new Node();
                n.neighborNodeList = new TIntArrayList(4);
                nodes[source] = n;
            }

            if (nodes[sink] == null) {
                Node n = new Node();
                n.neighborNodeList = new TIntArrayList(4);
                nodes[sink] = n;
            }

            Node snkNode = nodes[sink];
            snkNode.neighborNodeList.add(source);

            eCnt++;
        }
        in.close();

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        double elapsedTimeSec = elapsedTimeMillis / (1000.0);
        System.out.println("Loaded (sec): " + elapsedTimeSec);

        for (int i = 0; i <= maxNodeId; i++) {
            if (nodes[i] != null) {
                nCnt++;
            }
        }
        System.out.println("Nodes: " + nCnt);
        System.out.println("Edges: " + eCnt);

        start = System.currentTimeMillis(); // new ref time

        for (int i = 0; i <= maxNodeId; i++) {
            if (nodes[i] != null) {
                oldPR[i] = 1.0 / (double) nCnt;
            }
        }

        double t1 = (1.0 - d) / (nCnt);

        for (int itr = 0; itr < iteration; itr++) {
            for (int i = 0; i <= maxNodeId; i++) {
                if (nodes[i] != null) {
                    newPR[i] = t1;

                    Node node = nodes[i];
                    for (int nbrIdx = 0; nbrIdx < node.neighborNodeList.size(); ++nbrIdx) {
                        int from = node.neighborNodeList.get(nbrIdx);
                        newPR[i] = newPR[i] + d * oldPR[from] / (double) outDeg[from];
                    }
                }
            }

            for (int i = 0; i <= maxNodeId; i++) {
                oldPR[i] = newPR[i];
            }
        }

        double prSum = 0.0;

        for (int i = 0; i <= maxNodeId; i++) {
            if (nodes[i] != null) {
                prSum = prSum + newPR[i];
            }
        }
        System.out.println(prSum);

        elapsedTimeMillis = System.currentTimeMillis() - start;
        elapsedTimeSec = elapsedTimeMillis / (1000.0);
        System.out.println("PageRank (sec): " + elapsedTimeSec);

        try (FileWriter fw = new FileWriter(outFile, false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i <= maxNodeId; i++) {
                if (nodes[i] != null) {
                    out.println(newPR[i]);
                } else {
                    out.println(-1);
                }
            }
        } catch (IOException e) {

        }
    }

    static public void main(String[] args) throws IOException {
        SequentialPageRank pr = new SequentialPageRank();
        String inputFile = args[0];
        String outFile = args[1];

        // inputFile, outputFile
        pr.doPR(inputFile, outFile);
    }
}
