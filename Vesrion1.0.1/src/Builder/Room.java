package Builder;

import java.util.Vector;

public class Room {
    public Vector<Door> doors = new Vector<Door>();
    private Vector<Person> peopleInRoom = new Vector<>();
    private double [] [] coordinates;
    public Room(double [] [] array)
    {
        coordinates = new double[3][2];
        for(int i = 0; i < 3; i++)
        {
            coordinates[i][0] = array[i][0];
            coordinates[i][1] = array[i][1];
        }
    }

    public void addDoor(Door d)
    {
        doors.add(d);
    }

    public void addPerson(Person p)
    {
        peopleInRoom.add(p);
    }

    public Vector<Integer> assignPeople()
    {
        Vector<Integer> asignedTo = new Vector<>();
        for(int i = 0; i < peopleInRoom.size(); i++)
        {
            asignedTo.add(getClosestDoorIndex(i));
        }
        return asignedTo;
    }
    public int getClosestDoorIndex(int personIndex)
    {
        double total;
        double smallest = 999999999;
        int doorIndex = 0;
        double [] doorCoordinates;
        for(int i = 0; i < doors.size(); i++)
        {
            doorCoordinates= doors.get(i).getCenter();
            total = Math.sqrt(((doorCoordinates[0] - peopleInRoom.get(personIndex).position[0])*(doorCoordinates[0] - peopleInRoom.get(personIndex).position[0]))+((doorCoordinates[1] - peopleInRoom.get(personIndex).position[1])*(doorCoordinates[1] - peopleInRoom.get(personIndex).position[1])));
            if(total < smallest)
            {
                smallest = total;
                doorIndex = i;
            }
        }
        return doorIndex;
    }
}
