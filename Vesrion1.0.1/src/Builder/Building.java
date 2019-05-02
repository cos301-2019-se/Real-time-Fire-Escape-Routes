package Builder;

import java.util.Vector;

public class Building {
      private static int numBuildings = 0;
      private Vector<Floor> levels= new Vector<>();
      private int id;

      public Building() {
            id = numBuildings++;
      }
      public int getId() {
            return id;
      }

      /**Floor related functions*/
      public void addFloor(Floor f){
            levels.add(f);
      }
      public Floor getFloor(int i) {
            return levels.get(i);
      }
      public Vector<Floor> getFloors() {
            return levels;
      }

      /**People related functions*/
      public void AssignRoutes()
      {
            for (Floor f: levels) {
                  Vector<Person> peopleOnFloor = f.ListPeople();
                  for (Person p: peopleOnFloor) {
                        //Assign a RANDOM route for now
                  }
            }
      }
      public Vector<Person> ListPeople()
      {
          Vector<Person> temps = new Vector<>();
            return temps;
            
      }
      public int getNumPeople() {
            int i =0;
            for (Floor f:levels) {
                  i+= f.getNumPeople();
            }
            return i;
      }
}
