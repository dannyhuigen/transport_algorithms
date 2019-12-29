package main.java.graphalgorithms;

import main.java.model.TransportGraph;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstPath extends AbstractPathSearch {

    Queue<Integer> queue = new LinkedList<Integer>();

    public BreadthFirstPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    public void search(){

        int currentNode = startIndex;
        queue.add(currentNode);
        this.nodesVisited.add(graph.getStation(currentNode));
        marked[currentNode] = true;
        edgeTo[currentNode] = -1;

//        while (!nodesVisited.contains(graph.getStation(endIndex)) || queue.size() != 0){
        while (queue.size() != 0){
            currentNode = queue.remove();

            for (int adjecentNode: graph.getAdjacentVertices(currentNode)){
                if (!nodesVisited.contains(graph.getStation(adjecentNode))){
                    queue.add(adjecentNode);
                    nodesVisited.add(graph.getStation(adjecentNode));
                    marked[adjecentNode] = true;
                    edgeTo[adjecentNode] = currentNode;
                }
            }
        }
        pathTo(endIndex);
    }
}
