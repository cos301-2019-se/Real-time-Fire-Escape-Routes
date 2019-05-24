package Builder;

import Building.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Builder {
    static boolean verbose = false;
    JSONObject data;
    public Builder(Object _data){
        data =  (JSONObject)_data;
    }
    Object buildPart(){
        return null;
    }

    static public double [][] json2dArrTOJava2dArr(JSONArray data){
        try {
            double [][] corners = new double[data.length()][2];
        //System.out.println("Builder: "+data.toString());
        for (int i = 0; i < data.length(); i++) {
           JSONArray index =  (JSONArray)data.get(i);
           corners[i][0] =index.getDouble(0);
           corners[i][1] =index.getDouble(1);
        }
            return corners;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
