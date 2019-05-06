package Building;
import java.util.Vector;

public class Building {
      private static int numBuildings = 0;
      private Vector<Room> Floor= new Vector<>();
      private int id;

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
}
