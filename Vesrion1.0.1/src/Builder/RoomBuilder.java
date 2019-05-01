package Builder;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomBuilder extends Builder {
    RoomBuilder(Object data) {
        super(data);
    }
    Room buildPart(){
        try {
            double [] x= (double[])data.get("x");
            double [] y= (double[])data.get("y");
            double [] z= (double[])data.get("z");

        double array [][] = {x,z,y};
        Room temp = new Room(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


/*
*           double array [][] = new double[3][2];
            for(int i = 0; i < 3; i++)
            {
                array[i][0] = 0;
                array[i][1] = 10;
            }
            Room room1 = new Room(array);
*
*
*
*
* */