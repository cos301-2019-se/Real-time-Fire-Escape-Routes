package ApiEndpoints;

import org.json.JSONObject;

public class BuildingAPI extends API {
    public static JSONObject handleRequest(JSONObject request)throws Exception {
        //String reqType = (String)req.get("type");
        JSONObject response;
        switch ((String)request.get("type")){
            case "getNumRooms":{
                response = getNumRooms(request);
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
}
