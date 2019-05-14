package Building;

import Building.*;

import java.util.Vector;

public class Routes {
    public String RouteName;
    private String routeName;
    private int routeId;
    private Vector<Node> nodes = new Vector<Node>();
    private Vector<Person> assignedPeople = new Vector<Person>();

    public Routes(String s) {
        RouteName = s;
    }

    public int getNumPeople()
    {
        return assignedPeople.size();
    }
    public Vector<Person> listPeople()
    {
        return null;
    }
    public int calculateHeuristic(Node startNode)
    {
        return 0;
    }
    public void addNode(Node n)
    {
        nodes.add(n);
    }

    public double distanceToGoal(Node n)
    {
        double dist = 0;
        Node temp = n;
        int position = nodes.indexOf(n);
        while(position < nodes.size() - 1)
        {
            temp = nodes.get(position);
            dist += temp.distanceToNode(nodes.get(++position));
        }
        return dist;
    }

}
