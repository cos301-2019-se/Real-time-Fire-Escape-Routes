package Building;


import java.util.Vector;

public class Person {

    public static int numPeople = 0;
    public String name;
    Routes AssignedRoute;
    double [] position;
    public int floor = 0;
    int personID = 0;
    public String deviceID = "null";
    public Vector<Door> availableDoors = new Vector<>();
    public Vector <Node> pathToFollow;
    public double distanceToExit = Double.MAX_VALUE;

    public Person(String n,double [] pos) {
        name = n;
        personID =numPeople++;
        setPosition(pos);
    }

    public void setPosition(double[] p) {
       position = new double [2];
       position[0] = p[0];
       position[1] = p[1];
    }


    public void setAssignedRoute(Routes assignedRoute) {
        AssignedRoute = assignedRoute;
    }

    public int getPersonID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public double[] getPosition() {
        return position;
    }

    public Routes getAssignedRoute() {
        return AssignedRoute;
    }


}
