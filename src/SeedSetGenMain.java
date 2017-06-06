import graph.Graph;
import graph.GraphUtil;
import graph.sharedData.PersonalPageRankSharedData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class SeedSetGenMain {
    public static void main(String[] args) {
        final boolean isDirected = true;
        final boolean isWeighted = false;


        String inputFile = args[0];
        String outputFile = args[1];
        int numSeeds = Integer.parseInt(args[2]);
        int[] seedSet = new int[numSeeds];

        Graph<PersonalPageRankSharedData> graph = Graph.getInstance(0, isDirected, isWeighted);

        long start = System.currentTimeMillis();
        System.err.println("Graph Loading... ");
        GraphUtil.load(graph, inputFile);
        System.err.println("Loading Time : " + (System.currentTimeMillis() - start) / 1000.0);

        int count = 0;
        while (count < numSeeds) {
            boolean isDuplicate = false;
            Random random = new Random();
            int nodeId = random.nextInt(graph.getMaxNodeId() + 1);

            if (graph.getNode(nodeId) == null) {
                continue;
            }

            for (int i = 0; i <= count; i++) {
                if (seedSet[i] == nodeId) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                seedSet[count] = nodeId;
                System.out.println(nodeId);
                count++;
            }
        }

        Arrays.sort(seedSet);

        try (FileWriter fw = new FileWriter(outputFile, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < numSeeds; i++) {
                out.println(seedSet[i]);
            }
        } catch (IOException e) {

        }
    }
}