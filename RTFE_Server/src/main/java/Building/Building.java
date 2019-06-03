package Building;
import java.util.Vector;

public class Building {
      private static int numBuildings = 0;
      private Vector<Room> Floor= new Vector<>();
      private int id;

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
            return Floor.get(0).connectDoors();
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

}
