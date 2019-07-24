package Builder;

import Building.Sensor;
import org.json.JSONArray;

public class SensorPlacer extends Builder {
    /**
     * The Constructer is used to save the data of an object that will be
     * built when the buildPart Function is called
     *
     * @param _data : Contains specific information of the object that needs to be created
     */
    public SensorPlacer(Object _data) {
        super(_data);
    }
    protected Sensor buildPart(){
        try {
            //System.out.println("DoorBuilder - "+data);
            double [] pos= new double[2];
            JSONArray posData =(JSONArray)data.get("position");
            pos[0]= posData.getDouble(0);
            pos[1]= posData.getDouble(1);
            int floor = data.getInt("floor");
            String deviceID = (String)data.get("sensorID");
            return new Sensor(pos,floor,deviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
