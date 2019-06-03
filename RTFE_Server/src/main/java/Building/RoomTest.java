package Building;
import static Building.NodeType.buildingExit;
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
    private static double [][] TriangleCorners = {
            {0,50}, //First corner
            {10,50},
            {0,40}, // Last corner - Will always connect to first corner
    };
    private static double [][] test1 = {
            {25,0}, //First corner
            {50,0},
            {50,20},{25,20}, // Last corner - Will always connect to first corner
    };
    private static double [][] doorPositions = {
            {22.5,0},
            {50, 22.5},
            {40,20}
    };


    public static void main(String[] args)
    {
        /*
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

        Room triangle = new Room(normal);
        for (int i = 0; i < TriangleCorners.length-1; i++)
            triangle.buildWall(TriangleCorners[i],TriangleCorners[i+1]);
        triangle.buildWall(TriangleCorners[TriangleCorners.length-1],TriangleCorners[0]);
        System.out.println(triangle.isValidRoom());
        if(triangle.isCyclic())
            GroundFloor.addRoom(triangle);
        else
            return;

        Room test2 = new Room(normal);
        for (int i = 0; i < test1.length-1; i++)
            test2.buildWall(test1[i],test1[i+1]);
        test2.buildWall(test1[test1.length-1],test1[0]);
        System.out.println(test2.isValidRoom());
        if(test2.isCyclic())
            GroundFloor.addRoom(test2);
        else
            return;

        double [] pos = {22.5,22.5};
        Person test = new Person("",pos);
        System.out.println("Placing person - "+Demo.addPerson(test));
        pos[0] = 18;
        pos[1] = 30;
        test = new Person("",pos);
        System.out.println("Placing person - "+Demo.addPerson(test));
        pos[0] = 5;
        pos[1] = 49;
        test = new Person("",pos);
        System.out.println("Placing person - "+Demo.addPerson(test));
        System.out.println("number of people found:" + Demo.getNumPeople());
        System.out.println("==================Placing Doors=================");
            System.out.println("Door "+0+" "+GroundFloor.addDoor(new Door(buildingExit,doorPositions[0])));
        System.out.println("Door "+0+" "+GroundFloor.addDoor(new Door(buildingExit,doorPositions[1])));
        System.out.println("Door "+0+" "+GroundFloor.addDoor(new Door(buildingExit,doorPositions[2])));
        System.out.println();
        System.out.println("==================Connecting Doors=================");
        System.out.println(Demo.connectDoors());
        System.out.println();
        for(int i = 0; i < Demo.getFloor(0).doors.size(); i++)
        {
            System.out.println("=================");
            for(int j = 0; j < Demo.getFloor(0).doors.get(i).node.connectedTo.size(); j++)
            {
                System.out.print(Demo.getFloor(0).doors.get(i).node.nodeId + " <-> " +  Demo.getFloor(0).doors.get(i).node.connectedTo.get(j).nodeId + "\n");
            }

        }
        */
    }
}
