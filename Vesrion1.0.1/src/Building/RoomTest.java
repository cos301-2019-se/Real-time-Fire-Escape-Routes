package Building;

import static Building.RoomType.floor;
import static Building.RoomType.normal;
import static Building.RoomType.hall;

public class RoomTest {

    public static void main(String[] args)
    {
        Building Demo = new Building();

        // Create a graph given in the above diagram
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
        Room corridoor = new Room(hall);
        corridoor.addCorner(new Room.Corner(20,0));
        corridoor.addCorner(new Room.Corner(25,0));
        corridoor.addCorner(new Room.Corner(25,20));
        corridoor.addCorner(new Room.Corner(50,20));
        corridoor.addCorner(new Room.Corner(50,25));
        corridoor.addCorner(new Room.Corner(20,25));
        corridoor.buildWall(corridoor.getCorner(0),corridoor.getCorner(1));
        corridoor.buildWall(corridoor.getCorner(1),corridoor.getCorner(2));
        corridoor.buildWall(corridoor.getCorner(2),corridoor.getCorner(3));
        corridoor.buildWall(corridoor.getCorner(3),corridoor.getCorner(4));
        corridoor.buildWall(corridoor.getCorner(4),corridoor.getCorner(5));
        corridoor.buildWall(corridoor.getCorner(5),corridoor.getCorner(0));
        System.out.println(corridoor.isValidRoom());
        if(corridoor.isCyclic())
            Groundfloor.addRoom(corridoor);
        else
            return;
    }

}
