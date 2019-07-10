package ApiEndpoints;

import Building.Fire;
import Building.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Vector;

public class BuildingAPI extends API {
    private static boolean verbose = true;
    public static JSONObject handleRequest(JSONObject request)throws Exception {
        if(verbose){
            System.out.println("BuildingAPI: "+ request.toString());
        }
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
            case "bind":{
                response = new JSONObject();
                response.put("message", bindPerson(request));
                response.put("status",true);
                return response;
            }
            case "personInfo":{
                response = new JSONObject();
                response.put("message", getPersonInfo(request));
                if(building.emergancy){
                    response.put("status",true);
                }
                else{
                    response.put("status",false);
                }
                return response;
            }
        }
        throw new Exception("Unsupported Request");
    }

    private static boolean bindPerson(JSONObject request) {
        int id = request.getInt("id");
        String deviceId = request.getString("device_id");
        return building.bindPerson(id,deviceId);
    }

    private static String getPersonInfo(JSONObject request) {
        if(building== null){
            return "Person is not in building";
        }
        Vector<Person> people = building.getPeople();
        String id = (String)request.get("device_id");
        Person person = null;
        for (Person p:people) {
            if(p.deviceID.compareTo(id)==0){
                person = p;
                break;
            }
        }
        String status = "";
        if(person != null){
            status +="ID: "+person.deviceID+" - ";
            status +="Name: "+person.name+" - ";
            status +="Position: "+Arrays.toString(person.getPosition())+" - ";
            if(building.emergancy){
                status+="Status: Emergancy - ";
                status +="Assigned Route ID: "+person.getAssignedRoute().RouteName+" ";
            }
            else{
                status+="Status: safe - ";
                status+="No assigned Route";
            }
        }
        else{
            status+="Person is not in building";
        }

        return status;
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
        building.emergancy = true;
        Vector<Person> people = building.getPeople();
        String data = "";
        for (int i = 0; i < people.size(); i++) {
            Person c = people.get(i);
            if(c.getAssignedRoute()!=null) {
                double[] pos = c.getAssignedRoute().getGoal().coordinates;
                data += c.getPersonID() + " *" ;
                for (int j = 0; j < c.pathToFollow.size(); j++) {
                    data+= " "+c.pathToFollow.get(j).floor+","+c.pathToFollow.get(j).coordinates[0]+","+c.pathToFollow.get(j).coordinates[1];
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
