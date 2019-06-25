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
//    public double calculateHeuristic(Node startNode, Person p) throws Exception {
//        try {
//            double Heuristic =0;
//            double distanceToGoal = 0;
//            weight =2.5 ; //tweak
//
//
//            distanceToGoal = startNode.distance(p.position[0], p.position[1]);
//            distanceToGoal += distanceToGoal(startNode);
//            int numPeople = startNode.getNumPeople();
//            double doorResistance = startNode.weight * numPeople;
////            doorResistance *= weight;
//            Heuristic = distanceToGoal + doorResistance;
////            Heuristic += distanceToGoal;
//            Heuristic += assignedPeople.size()*weight;
//            return Heuristic;
//
//        }catch(Exception e) {
//            throw e;
//        }
//
//
//    }
    public void addNode(Node n)
    {
        nodes.add(n);
    }
//
//    public double distanceToGoal(Node n) throws Exception {
//        double dist = 0;
//        Node temp = n;
//        int position = nodes.indexOf(n);
//        if(nodes.contains(n)== false){
//            throw new Exception("Route does not contain Node");
//        }
//        while(position < nodes.size() - 1)
//        {
//            temp = nodes.get(position);
//            if(nodes.get(position).connectedTo.contains(nodes.get(position+1)))
//            dist += temp.distanceToNode(nodes.get(position+1));
//            position++;
//        }
//        return dist;
//    }

//    public double distanceToGoal(Node n) throws Exception {
//        double Distance = 0;
//        Node goal = this.getGoal();
//        /**
//         * Base Case if this node is directly connected to the goal then return the distance to the goal
//         * */
//        if(n.connectedTo.contains(goal)) {
//            Distance = n.distanceToNodes.get(n.connectedTo.indexOf(goal));
//            return Distance;
//        }
//        else{
//            /**
//             * Rooms are nested thus node needs to find a door which is connected to the goal
//             * */
//        }
//        return Distance;
//    }

    public Node getGoal(){
        return nodes.lastElement();
    }

    public void addPerson(Person p) {
        assignedPeople.add(p);
    }

    public Vector<Node> ShortestPathToGoal(Node start, Node end){
        Vector<Node> returnNodePath = new Vector<>();
        Vector<Node> recursiveNodePath = new Vector<>();
        Vector<Node> shortestPath = new Vector<>();
        double SH = Double.MAX_VALUE;
        if(start.nodeId==end.nodeId) {
            returnNodePath.add(end);
        }
        else{
            Node otherNode;
            start.visited=true;
            for (Path Path:start.Paths) {
                if(Path.start.nodeId != start.nodeId)
                    otherNode = Path.start;
                else
                    otherNode = Path.end;
                if(!otherNode.visited){
                    recursiveNodePath = ShortestPathToGoal(otherNode,end);
                    double pathH = pathHeuristic(recursiveNodePath,null);
                    if(recursiveNodePath.contains(end) && pathH < SH) {
                        shortestPath = recursiveNodePath;
                        SH = pathHeuristic(shortestPath,null);
                    }
                }
            }
            returnNodePath.add(start);
            returnNodePath.addAll(shortestPath);
//            printPath(returnNodePath);
        }
        return  returnNodePath;
    }
    public static void printPath(Vector<Node> path, Person p){
        String Path="";
        for (Node v:path) {
            Path += v.nodeId+" ";
        }
        double distance = pathDistance(path,p);
        System.out.println("Path: "+Path + " Distance: " +distance+ " Heuristic: "+pathHeuristic(path,p));
    }

    public static double pathDistance(Vector<Node> path, Person p){
        double d = 0;
        for (int i = 0; i < path.size()-1; i++) {
            Node current = path.get(i);
            Node target = path.get(i+1);
            boolean added =false;
            for (Path Path: current.Paths){
                if(added)
                    break;
                if(Path.end.nodeId == target.nodeId){
                    d+=Path.Distance;
                    added = true;
                }
            }
        }
        return d + path.get(0).distance(p.position[0], p.position[1]);
    }
    public static double pathHeuristic(Vector<Node> path, Person p){
        double h = 0;

        for (int i = 0; i < path.size()-1; i++) {
            Node current = path.get(i);
            Node target = path.get(i+1);
            boolean added =false;
            for (Path Path: current.Paths){
                if(added)
                    break;
                if(Path.end.nodeId == target.nodeId){
                    h += Path.Distance;
                    h *= current.weight;
                    added = true;
                }
            }
        }
        if(p!=null){
            h+= path.get(0).distance(p.position[0], p.position[1]);
        }
        return h;
    }
    public void resetVisited(){
        for (Node v: nodes) {
            v.visited=false;
        }
    }


}
