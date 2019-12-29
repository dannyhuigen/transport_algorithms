package main.java.model;

import java.util.*;

public class TransportGraph {

    private int numberOfStations;
    private int numberOfConnections;
    private List<Station> stationList;
    private Map<String, Integer> stationIndices;
    private List<Integer>[] adjacencyLists;
    private Connection[][] connections;

    public TransportGraph (int size) {
        this.numberOfStations = size;
        stationList = new ArrayList<>(size);
        stationIndices = new HashMap<>();
        connections = new Connection[size][size];
        adjacencyLists = (List<Integer>[]) new List[size];
        for (int vertex = 0; vertex < size; vertex++) {
            adjacencyLists[vertex] = new LinkedList<>();
        }
    }

    public int getIndexOfStationByName(String stationName) {
        return stationIndices.get(stationName);
    }


    /**
     * @param vertex Station to be added to the stationList
     * The method also adds the station with it's index to the map stationIndices
     */
    public void addVertex(Station vertex) {
        stationList.add(vertex);
        stationIndices.put(vertex.getStationName(), stationIndices.size());
        numberOfStations = stationList.size();
    }


    /**
     * Method to add an edge to a adjancencyList. The indexes of the vertices are used to define the edge.
     * Method also increments the number of edges, that is number of Connections.
     * The grap is bidirected, so edge(to, from) should also be added.
     * @param from
     * @param to
     */
    private void addEdge(int from, int to) {
        adjacencyLists[from].add(to);
    }


    /**
     * Method to add an edge in the form of a connection between stations.
     * The method also adds the edge as an edge of indices by calling addEdge(int from, int to).
     * The method adds the connecion to the connections 2D-array.
     * The method also builds the reverse connection, Connection(To, From) and adds this to the connections 2D-array.
     * @param connection The edge as a connection between stations
     */
    public void addEdge(Connection connection) {
        int fromIndex = this.stationIndices.get(connection.getFrom().getStationName());
        int toIndex = this.stationIndices.get(connection.getTo().getStationName());

        addEdge(fromIndex, toIndex);
        addEdge(toIndex, fromIndex);

        this.connections[fromIndex][toIndex] = connection;
        this.connections[toIndex][fromIndex] = connection;

        numberOfConnections = 0;
        for (List list : adjacencyLists){
            numberOfConnections+= list.size();
        }
        numberOfConnections= numberOfConnections / 2;
    }

    public List<Integer> getAdjacentVertices(int index) {
        return adjacencyLists[index];
    }

    public Connection getConnection(int from, int to) {
        return connections[from][to];
    }



    public Station getStation(int index) {
        return stationList.get(index);
    }

