package Building;
import java.util.Iterator;
import java.util.List;
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


    private Vector<Room> Rooms = new Vector<Room>();
    private Vector<Corner> Corners = new Vector<>();
    private Vector<Vector<Corner>> Walls=new Vector<>(); // Adjacency List Represntation

    // Constructor
    Room(RoomType type) {
        roomType = type;
        Walls.add(new Vector<>());
    }
    /**Self Explanatory*/
    public Vector<Room> getRooms() {
        return Rooms;
    }
    /**Self Explanatory*/
    public Room getRooms(int i ) {
        return Rooms.get(i);
    }
    /**Self Explanatory*/
    public void addRoom(Room room) {
        Rooms.add(room);
    }

    public boolean addPerson(Person p){
        for (int i = 0; i < getRooms().size(); i++) {
            if(getRooms(i).addPerson(p))
                return true;
        }
        /** !REPLACE THIS - Pseudo code for adding a person
         *  DO some wall detection stuff if 4 or more walls matches the coords
         *  if(wallOverlaps >=4 ){
         *      peopleInRoom.add(p)
         *      return true;
         * }
         * */
        return false;
    }

    public int getNumPeople(){
        int total = 0;
        for (int i = 0; i < getRooms().size(); i++) {
            total += getRooms(i).getNumPeople();
        }
        total += peopleInRoom.size();
        return total;
    }

    public Vector<Person> getPeople(){
        Vector <Person> ListOfPeople = new Vector<Person>();
        for (int i = 0; i < getRooms().size(); i++) {
            ListOfPeople.addAll(getRooms(i).getPeople());
        }
        ListOfPeople.addAll(peopleInRoom);
        return ListOfPeople;
    }
    /**
     * @Description: Returns a string specifying the type of room and if it is valid
     * */
    public String isValidRoom(){
        if(!isCyclic())
            return roomType.name()+ " (room) has an error";
        return roomType.name()+ " (room) is complete";
    }
    /**
     * @Description: Adds a corner to the room, Take Note it means nothing on its own
     * */
    protected void addCorner(Corner c){
        Corners.add(c);
        Walls.add(new Vector<>());
    }

    protected Corner getCorner(int i){
        return Corners.get(i);
    }

    public boolean crossWall(int i, double [] coords)
    {
        boolean inRange = false;
        Vector<Corner> wall = Walls.get(i);
        double [] point1 = wall.get(0).getCoords();
        double [] point2 = wall.get(1).getCoords();
        double largeX = Math.max(point1[0], point2[0]);
        double largeZ = Math.max(point1[1], point2[1]);
        double smallX = Math.min(point1[0], point2[0]);
        double smallZ = Math.min(point1[1], point2[1]);

        if(coords[0] < largeX && coords[0] > smallX)
        {
            inRange = true;
        }
        if(coords[1] < largeZ && coords[1] > smallZ)
        {
            inRange = true;
        }
        return inRange;
    }
    /**
     * Connects 2 corners and adds them into the wall array
     * You can call this function with corners not previously added since it will add it automatically
     * */
    void buildWall(Corner v, Corner w) {
        int i= Corners.indexOf(v);      //  This part prevents
        if(i== -1)                      //  errors by making sure
            addCorner(v);               //  the corner exists in
        int j = Corners.indexOf(w);     //  the room
        if(j== -1)                      //
            addCorner(w);               //

        Walls.get(Corners.indexOf(v)).add(w);
        Walls.get(Corners.indexOf(w)).add(v);
    }
    /**
     * Just a simple way of building Walls
     * */
    void buildWall(double [] corner1, double[] corner2 ) {

        Corner a = null;
        Corner b = null;
        for (int i = 0; i < Corners.size(); i++) {
            double [] current =Corners.get(i).getCoords();
            if(current[0] == corner1[0] && current[1] == corner1[1])
                a= Corners.get(i);
            if(current[0] == corner2[0] && current[1] == corner2[1])
                b= Corners.get(i);
        }
        if(a==null)
            a = new Corner(corner1);
        if(b==null)
            b = new Corner(corner2);
        buildWall(a,b);
    }

    /**
     * @info : See the function "isCyclic()" for more details
     * */
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
    /**
     * @return :
     *  true - Returns true if it contains a cycle
     *  false - Returns false if it still needs walls
     * */
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
