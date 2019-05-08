package Building;


public class Person {

    static int numPeople = 0;
    public String name;
    Routes AssignedRoute;
    double [] position;
    int personID= 0;

    public Person(String n) {
        name = n;
        personID =numPeople++;

    }
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
