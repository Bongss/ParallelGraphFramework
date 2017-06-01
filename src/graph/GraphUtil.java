package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GraphUtil {
    public static Graph load(Graph graph, String inputFile) {
        Path path = Paths.get(inputFile);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            readEdgeFromFile(graph, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    static void readEdgeFromFile(Graph graph, BufferedReader reader)
            throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] nodeId = line.trim().split("\t");
            int srcNodeId = Integer.parseInt(nodeId[0]);
            int destNodeId = Integer.parseInt(nodeId[1]);

            if (srcNodeId == destNodeId) {      // remove selfEdge count
                continue;
            }

            if (graph.isWeighted()) {
                int weight = Integer.parseInt(nodeId[2]);
                graph.addEdge(srcNodeId, destNodeId, weight);
            } else {
                graph.addEdge(srcNodeId, destNodeId);
            }
        }
    }

    public static void writeGraph(Graph graph, String fileName) {
        try (FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {

            int maxNodeId = graph.getMaxNodeId();
            for (int i = 0; i <= maxNodeId; i++) {
                Node node = graph.getNode(i);
                int neighborListSize = node.neighborListSize();
                for (int j = 0; j < neighborListSize; j++) {
                    int neighborId = node.getNeighbor(j);
                    out.println(i + "\t" + neighborId);
                }
            }
        } catch (IOException e) {

        }
    }
}
