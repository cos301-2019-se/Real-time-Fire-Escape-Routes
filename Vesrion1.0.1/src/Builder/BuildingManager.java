package Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> rooms = new Vector<Builder>();
    Vector <Builder> doors = new Vector<Builder>();
    JSONObject buildingData ;
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;
        try {
            JSONArray roomData = (JSONArray)buildingData.get("rooms");
            for (int i = 0; i < roomData.length() ; i++) {
                rooms.add(new RoomBuilder(roomData.get(i)));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
