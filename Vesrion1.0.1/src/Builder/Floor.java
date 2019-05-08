package Builder;

import java.util.Vector;

public class Floor {
    static int idNum = 0;
    protected Vector<Node> nodes= new Vector<>();
    private Vector<Floor> ConnectedTo= new Vector<>();
    private Vector<Room> rooms= new Vector<>();
    private Vector<Routes> escapeRoutes= new Vector<>();
    private int id;
    public Vector<Room> getRooms() {
        return rooms;
    }
    public void addRoom(Room r) {
        rooms.add(r);
    }
    public Floor() {
        id= idNum++;
    }

    public int getNumPeople()
    {
        return 1;
    }

    public Vector<Person> ListPeople()
    {
        return null;
    }

    public Node getNode(int id)
    {
        for (Node n :nodes) {
            if(n.nodeId == id)
                return n;
        }
        return null;
    }
    public void addNode(Node n){
        nodes.add(n);
    }
}
