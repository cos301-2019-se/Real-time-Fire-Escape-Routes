package Builder; //Builder design pattern - Concrete Builder

import Building.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomBuilder extends Builder {
    public RoomBuilder(Object data) {
        super(data);
    }
    Room buildPart(){
        try {
            if(data.has("floor")){
                JSONObject cornersData  = (JSONObject)data.get("floor");
                double [][] corners =  json2dArrTOJava2dArr((JSONArray)cornersData.getJSONArray("corners"));
                Room temp = new Room(RoomType.floor);
                for (int i = 0; i < corners.length-1; i++)
                    temp.buildWall(corners[i],corners[i+1]);
                temp.buildWall(corners[corners.length-1],corners[0]);
                return temp;
            }
            if(data.has("halls")){
                JSONObject cornersData  = (JSONObject)data.get("halls");
                double [][] corners =  json2dArrTOJava2dArr((JSONArray)cornersData.getJSONArray("corners"));
                Room temp = new Room(RoomType.hall);
                for (int i = 0; i < corners.length-1; i++)
                    temp.buildWall(corners[i],corners[i+1]);
                temp.buildWall(corners[corners.length-1],corners[0]);
                return temp;
            }
            if(data.has("rooms")){
                JSONObject cornersData  = (JSONObject)data.get("rooms");
                double [][] corners =  json2dArrTOJava2dArr((JSONArray)cornersData.getJSONArray("corners"));
                Room temp = new Room(RoomType.normal);
                for (int i = 0; i < corners.length-1; i++)
                    temp.buildWall(corners[i],corners[i+1]);
                temp.buildWall(corners[corners.length-1],corners[0]);
                return temp;
            }

        } catch (Exception e) {
            System.out.println("ROOM FAIL");
            e.printStackTrace();
        }
        return null;
    }
}