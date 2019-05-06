package Building;

import static Building.RoomType.floor;
import static Building.RoomType.normal;
import static Building.RoomType.hall;

public class RoomTest {

    private static double [][] CorridoorCorners = {
            {20,0}, //First corner
            {25,0},
            {25,20},
            {50,20},
            {50,25},
            {20,25}, // Last corner
    };

    public static void main(String[] args)
    {
        Building Demo = new Building();

        // Create a graph given in the above diagram
        /*
        Room r1 = new Room(floor);
        Room.Corner c1 = new Room.Corner(0,0);
        Room.Corner c2 = new Room.Corner(0,50);
        r1.addCorner(c1);
        r1.addCorner(c2);
        r1.buildWall(c1,c2);
        Room.Corner c3 = new Room.Corner(50,50);
        Room.Corner c4 = new Room.Corner(50,0);

        r1.addCorner(c3);
        r1.buildWall(c2,c3);
        r1.addCorner(c4);
        r1.buildWall(c3,c4);
        //System.out.println(r1.isValidRoom());
        r1.buildWall(c4,c1);
        System.out.println(r1.isValidRoom());

        if(r1.isCyclic())
            Demo.addFloor(r1);
        else
            return;

        Room Groundfloor = Demo.getFloor(0);
    */
        Room corridoor = new Room(hall);
        for (int i = 0; i < CorridoorCorners.length-1; i++) {
            corridoor.buildWall(CorridoorCorners[i],CorridoorCorners[i+1]);
        }
        corridoor.buildWall(CorridoorCorners[CorridoorCorners.length-1],CorridoorCorners[0]);
        System.out.println(corridoor.isValidRoom());
        /*
        if(corridoor.isCyclic())
            Groundfloor.addRoom(corridoor);
        else
            return;
            */
    }

}
