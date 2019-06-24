package Building;
import java.util.Vector;

import static Building.NodeType.stairs;

public class Building {
      private static int numBuildings = 0;
      private Vector<Room> Floor= new Vector<>();
      private int id;
      private boolean verbose = true;
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
            Person.numPeople = 0;
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
            //Old
            /*
            try{
                  for (int i = 0; i < getNumFloors(); i++) {
                        getFloor(i).assignPeople(Routes);
                  }
            }
            catch (Exception e){
                  System.out.println(e.getMessage());
            }
            /** New */

            Vector<Person> people = new Vector<Person>();
            for (Room floor:Floor) {
                  people.addAll(floor.getPeopleData(Routes));
            }
            if(verbose) {
                  System.out.println("======= Before Sort =======");
                  printPeopleData(people);
            }
            sort(people,0,people.size()-1);

            if(verbose) {
                  System.out.println("======= After Sort =======");
                  printPeopleData(people);
            }
            /**
             * Now Route Assignment can begin
             * */

            for (Person p : people) {
                  Vector<Node> Bestpath= new Vector<Node>();
                  Routes bestRoute =null;
                  double bestDistance = Double.MAX_VALUE;
                  for (Routes r:Routes) {
                        for (Door d:p.availableDoors) {
                              r.resetVisited();
                              Vector<Node> path =  r.ShortestPathToGoal(d.node,r.getGoal());
                              double tempD = r.pathHeuristic(path,p);
                              if(tempD < bestDistance){
                                    Bestpath = path;
                                    bestDistance = tempD;
                                    bestRoute = r;
                              }
                        }
                  }
                  p.setAssignedRoute(bestRoute);
                  bestRoute.addPerson(p);
                  if(verbose){
                        System.out.println("Person "+p.name+" is assigned to Route "+p.AssignedRoute.RouteName );
//                        System.out.println("Heuristic safety value is: "+ bestRoute.pathHeuristic(Bestpath,p));
//                        System.out.println("Actual Distance: "+bestRoute.pathDistance(Bestpath,p));
                        bestRoute.printPath(Bestpath,p);
                        System.out.println();
                  }
            }
            /**/
      }

      public void connectStairs(){
            Vector<Node> stairs = getStairs();
            Node last= stairs.remove(0);
            int i=0;
            while (stairs.size()>0){
                  if(i== stairs.size()){
                        last = stairs.remove(0);
                        i=0;
                  }
                  if(last.coordinates[0] == stairs.get(i).coordinates[0] && last.coordinates[1] == stairs.get(i).coordinates[1] && last.nodeId != stairs.get(i).nodeId){
                       last.connect(stairs.get(i), floorHeight);
                       last = stairs.remove(i);
                       i=0;
                  }
                  i++;
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


      int partition(Vector<Person> arr, int low, int high)
      {
            double pivot = arr.get(high).distanceToExit;
            int i = (low-1); // index of smaller element
            for (int j=low; j<high; j++)
            {
                  // If current element is smaller than or
                  // equal to pivot
                  if (arr.get(j).distanceToExit <= pivot)
                  {
                        i++;
                        // swap arr[i] and arr[j]
                        Person temp = arr.get(i);
                        arr.setElementAt(arr.get(j),i);;
                        arr.setElementAt(temp,j);
                  }
            }
            // swap arr[i+1] and arr[high] (or pivot)
            Person temp = arr.get(i+1);
            arr.setElementAt(arr.get(high),i+1);
            arr.setElementAt(temp,high);
            return i+1;
      }


      /* The main function that implements QuickSort()
        arr[] --> Array to be sorted,
        low  --> Starting index,
        high  --> Ending index */
      void sort(Vector<Person> arr, int low, int high)
      {
            if (low < high)
            {
            /* pi is partitioning index, arr[pi] is
              now at right place */
                  int pi = partition(arr, low, high);

                  // Recursively sort elements before
                  // partition and after partition
                  sort(arr, low, pi-1);
                  sort(arr, pi+1, high);
            }
      }
      private void printPeopleData(Vector<Person> peopleData){
            for (Person p:peopleData) {
                  System.out.println("Person ID: "+p.personID +" Distance to exit: "+p.distanceToExit);
            }
      }

      public boolean addFire(int floor,Fire f){
            return false;
      }
}
