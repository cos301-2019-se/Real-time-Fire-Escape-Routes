package Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BuildingManager {// Builder design pattern - Director
    Vector <Builder> RoomBuilder ;
    JSONObject buildingData ;
    public BuildingManager(JSONObject BuildingData){
        buildingData =BuildingData;

    }

    public Building construct() {
        Building temp = new Building();
        try {
            JSONArray rooms = (JSONArray)buildingData.get("rooms");
            temp.addFloor(new Floor());
            for (int i = 0; i < rooms.length(); i++) {

                temp.getFloor(0).addRoom(new RoomBuilder(rooms.get(i)).buildPart());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
