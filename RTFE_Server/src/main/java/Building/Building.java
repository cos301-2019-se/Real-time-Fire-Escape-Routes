/**
 * @file Building.java
 * @brief This file contains the structure of the building and the objects that the building consists of.
 * This is used by the system as a representation of the real building and to perform relevant calculations.
 *
 * @author Louw, Matthew Jason
 * @author Bresler,  Mathilda Anna
 * @author Braak, Pieter Albert
 * @author Reva, Kateryna
 * @author  Li, Xiao Jian
 *
 * @date 02/05/2019
 */

package Building;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.Vector;

import static Building.NodeType.stairs;

public class Building {
      private static int numBuildings = 0;
      private Vector<Room> Floor= new Vector<>();
      private int id;
      private boolean verbose = false;
      public boolean emergency = false;
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

      /**
       * Takes a Device ID and binds it to a person inside the building
       * @param id: ID of a person to be bound
       * @param deviceID: The Device ID of the phone to be bound
       * */
      public boolean bindPerson(int id, String deviceID){
          unBindPerson(deviceID);
          Vector<Person> people =getPeople();
          for (Person p :people) {
              if(p.personID == id){
                  p.deviceID = deviceID;
                  return true;
              }
          }
          return false;
      }
      /**
      * Takes a Device ID and removes it from the building
      * @param deviceID: The Device ID of the phone to be bound
      * */
      public boolean unBindPerson( String deviceID){
          Vector<Person> people =getPeople();
          for (Person p :people) {
              if(p.deviceID.compareTo(deviceID)==0){
                  p.deviceID = null;
                  return true;
              }
          }
          return false;
      }
    /**
     * getNumPeople function
     * @brief This function return the amount of people contained in the building
     *
     * @return an integer of the amount of people left in the buidling
     * @date 28/04/2019
     */
      public int getNumPeople(){
            int total = 0;
            for (int i = 0; i < Floor.size(); i++) {
                  total += Floor.get(i).getNumPeople();
            }
            return total;
      }
      /**
       * Updates a persons information inside a building
       * @param id: The ID of the person who's location needs to be updates
       * @param floor: the floor on where the person is now located at
       * @param pos: The new position location of the person
       * */
      public boolean updatePersonLocation(int id,int floor, double [] pos) throws IndexOutOfBoundsException {
          if(floor>=Floor.size()){
              throw new IndexOutOfBoundsException("Please select a valid floor");
          }
          Vector<Person> people = getPeople();
          boolean status = false;
          for (Person p:people) {
              if(p.name.equalsIgnoreCase(String.valueOf(id))){
                  Person temp = p;
                  for (Room currentfloor:Floor) {
                     status = currentfloor.removePerson(p);
                  }
                  temp.floor = floor;
                  temp.position = pos;
                  addPerson(temp, floor);
                  if(verbose){
                      if(status){
                          System.out.println("Person "+temp.getName() +" has been updated");
                      }
                  }
                  return status;
              }
          }
          Person p = new Person(String.valueOf(id),pos);
          p.floor = floor;
          status = this.addPerson(p,floor);
          return status;
      }
    /**
     * @brief: Updates a persons information inside a building
     * This is an overloaded function please see the original 'updatePersonLocation' for more information
     * @param device_id: The device_id of the person who's location needs to be updates
     * @param floor: the floor on where the person is now located at
     * @param pos: The new position location of the person
     * */
      public boolean updatePersonLocation(String device_id,int floor, double[] pos) throws IndexOutOfBoundsException {
          Vector<Person> people = getPeople();
          for (Person p:people) {
              if(p.deviceID.equalsIgnoreCase(device_id)){
                  return updatePersonLocation(p.personID, floor,pos );
              }
          }
          return false;
      }
    /**
     * @brief: Removes a person from the building
     * This is an overloaded function please see the original 'remove' for more information
     * @param id: The device_id of the person who's location needs to be updates
     * */
    public boolean remove(int id) throws Exception {
        Vector<Person> people = getPeople();
        for (Person p:people) {
            if(p.personID == id){
                for (Room floor:Floor) {
                    floor.removePerson(p);
                }
            }
        }
        throw new Exception("Person not found");
    }



    /**
     * addPerson function
     * @brief This function inserts a Person object on a specified floor in the building
     *
     * @param p: as a specified Person object
     * @param floor: as an integer indicating which level the Person object should be placed
     * @return - true if the placement was successful
     *         - false if an error occurred during placement
     * @date 28/04/2019
     */
      public boolean addPerson(Person p,int floor){
            return Floor.get(floor).addPerson(p);
      }


    /**
     * clearPeople function
     * @brief This function removes all the Person objects from the current building
     *
     * @return no return value
     * @date 28/04/2019
     */
      public void clearPeople(){
            for (int i = 0; i < Floor.size(); i++) {
                  Floor.get(i).removePeople();
            }
            Person.numPeople = 0;
      }

