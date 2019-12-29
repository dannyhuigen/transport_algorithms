package main.java.graphalgorithms;

import main.java.model.Connection;
import main.java.model.Line;
import main.java.model.Station;
import main.java.model.TransportGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class that contains methods and attributes shared by the DepthFirstPath en BreadthFirstPath classes
 */
public abstract class AbstractPathSearch {

    protected boolean[] marked;
    protected int[] edgeTo;
    protected int[] travelcost;
    protected int transfers = 0;
    protected List<Station> nodesVisited;
    protected List<Station> nodesInPath;
    protected LinkedList<Integer> verticesInPath;
    protected TransportGraph graph;
    protected final int startIndex;
    protected final int endIndex;


    public AbstractPathSearch(TransportGraph graph, String start, String end) {
        startIndex = graph.getIndexOfStationByName(start);
        endIndex = graph.getIndexOfStationByName(end);
        this.graph = graph;
        nodesVisited = new ArrayList<>();
        marked = new boolean[graph.getNumberOfStations()];
        for (boolean m : marked) m = false;
        edgeTo = new int[graph.getNumberOfStations()];
        travelcost = new int[graph.getNumberOfStations()];
    }

    public int getAmountOfVerticesVisited(){
        return nodesVisited.size();
    }

    /**
     * @param vertex Determines whether a path exists to the station as an index from the start station
     * @return
     */
    public boolean hasPathTo(int vertex) {
        return marked[vertex];
    }


    /**
     * Method to build the path to the vertex, index of a Station.
     * First the LinkedList verticesInPath, containing the indexes of the stations, should be build, used as a stack
     * Then the list nodesInPath containing the actual stations is build.
     * Also the number of transfers is counted.
     * @param vertex The station (vertex) as an index
     */
    public void pathTo(int vertex) {
        verticesInPath = new LinkedList<>();
        nodesInPath = new LinkedList<>();

        int currentVertex = vertex;
        while (currentVertex != -1){
            //adding first because path is generated from end to start
            verticesInPath.addFirst(currentVertex);
            currentVertex = edgeTo[currentVertex];
        }
        //Looping in different loop so that the list gets reversed
        for (int v : verticesInPath){
            nodesInPath.add(graph.getStation(v));
        }
        countTransfers();
    }

    /**
     * Method to count the number of transfers in a path of vertices.
     * Uses the line information of the connections between stations.
     * If to consecutive connections are on different lines there was a transfer.
     */
    public void countTransfers() {
        //Just 1 node so there no transfers
        if (verticesInPath.size() <= 1){
            return;
        }

        Connection previousConnection = graph.getConnection(verticesInPath.get(0), verticesInPath.get(1));

        for (int i = 0; i < verticesInPath.size() - 1; i++){
            Connection currentConnection = graph.getConnection(verticesInPath.get(i), verticesInPath.get(i+1));

            //Connections are not the same so there's a switch between the lines thus adding one
            if (currentConnection.getLine() != previousConnection.getLine()){
                this.transfers++;
            }
            previousConnection = currentConnection;
        }
    }


    /**
     * Method to print all the nodes that are visited by the search algorithm implemented in one of the subclasses.
     */
    public void printNodesInVisitedOrder() {
        System.out.print("Nodes in visited order: ");
        for (Station vertex : nodesVisited) {
            System.out.print(vertex.getStationName() + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder(String.format("Path from %s to %s: ", graph.getStation(startIndex), graph.getStation(endIndex)));
        resultString.append(nodesInPath).append(" with " + transfers).append(" transfers");
        return resultString.toString();
    }

}
