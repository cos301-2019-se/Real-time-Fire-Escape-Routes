package Builder;
import Building.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;
/**
 * The BuildingManager part is used to create the buildings that will
 * be stored in memory. It makes use of various concrete builders to accomplish this.
 * Also seen as the "Director" part of the Builder design pattern
 * */
public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> floors = new Vector<Builder>();
    Vector <Vector<Builder>> halls = new Vector<>();
    Vector <Vector<Builder>> rooms = new Vector<>();
    Vector <Vector<Builder>> doors = new Vector<>();
    Vector <Vector<Builder>> stairs = new Vector<>();
    Vector <Vector<Builder>> sensors = new Vector<>();
    Vector <Vector<Builder>> people = new Vector<>();
    private static boolean verbose = true;
    JSONArray peopleData;
    JSONObject buildingData ;

    /**
     * The constructor is used to split the data into smaller pieces creating Concrete Builders where needed
     * @param BuildingData: Contains the full building details that needs to be constructed
     * */
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;
        try {


            JSONArray TempData = (JSONArray)buildingData.get("floors");
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                data.put("floor",TempData.get(i));
                System.out.println(data.toString());
                floors.add(new RoomBuilder(data));
            }
            int MaxFloors = floors.size();

            halls = new Vector<>();
            for (int i = 0; i < floors.size(); i++) {
                halls.add( new Vector<>());
            }
            TempData = (JSONArray)buildingData.get("halls");

            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                JSONObject data2 = (JSONObject)TempData.get(i);
                data.put("halls",TempData.get(i));
                int floornum = data2.getInt("floor");
                halls.get(floornum).add(new RoomBuilder(data));
            }


            rooms = new Vector<>();
            for (int i = 0; i < floors.size(); i++) {
                rooms.add( new Vector<>());
            }
            TempData = (JSONArray)buildingData.get("rooms");
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                JSONObject data2 = (JSONObject)TempData.get(i);
                data.put("rooms",TempData.get(i));
                int floornum = data2.getInt("floor");
                rooms.get(floornum).add(new RoomBuilder(data));
            }
            /**
             * Stairs
             * */
            stairs = new Vector<>();
            try {
                TempData = (JSONArray) buildingData.get("stairs");
                for (int i = 0; i < floors.size(); i++) {
                    stairs.add(new Vector<>());
                }
                for (int i = 0; i < TempData.length(); i++) {
                    JSONObject data = new JSONObject();
                    JSONObject data2 = (JSONObject) TempData.get(i);
                    data.put("stairs", TempData.get(i));
                    int floornum = data2.getInt("floor");
                    stairs.get(floornum).add(new StairsBuilder(data, floornum, MaxFloors));
                }
            }catch (Exception e){
                System.out.println("No stairs in JSON object, continueing build...");
            }
            doors = new Vector<>();
            for (int i = 0; i < floors.size(); i++) {
                doors.add( new Vector<>());
            }

            JSONArray doorData = (JSONArray)buildingData.get("doors");
            for (int i = 0; i < doorData.length() ; i++) {
                JSONObject door = (JSONObject)doorData.get(i);
                int floornum = door.getInt("floor");
                doors.get(floornum).add(new DoorBuilder(doorData.get(i)));
            }

            if(buildingData.has("sensors")){
                sensors = new Vector<>();
                for (int i = 0; i < floors.size(); i++) {
                    sensors.add( new Vector<>());
                }
                JSONArray sensorData = (JSONArray)buildingData.get("sensors");
                for (int i = 0; i < sensorData.length() ; i++) {
                    JSONObject Sensor = (JSONObject) sensorData.get(i);
                    int floornum = Sensor.getInt("floor");
                    sensors.get(floornum).add(new SensorPlacer(sensorData.get(i)));
                }
            }

            try{
                peopleData = (JSONArray)BuildingData.getJSONArray("people");

            }catch(Exception e){
                if(verbose)
                    System.out.println("No people specified in original dataset");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls 'buildPart()' from all the concrete builders and adds the part to the Building
     * @return Returns a newly created Building
     * */
    public Building construct() {
        if(verbose)
            System.out.println("Creating new building");
        Building building = new Building();

        try {
            for (int i = 0; i < floors.size(); i++) {
                Room f =(Room)floors.get(i).buildPart();
                if(verbose)
                    System.out.println(f.isValidRoom());
                building.addFloor(f);
            }

            for (int i = 0; i < halls.size(); i++) {
                for (int j = 0; j < halls.get(i).size(); j++) {
                    Room f =(Room)halls.get(i).get(j).buildPart();
                    building.getFloor(i).addRoom(f);
                    if(verbose)
                        System.out.println(f.isValidRoom());
                }
            }
            for (int i = 0; i < rooms.size(); i++) {
                for (int j = 0; j < rooms.get(i).size(); j++) {
                    Room f =(Room)rooms.get(i).get(j).buildPart();
                    building.getFloor(i).addRoom(f);
                    if(verbose)
                        System.out.println(f.isValidRoom());
                }
            }

            for (int i = 0; i < stairs.size(); i++) {
                for (int j = 0; j < stairs.get(i).size(); j++) {
                    Room f =(Room)stairs.get(i).get(j).buildPart();
                    building.getFloor(i).addRoom(f);
                    if(verbose)
                        System.out.println(f.isValidRoom());
                }
            }
            for (int i = 0; i < doors.size(); i++) {
                for (int j = 0; j < doors.get(i).size(); j++) {
                    boolean status =  building.getFloor(i).addDoor((Door)doors.get(i).get(j).buildPart());
                    if(verbose)
                        System.out.println("Placing door "+status);
                }
            }
            for (int i = 0; i < sensors.size(); i++) {
                for (int j = 0; j < sensors.get(i).size(); j++) {
                    boolean status =  building.getFloor(i).addSensor((Sensor)sensors.get(i).get(j).buildPart());
                    if(verbose)
                        System.out.println("Placing Sensor "+status);
                }
            }
            for (int i = 0; i < people.size(); i++) {
                for (int j = 0; j < people.get(i).size(); j++) {
                    boolean status =  building.getFloor(i).addPerson((Person)people.get(i).get(j).buildPart());
                    if(verbose)
                        System.out.println("Placing Sensor "+status);
                }
            }
            if (stairs.size()>0)
                building.connectStairs();
            building.connectDoors();



            if(peopleData != null){
                PersonManager HumanResources = new PersonManager(building,peopleData);
                HumanResources.construct();
            }


            if(verbose)
                System.out.println("Building Complete");

            constructRoutes(building);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return building;
    }

    /**
     * This function iterates over a given building creating paths to the exit points creates a route for each possible Exit point
     * @param building: The building that needs to create routes
     * */
    private void constructRoutes(Building building)throws Exception{
        if (verbose){
            System.out.println("Building Routes");
        }
        int numRoutes = 0;
        Vector<Node> goalStates = new Vector<>();
        int currentFloor=0;
        for (Room floor:building.getFloors()) {
            Vector<Node> Doors = filterDoors( floor.getAllDoors());
            switch (currentFloor){
                case 0:{ //Ground floor has the route creation option
                    for (Node buildingExit:Doors) {
                        if(buildingExit.getType() == NodeType.buildingExit){
                            goalStates.add(buildingExit);
                            Routes newRoute = new Routes(String.valueOf(numRoutes++));
                            building.addRoute(newRoute);
                            for (Node door:Doors) {
                                if (door.getType() != NodeType.buildingExit)
                                    newRoute.addNode(door);
                            }
                        }
                    }
                    break;
                }
                default:{// Any other Route
                    for (Node door:Doors) {
                        if (door.getType() == NodeType.buildingExit){
                            goalStates.add(door);
                            Routes newRoute = new Routes(String.valueOf(numRoutes++));
                            building.addRoute(newRoute);
                        }
                    }
                    for (Routes route:building.getRoutes()) {
                        for (Node door:Doors) {
                            if(door.getType()!=NodeType.buildingExit)
                                route.addNode(door);
                        }
                    }
                }
            }
            currentFloor++;
        }
        for (Routes current:building.getRoutes()) {
            current.addNode(goalStates.remove(0));
        }





        if (verbose) {
            System.out.println("Building Routes Complete");
        }
    }

    /**This function removes duplicate objects from a given Vector*/
    private Vector<Node> filterDoors (Vector<Node> b){
        Vector<Node> a = new Vector<Node>();
        for (int i = 0; i < b.size(); i++) {
            if(!a.contains(b.elementAt(i)))
                a.add(b.elementAt(i));
        }
        return a;
    }

}
