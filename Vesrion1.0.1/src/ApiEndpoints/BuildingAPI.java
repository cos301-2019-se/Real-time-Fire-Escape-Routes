package ApiEndpoints;

import Building.Person;
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
//                response.put("msg", "Successfully assigned People");
                response.put("people", peopleToUnity());
                response.put("numRoutes",building.getRoutes().size());
                response.put("status",true);

                return response;
            }
        }
        throw new Exception("Unsupported Request");
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
        return"";
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
            double[] pos =c.getAssignedRoute().getGoal().coordinates;
            data+= c.getPersonID() + " * "+ pos[0]+","+pos[1];
            if(i<people.size()-1)
                data+= " % ";
//            System.out.println("PersonID: "+c.getPersonID()+" goal:"+ Arrays.toString(c.getAssignedRoute().getGoal().coordinates));
        }
        return data ;
    }

}
