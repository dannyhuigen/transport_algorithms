package main.java.graphalgorithms;

import main.java.model.IndexMinPQ;
import main.java.model.TransportGraph;

public class A_Star extends AbstractPathSearch {
    public A_Star(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    private final int TRAVEL_TIME_MULTIPLIER = 300;
    private IndexMinPQ<Integer> priorityQueue = new IndexMinPQ<>(graph.getNumberOfStations());

    public void search(){

        priorityQueue.insert(startIndex, 0);
        int currentIndex = startIndex;
        edgeTo[currentIndex] = -1;

        while (priorityQueue.size() != 0 && currentIndex != endIndex){
            currentIndex = priorityQueue.delMin();
            marked[currentIndex] = true;
            nodesVisited.add(graph.getStation(currentIndex));
            for (int adjecentIndex : graph.getAdjacentVertices(currentIndex)){
                //als adjecent nog niet bekeken is
                if (!marked[adjecentIndex]){
                    //Bereken travelcost vanaf de start indecie
                    int travelCostFromStart =  travelcost[currentIndex] + (int) (graph.getConnection(currentIndex,adjecentIndex).getWeight() * 10);
                    if (!priorityQueue.contains(adjecentIndex)){
                        //als adjecent niet al in de queue zit
                        travelcost[adjecentIndex] = travelCostFromStart;
                        edgeTo[adjecentIndex] = currentIndex;

                        //Bereken de waardes en zet in de priority queue
                        int weightToNextStation = (int) (graph.getConnection(currentIndex,adjecentIndex).getWeight() * 10);
                        int travelTimeToNextStation = (int) (graph.getStation(endIndex).getLocation().travelTime(graph.getStation(adjecentIndex).getLocation()) );
                        priorityQueue.insert(adjecentIndex, travelCostFromStart + weightToNextStation + (travelTimeToNextStation * TRAVEL_TIME_MULTIPLIER)); //Note the multiplier that is changeable
                    }
                    else if (travelcost[adjecentIndex] > travelCostFromStart){
                        //Als hij wel in de queue zit en goedkoper is dan de al bekende route verander hem
                        travelcost[adjecentIndex] = travelCostFromStart;
                        edgeTo[adjecentIndex] = currentIndex;
                    }
                }
            }
        }
        pathTo(endIndex);
    }


    public int getTotalWeight(int stationIndex){
        return travelcost[stationIndex];
    }
}
