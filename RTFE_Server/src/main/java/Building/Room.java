package Building;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Room {

    /**Attributes*/
    RoomType roomType;
    public Vector<Door> doors = new Vector<Door>();
    private Vector<Person> peopleInRoom = new Vector<>();
    private Vector<Room> Rooms = new Vector<Room>();
    private Vector<Corner> Corners = new Vector<>();
    private Vector<Vector<Corner>> Walls=new Vector<>(); // Adjacency List Represntation
    Vector<Node> nodesInRooms = new Vector<Node>();
    static boolean verbose = true;

    // Constructor
    public Room(RoomType type) {
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
    public boolean addDoor(Door d){
        int numDoorsPlaced = 0;
        for (int i = 0; i < Rooms.size(); i++) {
            if(Rooms.get(i).addDoor(d))
                numDoorsPlaced++;
        }
        for (int i = 0; i < Walls.size(); i++) {
            if(onWall(i,d.getCenter())){
                doors.add(d);
                if(verbose)
                    System.out.println("Door Placed in "+roomType.toString());
                return true;
            }
        }
        if(numDoorsPlaced>=1)
            return true;
        return false;
    }

    private boolean onWall(int i, double[] pos) {
        /**
         * @Description: Identify whether or not the pos from the param overlaps with the wall
         *
         * Take note this works but doesnt 'snap' to the diagonal walls so your guessing should be on point
         * consult this link below for some useful math stuff en live triangle demo 0.o (such wow)
         * http://blackpawn.com/texts/pointinpoly
         **/
        //Very specific reason for this no touchy!
        Corner A = Corners.get(i);

        Corner B ;
        if(i==0)
            B = Walls.get(i).get(0);
        else
            B = Walls.get(i).get(1);

        //Line is Vertical
        if(A.x == B.x && A.x == pos[0])
            return (pos[1] < A.z && pos[1] > B.z) || (pos[1] > A.z && pos[1] < B.z);

        //Line is Horizontal
        if(A.z == B.z && A.z == pos[1])
            return (pos[0] < A.x && pos[0] > B.x) || (pos[0] > A.x && pos[0] < B.x);
        
        //Line is diagonal
        return ((A.x <pos[0] && pos[0] <B.x) && (A.z <pos[1] && pos[1] <B.z)) || ((A.x >pos[0] && pos[0] >B.x) && (A.z >pos[1] && pos[1] >B.z));

    }

    public void removePeople(){
        for (int i = 0; i < Rooms.size(); i++) {
            Rooms.get(i).removePeople();
        }
        while(peopleInRoom.size() != 0){
            peopleInRoom.remove(0);
        }
    }

    public double distance(double[] doorCoordinates, double[] doorCoordinates2)
    {
        double total = Math.sqrt(((doorCoordinates[0] - doorCoordinates2[0])*(doorCoordinates[0] - doorCoordinates2[0]))+((doorCoordinates[1] - doorCoordinates2[1])*(doorCoordinates[1] - doorCoordinates2[1])));
        return total;
    }

    public boolean connectDoors()
    {
        boolean connect = true;
        for (int i = 0; i < getRooms().size(); i++) {
            if(getRooms(i).connectDoors())
            {
                return true;
            }

        }
        for(int j = 0; j < doors.size()-1; j++)
        {
            for(int k = j + 1; k < doors.size(); k++)
            {
                for(int i = 0; i < Walls.size(); i++)
                {
                    if(onWall(i, doors.get(j).getCenter()) && onWall(i, doors.get(k).getCenter()))
                    {
                        // DO NOT CONNECT
                        connect = false;
                    }
                }
                if(connect)
                {
                    doors.get(j).node.connect(doors.get(k).node, distance(doors.get(j).getCenter(), doors.get(k).getCenter()));
                    System.out.println("Connecting doors: " + doors.get(j).node.nodeId + " <-> " + doors.get(k).node.nodeId + "  // distance: " + distance(doors.get(j).getCenter(), doors.get(k).getCenter()));
                }
                connect = true;
            }

        }
        return true;
    }

    public Vector<Node> getAllDoors(){
        Vector<Node> currentDoors = new Vector<Node>();
        for (int i = 0; i < Rooms.size(); i++) {
            currentDoors.addAll(Rooms.get(i).getAllDoors());
        }
        for (int i = 0; i < doors.size(); i++) {
            currentDoors.add(doors.get(i).node);
        }
        return currentDoors;
    }

    /**
     * @Tilanie:
     * What is the purpose of this function?
     * */
    public boolean getAllNodes()
    {
        for (int i = 0; i < getRooms().size(); i++) {
            if(getRooms(i).getAllNodes())
                return true;
        }
        for(int i = 0; i < doors.size(); i++)
        {
            if(!nodesInRooms.contains(doors.get(i).node))
            {
                nodesInRooms.add(doors.get(i).node);
            }
        }
        return true;
    }
    public boolean addPerson(Person p){
        for (int i = 0; i <getRooms().size() ; i++) {
            if(getRooms(i).addPerson(p))
                return true;
        }
        int wallOverlaps = 0;
        for (int i = 0; i < Walls.size(); i++) {
            if(crossWall(i,p.getPosition()))
                wallOverlaps++;
        }
        if(verbose)
            System.out.println("Wall overlaps with "+roomType+": "+wallOverlaps);
        if((wallOverlaps >=4)|| wallOverlaps == Walls.size() ){
            peopleInRoom.add(p);
            return true;
        }
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
        System.out.println("People In Room "+roomType.toString() +" is "+peopleInRoom.size());
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
    public void buildWall(double[] corner1, double[] corner2) {

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
        if(Walls.lastElement().size() == 0)
            Walls.remove(Walls.size()-1);
        Boolean visited[] = new Boolean[Corners.size()];
        for (int i = 0; i < Corners.size(); i++)
            visited[i] = false;
        for (int u = 0; u < Corners.size(); u++)
            if (!visited[u])
                if (isCyclicUtil(Corners.elementAt(u), visited, null))
                    return true;
        return false;
    }

    public void assignPeople(Vector<Routes> routes){
        for (int i = 0; i < Rooms.size(); i++) {
            Rooms.get(i).assignPeople(routes);
        }

        for (int i = 0; i < peopleInRoom.size(); i++) {
            double Distance =  Double.POSITIVE_INFINITY;
            Person p = peopleInRoom.get(i);
            int BestRoute = 0;
            int BestDoor = 0;
            for (int j = 0; j < doors.size(); j++) {
                for (int k = 0; k < routes.size(); k++) {
                    double temp = routes.get(k).calculateHeuristic(doors.get(j).node,p);
                    if(temp < Distance){
                        Distance= temp;
                        BestDoor =j;
                        BestRoute = k;
                    }
                }
            }
            if(verbose){
                System.out.println("Person "+p.personID+" is assigned to Route "+BestRoute +" Distance to safety is: "+Distance+ " using door " +doors.get(BestDoor).id);
            }

            p.setAssignedRoute(routes.get(BestRoute));
            routes.get(BestRoute).addPerson(p);
        }
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
