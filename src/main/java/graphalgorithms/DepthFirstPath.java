package main.java.graphalgorithms;

import main.java.model.TransportGraph;
import java.util.LinkedList;
import java.util.Queue;

public class DepthFirstPath extends AbstractPathSearch {

    Queue<Integer> queue = new LinkedList<Integer>();

    public DepthFirstPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    public void search(){
        dfsSearch(startIndex);
        edgeTo[startIndex] = -1;
        pathTo(endIndex);
    }

    public void dfsSearch(int currentNode){
        if (marked[currentNode]){
            return;
        }
        marked[currentNode] = true;
        nodesVisited.add(graph.getStation(currentNode));

        for(int adjecentNode : graph.getAdjacentVertices(currentNode)){
            if (!marked[adjecentNode]) {
                edgeTo[adjecentNode] = currentNode;
                dfsSearch(adjecentNode);
            }
        }
    }
}
