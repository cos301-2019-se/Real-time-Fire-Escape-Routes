package Building;
import java.util.Vector;

import static Building.NodeType.stairs;

public class Building {
      private static int numBuildings = 0;
      private Vector<Room> Floor= new Vector<>();
      private int id;
      private double floorHeight = 3.0; //needed for connecting stairs

      public Vector<Routes> getRoutes() {
            return Routes;
      }

      private Vector<Routes> Routes = new Vector<Routes>();

      public Building() {
            id = numBuildings++;
      }
      public int getId() {
            return id;
      }
      /**Floor related functions*/
      public void addFloor(Room r){
            Floor.add(r);
      }
      public Room getFloor(int i) {
            return Floor.get(i);
      }
      public Vector<Room> getFloors() {
            return Floor;
      }
      public Vector<Person> getPeople(){
            Vector<Person> PeopleList = new Vector<Person>();
            for (int i = 0; i < Floor.size(); i++) {
                  PeopleList.addAll(Floor.get(i).getPeople());
            }
            return PeopleList;
      }

      public int getNumPeople(){
            int total = 0;
            for (int i = 0; i < Floor.size(); i++) {
                  total += Floor.get(i).getNumPeople();
            }
            return total;
      }
      public boolean addPerson(Person p,int floor){
            return Floor.get(floor).addPerson(p);
      }

      public void clearPeople(){
            for (int i = 0; i < Floor.size(); i++) {
                  Floor.get(i).removePeople();
            }
      }

      public int getNumFloors(){
            return Floor.size();
      }
      public boolean connectDoors()
      {
          boolean status = true;
          for (Room floor:Floor) {
              boolean success= floor.connectDoors();
              status = !success? false : status;
          }
          return status;
      }

      public void addRoute(Routes r) {
            Routes.add(r);
      }



      public void assignPeople(){
            try{
                  for (int i = 0; i < getNumFloors(); i++) {
                        getFloor(i).assignPeople(Routes);
                  }
            }
            catch (Exception e){
                  System.out.println(e.getMessage());
            }
      }

      public void connectStairs(){
            Vector<Node> stairs = getStairs();
            for (int i = 0; i < stairs.size(); i++) {
                  Node current = stairs.remove(0);
                  for (int j = 0; j < stairs.size() ; j++) {
                        Node other = stairs.get(i);
                        if(current.coordinates[0] == other.coordinates[0] &&current.coordinates[1] == other.coordinates[1]){
                              other.connect(current,floorHeight);
                              System.out.println("Connecting Stairs");
//                              current = other;
                              break;
                        }
                  }
            }
      }
      private Vector<Node> getStairs(){
            Vector<Node> allNodes = new Vector<>();
            Vector<Node> stairNodes = new Vector<>();
            for (Room floor:Floor) {
                  allNodes.addAll(floor.getAllDoors());
            }
            for (Node current:allNodes) {
                  if (current.type == stairs){
                        stairNodes.add(current);
                  }
            }
            return stairNodes;
      }

}
