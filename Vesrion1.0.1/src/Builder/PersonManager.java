package Builder;

import Building.Building;
import Building.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonManager {

    JSONArray peopleData;
    public Building building;
    static boolean verbose = true;

    public PersonManager(Building b,JSONArray data){
        peopleData=data;
        building = b;
    }
    public void construct(){
        try {

            for (int i = 0; i < peopleData.length(); i++) {
                boolean status = false;
                JSONObject person = (JSONObject) peopleData.get(i);
                JSONArray pos = person.getJSONArray("position");
//                double [] position = {pos.getDouble(0),pos.getDouble(1)};
                double [] position = {pos.getDouble(1),pos.getDouble(0)};
                int floor =person.getInt("floor");
                if(floor<building.getFloors().size())
                    status = building.getFloor(floor).addPerson(new Person(person.get("id").toString(),position));
                if(verbose){
                    System.out.println("Placing person "+i+" - "+status);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void reset(){
        building.clearPeople();
    }

}
