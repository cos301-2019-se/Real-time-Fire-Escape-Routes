package Builder;

import Building.Door;
import Building.NodeType;
import org.json.JSONArray;
import org.json.JSONException;

public class DoorBuilder extends Builder{

    public DoorBuilder(Object _data) {
        super(_data);
    }
    protected Door buildPart(){
        try {
            //System.out.println("DoorBuilder - "+data);
            double [] pos= new double[2];
            JSONArray posData =(JSONArray)data.get("position");
            pos[0]= posData.getDouble(0);
            pos[1]= posData.getDouble(1);
            int floor = data.getInt("floor");

            String type = data.get("type").toString();
            switch (type){
                case "stairs":{
                    return new Door(NodeType.stairs,pos,floor);
                }
                case "singleDoor":{
                    return new Door(NodeType.singleDoor,pos,floor);
                }
                case "doubleDoor":{
                    return new Door(NodeType.doubleDoor,pos,floor);
                }
                case "buildingExit":{
                    return new Door(NodeType.buildingExit,pos,floor);
                }
                case "goal":{
                    return new Door(NodeType.goal,pos,floor);
                }
            }
            throw new Exception();
        } catch (Exception e) {
            System.out.println("Door FAIL");
            e.printStackTrace();
        }
        return null;
    }
}