package Building;
import java.util.Iterator;
import java.util.Vector;

enum RoomType{
    normal,
    floor,
    hall
}

public class Room {

    /**Attributes*/
    RoomType roomType;
    public Vector<Door> doors = new Vector<Door>();
    private Vector<Person> peopleInRoom = new Vector<>();

    public Vector<Room> getRooms() {
        return Rooms;
    }
    public Room getRooms(int i ) {
        return Rooms.get(i);
    }

    public void addRoom(Room room) {
        Rooms.add(room);
    }

    private Vector<Room> Rooms = new Vector<Room>();
    private Vector<Corner> Corners = new Vector<>();
    private Vector<Vector<Corner>> Walls=new Vector<>(); // Adjacency List Represntation

    // Constructor
    Room(RoomType type) {
        roomType = type;
        Walls.add(new Vector<>());
    }
    public String isValidRoom(){
        if(!isCyclic())
            return roomType.name()+ " (room) has an error";
        return roomType.name()+ " (room) is complete";
    }
    void addCorner(Corner c){
        Corners.add(c);
        Walls.add(new Vector<>());
    }
    protected Corner getCorner(int i){
        return Corners.get(i);
    }
    void buildWall(Corner v, Corner w) {
        Walls.get(Corners.indexOf(v)).add(w);
        Walls.get(Corners.indexOf(w)).add(v);
    }
    Boolean isCyclicUtil(Corner v, Boolean visited[], Corner parent) {
        int _v = Corners.indexOf(v);
        visited[_v] = true;
        Corner i;
        Iterator<Corner> it = Walls.get(_v).iterator();
        while (it.hasNext())
        {
            i = it.next();
            if (!visited[Corners.indexOf(i)]) {
                if (isCyclicUtil(i, visited, v))
                    return true;
            }
            else if (i != parent)
                return true;
        }
        return false;
    }
    Boolean isCyclic() {
        Boolean visited[] = new Boolean[Corners.size()];
        for (int i = 0; i < Corners.size(); i++)
            visited[i] = false;
        for (int u = 0; u < Corners.size(); u++)
            if (!visited[u])
                if (isCyclicUtil(Corners.elementAt(u), visited, null))
                    return true;
        return false;
    }



    public static class Corner{
        double x;
        double z;

        Corner(double[] coords){
            x = coords[0];
            z = coords[1];
        }
        Corner(double _x,double _z){
            x = _x;
            z = _z;
        }
        public double[] getCoords(){
            double [] temp = {x,z};
            return temp;
        }
    }
}