    /**
     * getNumFloors function
     * @brief this function calculates the amount of floors contained in the current building object
     *
     * @return an integer value of the amount of floor in the current building object
     * @date 12/05/2019
     */
      public int getNumFloors()
      {
            return Floor.size();
      }

    /**
     * connectDoors function
     * @brief this function connect the doors that are on the same paths
     *
     * @return - true if the placement was successful
     *         - false if an error occurred during placement
     * @date 21/05/2019
     */
      public boolean connectDoors()
      {
          boolean status = true;
          for (Room floor:Floor) {
              boolean success= floor.connectDoors();
              status = !success? false : status;
          }
          return status;
      }

    /**
     * addRoute function
     * @brief this function adds routes to the current building object
     *
     * @return no return value
     * @date 21/05/2019
     */
      public void addRoute(Routes r) {
            Routes.add(r);
      }



    /**
     * assignPeople function
     * @brief this function calculates the best routes for each Person object
     * in the current building and assigns them to said route
     *
     * @return no return value
     * @date 27/06/2019
     */
      public synchronized void assignPeople(){

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
                  boolean valid = false;

                for (Routes r:Routes) {
                      for (Door d:p.availableDoors) {
                            r.resetVisited();
                            for (Path c:d.node.Paths) {

//                                r.resetVisited();
                                Vector<Node> path =  r.ShortestPathToGoal(c.end,r.getGoal());
                                c.assignPeople(p);
                                path.insertElementAt(c.start,0);
                                double tempD = r.pathHeuristic(path,p);
                                if(tempD < bestDistance && Path.hasGoal(path)){
                                    valid = Path.hasGoal(path);
                                    Bestpath = path;
                                    bestDistance = tempD;
                                    bestRoute = r;
//                                    System.out.println("Heuristic: " + tempD);

                                }
                                else
                                {
                                    c.removePeople(p);
                                }
                            }



                        }
                  }
                  if(valid){


                      p.pathToFollow = Bestpath;
                        p.setAssignedRoute(bestRoute);
                        bestRoute.addPerson(p);
                  }
                  if(verbose){
                        if(p.getAssignedRoute() != null) {
                              System.out.println("Person " + p.name + " is assigned to Route " + p.AssignedRoute.RouteName);
                        }
                        else {
                              System.out.println("Person " + p.name + " is Trapped!");
                        }
//                        System.out.println("Heuristic safety value is: "+ bestRoute.pathHeuristic(Bestpath,p));
//                        System.out.println("Actual Distance: "+bestRoute.pathDistance(Bestpath,p));
                        if(valid)
                              bestRoute.printPath(Bestpath,p);
                        System.out.println();
                  }
            }
            /**/
      }


    /**
     * connectStairs function
     * @brief this function connects the stairs in the building in a logical manner from one floor to the next
     *
     * @return no return value
     * @date 22/06/2019
     */
      public void connectStairs(){
            Vector<Vector<Node>> stairs = getStairs();
            Vector<Node> last= stairs.remove(0);
            while (stairs.size()>0){
                for (Node bottom: last){
                    for (Node top:stairs.get(0)) {
                        if(Arrays.toString(bottom.coordinates).equals(Arrays.toString(top.coordinates))){
                            bottom.connect(top, floorHeight);
                        }
                    }
                }
                last = stairs.remove(0);
            }
      }

    /**
     * getStairs function
     * @brief this function returns all the stair objects in the current building object
     *
     * @return a Vector<Node> containing all the nodes linked to stair objects
     * @date 04/08/2019
     */
      private Vector<Vector<Node>> getStairs(){
//            Vector<Node> allNodes = new Vector<>();
            Vector<Vector<Node>> stairNodes = new Vector<>();
//            for (Room floor:Floor) {
//                  allNodes.addAll(floor.getAllDoors());
//            }
          for (int i = 0; i <Floor.size() ; i++) {
              stairNodes.add(new Vector<>());
              for (Node current:Floor.get(i).getAllDoors()) {
                  if (current.type == stairs){
                      if(!stairNodes.get(i).contains(current))
                        stairNodes.get(i).add(current);
                  }
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

      public boolean addFire(int floor,Fire f) throws Exception {
            if(floor>=Floor.size()){
                  throw new Exception("Invalid floor");
            }
            Floor.get(floor).addFire(f);

            if(destroyRoutes()>0){
                  return true;
            }

            return false;
      }
      public JSONArray getFires(){
          JSONArray floors = new JSONArray();
          for (Room f :Floor) {
              JSONArray fires = new JSONArray();
              f.getFires(fires);
              floors.put(fires);
          }
          return floors;
      }

      private int destroyRoutes(){
            int numPathsAffected = 0;
            for (Room f:Floor) {
                numPathsAffected+= f.destroyRoutes();
            }
            return numPathsAffected;
      }
}
