package Builder; //Builder design pattern - Concrete Builder

import org.json.JSONException;
import org.json.JSONObject;

public class RoomBuilder extends Builder {
    RoomBuilder(Object data) {
        super(data);
    }
    Room buildPart(){
        System.out.println(data.toString());
        try {
            double[] x = new double[2];
            double[] y = new double[2];
            double[] z = new double[2];
            // Extract numbers from JSON array.
            for (int i = 0; i <2; ++i) {
                x[i] =  data.getJSONArray("x").getDouble(i);
                y[i] =  data.getJSONArray("y").getDouble(i);
                z[i] =  data.getJSONArray("z").getDouble(i);
            }
            double array [][] = {x,z,y};
            Room temp = new Room(array);
            return temp;
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