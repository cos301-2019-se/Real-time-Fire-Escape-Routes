package Building;


import org.json.JSONObject;

import java.util.Vector;

public class Person {

    public static long numPeople = 0;
    public String name;
    Routes AssignedRoute;
    double [] position;
    public int floor = 0;
    public long personID = 0;
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

    public long getPersonID() {
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

    public JSONObject toJSON(){
        JSONObject person = new JSONObject();
        person.put("floor",this.floor);
        person.put("position",this.position);
        person.put("id",this.personID);
        person.put("device_id",this.deviceID);
        return person;
    }

}