    public int getNumberOfStations() {
        return numberOfStations;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public int getNumberEdges() {
        return numberOfConnections;
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(String.format("\nGraph with %d vertices and %d edges: \n\n", numberOfStations, numberOfConnections));

        resultString.append(String.format("%-20s%-15s%-20s", "StationName", "Cordinates", "Adjecent Stations and their travel-weights\n"));
        resultString.append("\n");

        for (int indexVertex = 0; indexVertex < numberOfStations; indexVertex++) {

            resultString.append(String.format("%-20s", stationList.get(indexVertex)));
            resultString.append(String.format("%-15s", stationList.get(indexVertex).getLocation().toString()));

            int loopsize = adjacencyLists[indexVertex].size() - 1;
            for (int indexAdjacent = 0; indexAdjacent < loopsize; indexAdjacent++) {
                resultString.append(String.format(
                    "%-24s",
                    stationList.get(adjacencyLists[indexVertex].get(indexAdjacent)).getStationName() + "(" +
                    this.getConnection(indexVertex, adjacencyLists[indexVertex].get(indexAdjacent)).getWeight() + ")"
                ));



            }
            resultString.append(String.format(
                    "%-24s",
                    stationList.get(adjacencyLists[indexVertex].get(loopsize)).getStationName() + "(" +
                            this.getConnection(indexVertex , adjacencyLists[indexVertex].get(loopsize)).getWeight() + ")"
            ));
            resultString.append("\n");
//            resultString.append(stationList.get(adjacencyLists[indexVertex].get(loopsize)).getStationName() + "\n");
        }

        return resultString.toString();
//        return "";
    }

    public void printMap(){
        System.out.println("o--- --- ---o   The station map   o--- --- ---o");
        String[][] map = new String[15][15];
        for (String[] strings : map) {
            Arrays.fill(strings, "   ");
        }

        for (Station s : this.stationList){
            map[s.getLocation().getX()][s.getLocation().getY()] = " " + Character.toString(s.getStationName().charAt(0)) + " ";
        }

        for (String[] sa: map){
            StringBuilder line = new StringBuilder();
            for (String s : sa){
                line.append(s);
            }
            System.out.println(line);
        }
        System.out.println("\n");
    }


    /**
     * A Builder helper class to build a TransportGraph by adding lines and building sets of stations and connections from these lines.
     * Then build the graph from these sets.
     */
    public static class Builder {

        private Set<Station> stationSet;
        private List<Line> lineList;
        private Set<Connection> connectionSet;
        private List<Double> weightList;
        private List<Location> locationList;
        private int locationIndex = 0;


        public Builder() {
            lineList = new ArrayList<>();
            stationSet = new HashSet<>();
            connectionSet = new HashSet<>();
            weightList = new ArrayList<>();
            locationList = new ArrayList<>();
        }

        /**
         * Method to add a line to the list of lines and add stations to the line.
         * @param lineDefinition String array that defines the line. The array should start with the name of the line,
         *                       followed by the type of the line and the stations on the line in order.
         * @return
         */
        public Builder addLine(String[] lineDefinition) {

            //Creating line 0 is name and 1 is linetype
            Line lineToAdd = new Line(lineDefinition[0], lineDefinition[1]);

            //Adding vertecies that (starts at index 2)
            for (int i = 2; i < lineDefinition.length ; i++) {
                lineToAdd.addStation(new Station(lineDefinition[i].replace(" ", "_")));
                locationIndex++;
            }

            lineList.add(lineToAdd);

            return this;
        }

        /**
         * Method to add a line to the list of lines and add stations to the line.
         * @param lineDefinition String array that defines the line. The array should start with the name of the line,
         *                       followed by the type of the line and the stations on the line in order.
         * @return
         */
        public Builder addLine(String[] lineDefinition, int[][] stationCords) {

            //Creating line 0 is name and 1 is linetype
            Line lineToAdd = new Line(lineDefinition[0], lineDefinition[1]);

            //Adding vertecies that (starts at index 2)
            for (int i = 2; i < lineDefinition.length ; i++) {
                lineToAdd.addStation(new Station(lineDefinition[i].replace(" ", "_"), new Location(stationCords[i-2][0], stationCords[i-2][1])));
            }

            lineList.add(lineToAdd);

            return this;
        }


        /**
         * Method that reads all the lines and their stations to build a set of stations.
         * Stations that are on more than one line will only appear once in the set.
         * @return
         */
        public Builder buildStationSet() {
            //Loopt door elke line
            for (Line line: lineList){
                //Adds all stations from specific line
                stationSet.addAll(line.getStationsOnLine());
            }
            return this;
        }

        /**
         * For every station on the set of station add the lines of that station to the lineList in the station
         * @return
         */
        public Builder addLinesToStations() {

            //loop door stations
            for(Station station: stationSet){

                //loop door alle lines
                for (Line line: lineList){

                    //loop door alle stations in die line
                    for (Station stationOnLine: line.getStationsOnLine()){
                        //Als station in de line aangrenst tot het orginele station is er een verbinding met die lijn
                        if (stationOnLine.getStationName().equals(station.getStationName())){
                            //Voeg de lijn toe aan de station
                            station.addLine(line);
                        }
                    }
                }
            }
            return this;
        }

        /**
         *adds weight to a line
         * @param weightList the weigths in order of stations in line
         * @return
         */
        public Builder addWeights(Double[] weightList){
            for (double weight: weightList){
                this.weightList.add(weight);
            }
            return this;
        }

        /**
         * Adding cords to cord list
         * @param cords the cords to add
         * @return same instance builder
         */
        public Builder addCords(int[][] cords){
            for (int[] cord : cords){
                locationList.add(new Location(cord[0], cord[1]));
            }
            return this;
        }

        /**
         * Method that uses the list of Lines to build connections from the consecutive stations in the stationList of a line.
         * @return
         */
        public Builder buildConnections() {
            int weightindex = 0;
            for(Line line: lineList){
                for (int i = 0; i < line.getStationsOnLine().size() ; i++) {
                    if (i+1 < line.getStationsOnLine().size()){
                        connectionSet.add(new Connection(line.getStationsOnLine().get(i), line.getStationsOnLine().get(i+1),weightList.get(weightindex),  line));
                        weightindex++;
                    }
                }
            }
            return this;
        }

        /**
         * Method that builds the graph.
         * All stations of the stationSet are addes as vertices to the graph.
         * All connections of the connectionSet are addes as edges to the graph.
         * @return
         */
        public TransportGraph build() {
            TransportGraph graph = new TransportGraph(stationSet.size());

            for (Station station: stationSet){
                graph.addVertex(station);
            }

            for (Connection connection : connectionSet){
                graph.addEdge(connection);
            }

            return graph;
        }

    }
}
