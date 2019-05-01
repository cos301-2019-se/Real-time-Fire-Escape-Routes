import Builder.Door;
import Builder.Node;
import Builder.Room;
import Builder.Routes;

import java.util.Vector;

public class Main implements Runnable {

    Server myserver;
    @Override
    public void run() {
        myserver.start();
    }
    public Main(Server s) {
        myserver = s;
    }

    public static void main(String[] args){
        try
        {

            //##############################
            //#         HTTP Server        #
            //##############################

            Thread thread = new Thread(new HTTPServer());
            thread.start();



            //##############################
            //#            RTFE            #
            //##############################
/*
            Thread thread1 = new Thread( new RTFEServer());

            thread1.start();
            thread1.wait();
  */
            /*
            double array [][] = new double[3][2];
            for(int i = 0; i < 3; i++)
            {
                array[i][0] = 0;
                array[i][1] = 10;
            }
            Room room1 = new Room(array);
            array[0][0] = 0;
            array[0][1] = 0;
            array[1][0] = 0;
            array[1][1] = 2;
            array[2][0] = 0;
            array[2][1] = 4;
            Node start1 = new Node("door 1", 1);
            Door door1 = new Door(array, start1, "door 1");
            room1.addDoor(door1);
            array[0][0] = 0;
            array[0][1] = 0;
            array[1][0] = 6;
            array[1][1] = 8;
            array[2][0] = 0;
            array[2][1] = 4;

            Node start2 = new Node("door 2",1);
            Door door2 = new Door(array, start2, "door 2");
            room1.addDoor(door2);

            double a [] = new double[2];
            a[0] = 5;
            a[1] = 5;

            person1.setPosition(a);
            room1.addPerson(person1);

            Vector<Integer> assignedDoorsArray = room1.assignPeople();

            System.out.println(room1.doors.get(assignedDoorsArray.get(0)).doorName);
            Node share = new Node(1);
            Node end1 = new Node(1);
            Node end2 = new Node(1);
            start1.connect(share, 1);
            start2.connect(share, 2);
            share.connect(end1, 3);
            share.connect(end2, 5);

            Routes route1 = new Routes("route1");
            route1.addNode(start1);
            route1.addNode(share);
            route1.addNode(end1);
            Routes route2 = new Routes("route2");
            route2.addNode(start2);
            route2.addNode(share);
            route2.addNode(end2);

            System.out.println(route1. distanceToGoal(start1));
            System.out.println(route2. distanceToGoal(start2));
            */
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}









/*
*               Mini representation of how it looks in my head
*               KEY:
*                   'o' means it is a node in the Orange Route
*                   'b' means it is a node in the Blue Route
*                   'S' is our start location
*
*
*
*                       O (Goal)
*                       *
*                       * 4
*                       *
*               ####### O########
*               #       | 3     #
*               #       |       #  4
*               #       S-------B****B
*               #           4   #  (goal)
*               #               #
*               #               #
*               #################
*
*
*
*
*
* */