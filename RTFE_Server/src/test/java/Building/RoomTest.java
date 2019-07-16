package Building;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class RoomTest {
        Room test;
    @Before
    public void setUp() throws Exception {
        test = new Room(RoomType.normal);
        Room.Corner one = new Room.Corner(0,0);
        Room.Corner two = new Room.Corner(10,0);
        Room.Corner three = new Room.Corner(10,10);
        Room.Corner four = new Room.Corner(0,10);
        test.buildWall(one,two);
        test.buildWall(two,three);
        test.buildWall(three,four);
        test.buildWall(four,one);
        if(test.isValidRoom().equals(" (room) has an error")){
            throw new Exception("Something went wrong creating a room");
        }
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }

    @Test
    public void getRooms() {

        int CurrentSize = test.getRooms().size();
        Assert.assertEquals(CurrentSize,0);
            Room r = new Room(RoomType.normal);
            Room.Corner one = new Room.Corner(0,0);
            Room.Corner two = new Room.Corner(10,0);
            Room.Corner three = new Room.Corner(10,10);
            Room.Corner four = new Room.Corner(0,10);
            r.buildWall(one,two);
            r.buildWall(two,three);
            r.buildWall(three,four);
            r.buildWall(four,one);
            r.isValidRoom();
            test.addRoom(r);
        CurrentSize = test.getRooms().size();
        Assert.assertEquals(CurrentSize,1);
    }

    @Test
    public void getRooms1() {

        Room r = new Room(RoomType.normal);
        Room.Corner one = new Room.Corner(0,0);
        Room.Corner two = new Room.Corner(10,0);
        Room.Corner three = new Room.Corner(10,10);
        Room.Corner four = new Room.Corner(0,10);
        r.buildWall(one,two);
        r.buildWall(two,three);
        r.buildWall(three,four);
        r.buildWall(four,one);
        r.isValidRoom();
        test.addRoom(r);
        Room Current = test.getRooms(0);
        Assert.assertEquals(Current,r);
    }

    @Test
    public void addRoom() {
        Room r = new Room(RoomType.normal);
        Room.Corner one = new Room.Corner(0,0);
        Room.Corner two = new Room.Corner(10,0);
        Room.Corner three = new Room.Corner(10,10);
        Room.Corner four = new Room.Corner(0,10);
        r.addCorner(one);
        r.addCorner(two);
        r.addCorner(three);
        r.addCorner(four);
        r.isValidRoom();
        test.addRoom(r);
        Room Current = test.getRooms(0);
        Assert.assertEquals(Current,r);
    }

    @Test
    public void addDoor() {
        double []coords = {0.0,5.0};
        boolean result = test.addDoor(new Door(NodeType.singleDoor,coords,0));
        Assert.assertTrue( result);
    }

    @Test
    public void removePeople() {
        test.removePeople();
        Assert.assertEquals(0,test.getNumPeople());
    }

    @Test
    public void getAllDoors() {
        double []coords = {0.0,5.0};
        Vector<Door> doors = new Vector<Door>();
        doors.add(new Door(NodeType.singleDoor,coords,0));
        test.addDoor(doors.get(0));
        Assert.assertEquals(test.getAllDoors().get(0).nodeId,doors.get(0).node.nodeId);
    }

    @Test
    public void addPerson() {
        double []coords = {5.0,5.0};
        Person p = new Person("0",coords);
        Boolean result = test.addPerson(p);
        Assert.assertEquals(true, result );
    }

    @Test
    public void addFire() {
        Fire f= new Fire(5,5,2);
        test.addFire(f);
        Assert.assertEquals(f,test.fires.get(0));
    }

    @Test
    public void getNumPeople() {

        double []coords = {5.0,5.0};
        Person p = new Person("0",coords);
        test.addPerson(p);
        Assert.assertEquals(1,test.getPeople().size() );
    }

    @Test
    public void getPeople() {
        double []coords = {5.0,5.0};
        Person p = new Person("0",coords);
        test.addPerson(p);
        Assert.assertEquals(p,test.getPeople().get(0) );

    }
}