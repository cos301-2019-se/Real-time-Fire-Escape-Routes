package Building;

import java.util.Vector;

public class Path {
    public Node start;
    public Node end;
    public double Distance;
//    public Vector<Person> assignedPeople = new Vector<Person>();
    Path(Node _start,Node _end, double distance){
        start=_start;
        end=_end;
        Distance=distance;
    }

    public void assignPeople(Person p)
    {
        start.addPerson(p);
    }

    public void removePeople(Person p)
    {
        start.removePerson(p);
    }
    public static boolean hasGoal(Vector<Node> p){
        if(p.lastElement().type == NodeType.buildingExit)
            return true;
        return false;
    }

}
