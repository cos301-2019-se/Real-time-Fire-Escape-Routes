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
    public double calculateHeuristic(Node startNode, Person p) throws Exception {
        try {
            weight = 500; //tweak
            double distanceToGoal = startNode.distance(p.position[0], p.position[1]);
            distanceToGoal += distanceToGoal(startNode);
            int numPeople = startNode.getNumPeople();
            double doorResistance = startNode.weight * numPeople;
            doorResistance *= weight;
            double heuristic = distanceToGoal + doorResistance;
            heuristic *= (1+this.assignedPeople.size()*0.5);
            return heuristic;
        }catch(Exception e) {
            throw e;
        }


    }
    public void addNode(Node n)
    {
        nodes.add(n);
    }

    public double distanceToGoal(Node n) throws Exception {
        double dist = 0;
        Node temp = n;
        int position = nodes.indexOf(n);
        if(nodes.contains(n)== false){
            throw new Exception("Route does not contain Node");
        }
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
