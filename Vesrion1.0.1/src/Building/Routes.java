package Building;

import Builder.Building;
import Builder.Node;
import Builder.Person;

import java.util.Vector;

public class Routes {
    public String RouteName;
    private String routeName;
    private int routeId;
    private Vector<Builder.Node> nodes = new Vector<Builder.Node>();
    private Vector<Builder.Person> assignedPeople = new Vector<Builder.Person>();

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
    public int calculateHeuristic(Builder.Node startNode)
    {
        return 0;
    }
    public void addNode(Builder.Node n)
    {
        nodes.add(n);
    }

    public double distanceToGoal(Builder.Node n)
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
