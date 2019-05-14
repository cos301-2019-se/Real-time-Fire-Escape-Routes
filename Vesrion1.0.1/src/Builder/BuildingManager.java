package Builder;
import Building.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> floors = new Vector<Builder>();
    Vector <Vector<Builder>> halls = new Vector<>();
    Vector <Vector<Builder>> rooms = new Vector<>();
    Vector <Vector<Builder>> doors = new Vector<>();

    JSONObject buildingData ;
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;
        try {
            JSONArray TempData = (JSONArray)buildingData.get("floors");
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                data.put("floor",TempData.get(i));
                floors.add(new RoomBuilder(data));
            }


            for (int i = 0; i < floors.size(); i++) {
                halls = new Vector<>();
                halls.add( new Vector<>());
            }
            TempData = (JSONArray)buildingData.get("halls");

            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                JSONObject data2 = (JSONObject)TempData.get(0);
                data.put("halls",TempData.get(i));
                int floornum = data2.getInt("floor");
                halls.get(floornum).add(new RoomBuilder(data));
            }


            for (int i = 0; i < floors.size(); i++) {
                rooms = new Vector<>();
                rooms.add( new Vector<>());
            }
            TempData = (JSONArray)buildingData.get("rooms");
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                JSONObject data2 = (JSONObject)TempData.get(0);
                data.put("rooms",TempData.get(i));
                int floornum = data2.getInt("floor");
                rooms.get(floornum).add(new RoomBuilder(data));
            }


            for (int i = 0; i < floors.size(); i++) {
                doors = new Vector<>();
                doors.add( new Vector<>());
            }

            JSONArray doorData = (JSONArray)buildingData.get("doors");
            for (int i = 0; i < doorData.length() ; i++) {
                JSONObject door = (JSONObject)doorData.get(i);
                int floornum = door.getInt("floor");
                doors.get(floornum).add(new DoorBuilder(doorData.get(i)));
            }
            /**/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Building construct() {
        System.out.println("Creating new building");
        Building building = new Building();

        try {
            for (int i = 0; i < floors.size(); i++) {
                Room f =(Room)floors.get(i).buildPart();
                System.out.println(f.isValidRoom());
                building.addFloor(f);
            }

            for (int i = 0; i < halls.size(); i++) {
                for (int j = 0; j < halls.get(i).size(); j++) {
                    Room f =(Room)halls.get(i).get(j).buildPart();
                    building.getFloor(i).addRoom(f);
                    System.out.println(f.isValidRoom());
                }
            }
            for (int i = 0; i < rooms.size(); i++) {
                for (int j = 0; j < rooms.get(i).size(); j++) {
                    Room f =(Room)rooms.get(i).get(j).buildPart();
                    building.getFloor(i).addRoom(f);
                    System.out.println(f.isValidRoom());
                }
            }

            for (int i = 0; i < doors.size(); i++) {
                for (int j = 0; j < doors.get(i).size(); j++) {
                    boolean status =  building.getFloor(i).addDoor((Door)doors.get(i).get(j).buildPart());
                    System.out.println("Placing door "+status);
                }
            }


            ///**Making People*/
            /*
            for (int i = 0; i < people.size(); i++) {
                Builder r = people.get(i);
                temp.getFloor(0).getRooms().get(0).addPerson((Person)r.buildPart());

            }
            /**/
            System.out.println("Building Complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return building;
    }
}
