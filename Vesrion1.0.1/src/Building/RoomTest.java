package Building;

import static Building.RoomType.floor;
import static Building.RoomType.normal;
import static Building.RoomType.hall;

public class RoomTest {


    private static double [][] CorridoorCorners = {
            {20,0}, //First corner
            {25,0},{25,20},{50,20},{50,25},
            {20,25}, // Last corner - Will always connect to first corner
    };
    private static double [][] FloorCorners = {
            {0,0}, //First corner
            {50,0},{0,50},
            {50,50}, // Last corner - Will always connect to first corner
    };

    public static void main(String[] args)
    {
        Building Demo = new Building();
        Room GroundFloor = new Room(floor);
            for (int i = 0; i < FloorCorners.length-1; i++)
                GroundFloor.buildWall(FloorCorners[i],FloorCorners[i+1]);
            GroundFloor.buildWall(FloorCorners[FloorCorners.length-1],FloorCorners[0]);
            System.out.println(GroundFloor.isValidRoom());
            if(GroundFloor.isCyclic())
                Demo.addFloor(GroundFloor);
            else
                return;

        Room corridoor = new Room(hall);
            for (int i = 0; i < CorridoorCorners.length-1; i++)
                corridoor.buildWall(CorridoorCorners[i],CorridoorCorners[i+1]);
            corridoor.buildWall(CorridoorCorners[CorridoorCorners.length-1],CorridoorCorners[0]);
            System.out.println(corridoor.isValidRoom());
            if(corridoor.isCyclic())
                GroundFloor.addRoom(corridoor);
            else
                return;

    }

}
