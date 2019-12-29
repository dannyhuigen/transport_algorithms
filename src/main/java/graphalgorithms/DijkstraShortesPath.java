package main.java.graphalgorithms;

import main.java.model.IndexMinPQ;
import main.java.model.TransportGraph;

public class DijkstraShortesPath extends AbstractPathSearch {
    public DijkstraShortesPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    IndexMinPQ<Integer> priorityQueue = new IndexMinPQ<>(graph.getNumberOfStations());

    public void search(){

        priorityQueue.insert(startIndex, 0);
        int currentIndex = startIndex;
        edgeTo[currentIndex] = -1;

        while (priorityQueue.size() != 0 && currentIndex != endIndex){
            currentIndex = priorityQueue.delMin();
            marked[currentIndex] = true;
//            System.out.println("-->" + graph.getStation(currentIndex) + "(" + travelcost[currentIndex] + ")");
            nodesVisited.add(graph.getStation(currentIndex));

            for (int adjecentIndex : graph.getAdjacentVertices(currentIndex)){
                //als adjecent nog niet bekeken is
                if (!marked[adjecentIndex]){
                    //als adjecent al in de queue zit
                    if (priorityQueue.contains(adjecentIndex)){
                        int travelCostFromStart =  travelcost[currentIndex] + (int) (graph.getConnection(currentIndex,adjecentIndex).getWeight() * 10);
                        if (travelcost[adjecentIndex] > travelCostFromStart){
                            //Als hij goedkoper is dan bekende route verander hem
                            travelcost[adjecentIndex] = travelCostFromStart;
                            edgeTo[adjecentIndex] = currentIndex;
                        }
                    }
                    else{
                        int travelCostFromStart =  travelcost[currentIndex] + (int) (graph.getConnection(currentIndex,adjecentIndex).getWeight() * 10);
//                        System.out.println(graph.getStation(adjecentIndex) + "(" + adjecentIndex + ") " + travelCostFromStart);
                        travelcost[adjecentIndex] = travelCostFromStart;
                        edgeTo[adjecentIndex] = currentIndex;

                        priorityQueue.insert(adjecentIndex, travelCostFromStart);
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
