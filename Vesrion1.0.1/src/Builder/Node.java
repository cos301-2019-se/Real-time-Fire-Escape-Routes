package Builder;

import java.util.Vector;

public class Node {

    static int numNodes = 0;

    String type;
    int nodeId;
    int weight;
    Vector<Double> distanceToNodes = new Vector();
    Vector<Node> connectedTo = new Vector();
    Vector<Person> assignedPersons = new Vector();

    Node(String Type, int Weight){
        nodeId = numNodes++;
        type= Type;
        weight = Weight;
    }
    Node(int Weight){
        nodeId = numNodes++;
        type= "node";
        weight = Weight;
    }

    public Vector<Person> listPeople()//Done
    {
        return assignedPersons;
    }
    public int getNumPeople()//Done
    {
        return assignedPersons.size();
    }
    void connect(Node n,double distance){// Done
        connectedTo.add(n);
        distanceToNodes.add(distance);
    }

    public void addPerson(Person p) {//Done
        assignedPersons.add(p);
    }
    public Person removePerson(Person p) { //Done
        Person temp= null;
        for (int i=0;i<assignedPersons.size();i++){
            if(assignedPersons.get(i) == p){
                temp= assignedPersons.remove(i);
                break;
            }
        }
        return temp;
    }

    public int getNodeId() {
        return nodeId;
    }
    public double distanceToNode(Node n)
    {
        int index = connectedTo.indexOf(n);
        return distanceToNodes.get(index);
    }
    public String getType() {
        return type;
    }
    public double distanceToGoal(Node start,double d)
    {
        /*
        double distance = d;
        double TempDistance =99999999 ;
        for(int i = 0; i < nodes.size(); i++)
        {

            double newDistance =(nodes.get(i).distanceToGoal(nodes.get(i), nodes.get(i).distanceToNode(i)+distance));
            if(newDistance < TempDistance)
                TempDistance = newDistance;
        }
        return  TempDistance;
        */

        return 0.0;
    }
}
