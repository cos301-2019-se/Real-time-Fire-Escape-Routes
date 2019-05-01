package Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> rooms = new Vector<Builder>();
    Vector <Builder> doors = new Vector<Builder>();
    Vector <Builder> people = new Vector<Builder>();
    JSONObject buildingData ;
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;
        try {
            JSONArray roomData = (JSONArray)buildingData.get("rooms");
            for (int i = 0; i < roomData.length() ; i++) {
                rooms.add(new RoomBuilder(roomData.get(i)));
            }

            JSONArray doorData = (JSONArray)buildingData.get("doors");
            for (int i = 0; i < doorData.length() ; i++) {
                doors.add(new DoorBuilder(doorData.get(i)));
            }

            JSONArray peopleData = (JSONArray)buildingData.get("people");
            for (int i = 0; i < peopleData.length() ; i++) {
                people.add(new babyMaker(peopleData.get(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Building construct() {
        System.out.println("Creating new building");
        Building temp = new Building();
        try {
            temp.addFloor(new Floor());
            /**Making Rooms*/
                for (int i = 0; i < rooms.size(); i++) {
                    Builder r =rooms.get(i);
                    temp.getFloor(0).addRoom((Room)r.buildPart());
                }
            /**Making Doors*/
            for (int i = 0; i < doors.size(); i++) {
                Builder r = doors.get(i);
                temp.getFloor(0).getRooms().get(0).addDoor((Door)r.buildPart());
            }
            /**Making People*/
            for (int i = 0; i < people.size(); i++) {
                Builder r = people.get(i);
                temp.getFloor(0).getRooms().get(0).addPerson((Person)r.buildPart());

            }
            System.out.println("Building Complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
