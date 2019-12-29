package main.java.controller;

import main.java.graphalgorithms.A_Star;
import main.java.graphalgorithms.BreadthFirstPath;
import main.java.graphalgorithms.DepthFirstPath;
import main.java.graphalgorithms.DijkstraShortesPath;
import main.java.model.Connection;
import main.java.model.Station;
import main.java.model.TransportGraph;

public class TransportGraphLauncher {

    public static void main(String[] args) {
//        String[] redLine = {"metro", "red", "A", "B", "C", "D"};
//        String[] blueLine = {"metro", "blue", "E", "B", "F", "G"};
//        String[] greenLine = {"metro", "green", "H", "I", "C", "G", "J"};
//        String[] yellowLine = {"bus", "yellow", "A", "E", "H", "D", "G", "A"};

        String[] line1 = {"metro", "red", "Haven", "Marken", "Steigerplein", "Centrum", "Meridiaan", "Dukdalf", "Oostvaarders"};
        Double[] line1Weights = {4.5, 4.7, 6.1, 3.5, 5.4, 5.6};
        int[][] line1Cords = {{14,1}, {12,3}, {10,5}, {8,8},{6,9}, {3,10}, {0,11}};

        String[] line2 = {"metro", "blue", "Trojelaan", "Coltrane Cirkel", "Meridiaan", "Robijnpark", "Violetplantsoen"};
        Double[] line2Weights = {6.0, 5.3, 5.1, 3.3};
        int[][] line2Cords = {{9,3}, {7,6}, {6,9},{5,1}, {5,14}};

        String[] line3 = {"metro", "purple", "Grote Sluis", "Grootzeil", "Coltrane Cirkel", "Centrum", "Swingstraat"};
        Double[] line3Weights = {6.2, 5.2, 3.8, 3.6};
        int[][] line3Cords = {{2,3}, {4,6}, {7,6}, {8,8}, {10,9}};

        String[] line4 = {"metro", "green", "Ymeerdijk", "Trojelaan", "Steigerplein", "Swingstraat", "Bachgracht", "Nobelplein"};
        Double[] line4Weights = {5.0, 3.7, 6.9, 3.9, 3.4};
        int[][] line4Cords = {{9,0}, {9,3}, {10,5}, {10,9}, {11,11}, {12,13}};

        String[] line5 = {"bus", "yellow", "Grote Sluis", "Ymeerdijk", "Haven", "Nobelplein", "Violetplantsoen", "Oostvaarders", "Grote Sluis"};
        Double[] line5Weights = {26.0, 19.0, 37.0, 25.0, 22.0, 28.0};
        int[][] line5Cords = {{2,3}, {9,0}, {14,1}, {12,3}, {5,14}, {0,11}, {2,3}};

        TransportGraph.Builder builder = new TransportGraph.Builder();

        builder.addLine(line1, line1Cords).addLine(line2, line2Cords).addLine(line3, line3Cords).addLine(line4, line4Cords).addLine(line5, line5Cords);
        builder.addWeights(line1Weights).addWeights(line2Weights).addWeights(line3Weights).addWeights(line4Weights).addWeights(line5Weights);

        builder.buildStationSet();
        builder.addLinesToStations();
        builder.buildConnections();

        TransportGraph transportGraph = builder.build();
        System.out.println(transportGraph);
        transportGraph.printMap();

        String start = "Steigerplein";
        String end = "Oostvaarders";

        System.out.println("\ndepth first search:");
//        DepthFirstPath dfpTest =  new DepthFirstPath(transportGraph, "E", "J");
        DepthFirstPath dfpTest =  new DepthFirstPath(transportGraph, start, end);
        dfpTest.search();
        System.out.println(dfpTest);
        dfpTest.printNodesInVisitedOrder();

        System.out.println("\nbreadth first search:");
//        Uncommented to test the BreadthFirstPath algorithm
        BreadthFirstPath bfsTest = new BreadthFirstPath(transportGraph, start, end);
        bfsTest.search();
        bfsTest.printNodesInVisitedOrder();
        System.out.println(bfsTest);

        System.out.println("\ndijkstra's:");
        DijkstraShortesPath dspTest = new DijkstraShortesPath(transportGraph, start, end);
        dspTest.search();
        dspTest.printNodesInVisitedOrder();
        System.out.println(dspTest);
        System.out.println(dspTest.getAmountOfVerticesVisited());

        System.out.println("\nA*:");
        A_Star aStarTest = new A_Star(transportGraph, start, end);
        aStarTest.search();
        aStarTest.printNodesInVisitedOrder();
        System.out.println(aStarTest);
        System.out.println(aStarTest.getAmountOfVerticesVisited());


        int i = 0;
        int[] dijkstraTVF = new int[transportGraph.getStationList().size()* transportGraph.getStationList().size()];
        int[] aStarTVF = new int[transportGraph.getStationList().size()* transportGraph.getStationList().size()];
        boolean[][] alreadVisited = new boolean[transportGraph.getStationList().size()][transportGraph.getStationList().size()];

        StringBuilder resultString = new StringBuilder();

        for (Station currentCheck : transportGraph.getStationList()){
            resultString.append(String.format("%-25s", currentCheck + " ->"));
            for (Station s : transportGraph.getStationList()){

                String start_ = currentCheck.getStationName();
                String end_ = s.getStationName();

                //Check alleen als de 2 stations nog niet met elkaar zijn vergeleken
                if (!alreadVisited[transportGraph.getIndexOfStationByName(s.getStationName())][transportGraph.getIndexOfStationByName(currentCheck.getStationName())]){

                    DijkstraShortesPath dijkstras = new DijkstraShortesPath(transportGraph, start_, end_);
                    dijkstras.search();

                    A_Star aStar_ = new A_Star(transportGraph, start_, end_);
                    aStar_.search();

                    dijkstraTVF[i] = dijkstras.getAmountOfVerticesVisited();
                    aStarTVF[i] = aStar_.getAmountOfVerticesVisited();

                    if (aStar_.getAmountOfVerticesVisited() > dijkstras.getAmountOfVerticesVisited()){
                        System.out.println("dijkstra's: " + dijkstras.getAmountOfVerticesVisited() + " --- a*: " + aStar_.getAmountOfVerticesVisited());
                    }


                    resultString.append(String.format("%-20s", end_));

                    alreadVisited[transportGraph.getIndexOfStationByName(start_)][transportGraph.getIndexOfStationByName(end_)] = true;
                    alreadVisited[transportGraph.getIndexOfStationByName(end_)][transportGraph.getIndexOfStationByName(start_)] = true;
                }
                i++;
            }
            resultString.append("\n");
        }

        System.out.println(resultString);
        int totalDijkstraTVF = 0;
        int totalAStarTVF = 0;

        for (int j = 0; j < dijkstraTVF.length ; j++) {
            totalDijkstraTVF += dijkstraTVF[j];
            totalAStarTVF += aStarTVF[j];
        }

        System.out.println("Total verticies visited dijkstra's: " + totalDijkstraTVF);
        System.out.println("Total verticies visited A*: " + totalAStarTVF);
        System.out.println(String.format("%s%.2f%s", "Percentage of nodes A* visited compared to dijkstra's: " , (((double)totalAStarTVF / (double) totalDijkstraTVF) * 100) , "%"));
        System.out.println(String.format("%s%.2f%s", "This means A* is on average " , (100 - (((double)totalAStarTVF / (double) totalDijkstraTVF) * 100)) , "% faster than dijkstra's on the tested graph"));

    }
}
