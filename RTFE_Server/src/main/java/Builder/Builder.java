package Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Abstract Class that will create various parts that will be used in the building.
 * Also seen as the "Builder" part of the Builder design pattern
 * */
public abstract class Builder {
    static boolean verbose = false;
    JSONObject data;
    /**
     * The Constructer is used to save the data of an object that will be
     * built when the buildPart Function is called
     * @param _data: Contains specific information of the object that needs to be created
     * */
    public Builder(Object _data){
        data =  (JSONObject)_data;
    }

    /**
     * Makes use of the data saved to create a relevant object
     * @return Returns a newly create object
     * */
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
