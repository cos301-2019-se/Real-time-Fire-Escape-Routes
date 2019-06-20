package Builder;

import Building.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class StairsBuilder extends Builder {
    RoomType StairType;
    public StairsBuilder(Object _data,int _floor,int _maxFloor) {
        super(_data);
//        if(_floor == 0)
//            StairType = RoomType.stairsBottom;
//        else if (_floor == _maxFloor)
//            StairType = RoomType.stairsTop;
//        else
            StairType = RoomType.stairs;
    }
    Room buildPart(){
        try {
            if(data.has("stairs")){
                JSONObject cornersData  = (JSONObject)data.get("stairs");
                double [][] corners =  json2dArrTOJava2dArr((JSONArray)cornersData.getJSONArray("corners"));
                Stairs temp = new Stairs(StairType);
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
