package main.java.model;

public class Location {

    int x;
    int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Gets estimated traveltime between 2 cords
     * @param to cords to the wanted location
     * @return estimated travel time
     */
    public double travelTime(Location to){
        int xDistance = Math.abs(this.x - to.x);
        int yDistance = Math.abs(this.y - to.y);
        double totalTravelTime = (xDistance + yDistance) * 1.5;

        return totalTravelTime;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
