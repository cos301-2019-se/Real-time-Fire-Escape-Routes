package ApiEndpoints;

import Building.Fire;
import Building.Node;
import Building.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Vector;

public class BuildingAPI extends API {
    public static JSONObject handleRequest(JSONObject request)throws Exception {
        //String reqType = (String)req.get("type");
        JSONObject response;
        switch ((String)request.get("type")){
            case "getNumRooms":{
                response = getNumRooms(request);
                return response;
            }
            case "assignPeople":{
                building.assignPeople();
                response = new JSONObject();
                response.put("people", peopleToUnity());
                response.put("numRoutes",building.getRoutes().size());
                response.put("status",true);

                return response;
            }
            case "clearPeople":{
                response = new JSONObject();
                response.put("message", clearPeople());
                response.put("status",true);
                return response;
            }
            case"fire":{
                response = new JSONObject();
                response.put("message", addFire(request));
                response.put("status",true);
                return response;
            }
        }
        throw new Exception("Unsupported Request");
    }

    private static String addFire(JSONObject request) {
        JSONArray pos = request.getJSONArray("position");
        double radius = request.getDouble("radius");
        Fire f = new Fire(pos.getDouble(0),pos.getDouble(1),radius);
        int floor = request.getInt("floor");
        try {
            if (building.addFire(floor, f)) {
                return "Fire Added. Some Routes were affected";
            }
        }catch (Exception e){
            return e.getMessage();
        }
        return "Fire Added. No Routes were affected";
    }

    private static JSONObject getNumRooms(JSONObject req) throws Exception {
        JSONObject Response = new JSONObject();
        try{
            Response.put("status", true);

            Response.put("msg","There are "+ building.getFloor(0).getRooms().size()+" rooms");
            boolean status= false;
        }catch(Exception e){
            throw e;
        }
        return Response;
    }

    private static String clearPeople(){
        building.clearPeople();
        return "Removed people from building";
    }
    private static JSONObject getRoutes(JSONObject response) throws JSONException {
        JSONObject Response = new JSONObject();
        try{
            Response.put("status", true);
            Response.put("people", peopleToUnity());
        }catch(Exception e){
            Response.put("Exception",e.getMessage()) ;
            Response.put("status",false);
            return Response;
        }
        return Response;


    }
    private static String peopleToUnity(){
        Vector<Person> people = building.getPeople();
        String data = "";
        for (int i = 0; i < people.size(); i++) {
            Person c = people.get(i);
            if(c.getAssignedRoute()!=null) {
                double[] pos = c.getAssignedRoute().getGoal().coordinates;
                data += c.getPersonID() + " *" ;
                for (int j = 0; j < c.pathToFollow.size(); j++) {
                    data+= " "+c.pathToFollow.get(j).coordinates[0]+","+c.pathToFollow.get(j).coordinates[1];
                    if(j!= c.pathToFollow.size()-1){
                        data +=" % ";
                    }
                }

                if (i < people.size() - 1) {
                    if(people.get(i+1).getAssignedRoute()!=null) {

                        data += " - ";
                    }
                }
            }
//            System.out.println("PersonID: "+c.getPersonID()+" goal:"+ Arrays.toString(c.getAssignedRoute().getGoal().coordinates));
        }
        return data ;
    }

}
