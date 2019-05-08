package Builder;
import Building.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> floors = new Vector<Builder>();
    Vector <Builder> halls = new Vector<Builder>();
    Vector <Builder> rooms = new Vector<Builder>();
    Vector <Builder> doors = new Vector<Builder>();
    JSONObject buildingData ;
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;
        try {
            JSONArray TempData = (JSONArray)buildingData.get("floors");
            //System.out.println(TempData.get(0));
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                data.put("floor",TempData.get(i));
                floors.add(new RoomBuilder(data));
            }
            TempData = (JSONArray)buildingData.get("halls");
            System.out.println(TempData.get(0));
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                data.put("halls",TempData.get(i));
                halls.add(new RoomBuilder(data));
            }
            TempData = (JSONArray)buildingData.get("rooms");
            System.out.println(TempData.get(0));
            for (int i = 0; i < TempData.length() ; i++) {
                JSONObject data = new JSONObject();
                data.put("rooms",TempData.get(i));
                rooms.add(new RoomBuilder(data));
            }

            /*
            JSONArray doorData = (JSONArray)buildingData.get("doors");
            for (int i = 0; i < doorData.length() ; i++) {
                doors.add(new DoorBuilder(doorData.get(i)));
            }
            /*
            JSONArray peopleData = (JSONArray)buildingData.get("people");
            for (int i = 0; i < peopleData.length() ; i++) {
                people.add(new babyMaker(peopleData.get(i)));
            }
            */
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
                Room f =(Room)halls.get(i).buildPart();
                System.out.println(f.isValidRoom());
                building.addFloor(f);
            }for (int i = 0; i < rooms.size(); i++) {
                Room f =(Room)rooms.get(i).buildPart();
                System.out.println(f.isValidRoom());
                building.addFloor(f);
            }


            // /**Making Rooms*/
            /*
                for (int i = 0; i < rooms.size(); i++) {
                    Builder r =rooms.get(i);
                    temp.getFloor(0).addRoom((Room)r.buildPart());
                }
            ///**Making Doors*/
            /*
            for (int i = 0; i < doors.size(); i++) {
                Builder r = doors.get(i);
                temp.getFloor(0).getRooms().get(0).addDoor((Door)r.buildPart());
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
