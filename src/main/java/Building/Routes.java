package Building;

import Building.*;

import java.util.Vector;

public class Routes {
    public String RouteName;
    private String routeName;
    private int routeId;
    private Vector<Node> nodes = new Vector<Node>();
    private Vector<Person> assignedPeople = new Vector<Person>();
    public double weight;

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
    public double calculateHeuristic(Node startNode, Person p)
    {
        weight = 5; //tweak
        double distanceToGoal = startNode.distance(p.position[0], p.position[1]);
        distanceToGoal += distanceToGoal(startNode);
        int numPeople = startNode.getNumPeople();
        double doorResistance = startNode.weight * numPeople;
        doorResistance *= weight;
        double heuristic = distanceToGoal + doorResistance;
        return heuristic;


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
            if(nodes.get(position).connectedTo.contains(nodes.get(position+1)))
            dist += temp.distanceToNode(nodes.get(position+1));
            position++;

        }
        return dist;
    }
    public Node getGoal(){
        return nodes.lastElement();
    }

    public void addPerson(Person p) {
        assignedPeople.add(p);
    }
}
