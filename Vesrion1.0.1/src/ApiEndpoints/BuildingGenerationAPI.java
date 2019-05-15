package ApiEndpoints;

import Builder.BuildingManager;
import org.json.*;

public class BuildingGenerationAPI {
    public static JSONObject handleRequest(JSONObject request)throws Exception {
        JSONObject Response = new JSONObject();
        switch ( (String)request.get("type")){
            case "build":{
                Response.put("msg",build(request));
                Response.put("status",true);
                return Response;
            }
            case "buildingData":{
                Response.put("msg",BuildingToUnityString(Response));
                Response.put("status",true);
                return Response;
            }
        }

        throw new Exception("Unsupported Request");
    }
    private static String BuildingToUnityString(JSONObject response)throws Exception {
        if (API.lastbuild == null)
            throw new Exception("Please build a building first") ;

        String responseMessage = "No people to add yet";
        response.put("numberFloors",API.building.getFloors().size());

        /**
         * Adding Rooms to the response
         * */
        JSONArray rooms = (JSONArray)API.lastbuild.get("rooms");
        String data ="";
        for (int i = 0; i < rooms.length() ; i++) {
            JSONObject current = (JSONObject) rooms.get(i);
            data += current.getInt("floor") + " * ";
            JSONArray corners = (JSONArray)current.get("corners");
            for (int j = 0; j < corners.length(); j++) {
                JSONArray c = (JSONArray)corners.get(j);
                data += c.getDouble(0)+","+c.getDouble(1);
                if(j < corners.length()-1)
                    data+=" % ";
            }
            if( i < rooms.length() -1)
                data+= " - ";
        }

        /**
         * Adding Halls to the response
         * */
        rooms = (JSONArray)API.lastbuild.get("halls");
        data += " - ";
        for (int i = 0; i < rooms.length() ; i++) {
            JSONObject current = (JSONObject) rooms.get(i);
            data += current.getInt("floor") + " * ";
            JSONArray corners = (JSONArray)current.get("corners");
            for (int j = 0; j < corners.length(); j++) {
                JSONArray c = (JSONArray)corners.get(j);
                data += c.getDouble(0)+","+c.getDouble(1);
                if(j < corners.length()-1)
                    data+=" % ";
            }
            if( i < rooms.length() -1)
                data+= " - ";
        }

        /**
         * Adding Floors to the response
         * */
        rooms = (JSONArray)API.lastbuild.get("floors");
        data += " - ";
        for (int i = 0; i < rooms.length() ; i++) {
            JSONObject current = (JSONObject) rooms.get(i);
            data += current.getInt("floor") + " * ";
            JSONArray corners = (JSONArray)current.get("corners");
            for (int j = 0; j < corners.length(); j++) {
                JSONArray c = (JSONArray)corners.get(j);
                data += c.getDouble(0)+","+c.getDouble(1);
                if(j < corners.length()-1)
                    data+=" % ";
            }
            if( i < rooms.length() -1)
                data+= " - ";
        }
        response.put("rooms",data);

        /**
         * Adding Doors to the response
         * */
        data = "";
        JSONArray doors = (JSONArray)API.lastbuild.get("doors");
        for (int i = 0; i < doors.length() ; i++) {
            JSONObject current = (JSONObject) doors.get(i);
            data += current.getInt("floor") + " * ";
            data += (String)current.get("type") + " * ";
            JSONArray pos = (JSONArray)current.get("position");
            data += pos.getDouble(0)+","+pos.getDouble(1);

            if( i < rooms.length() -1)
                data+= " - ";
        }
        response.put("doors",data);



        return responseMessage;
    }

    private static String build(JSONObject data){
        String temp="Building built successfully";
        API.lastbuild = data;
//        lastBuild = data;
        try {
            BuildingManager BobTheBuilder = new BuildingManager(data);
            API.building = BobTheBuilder.construct();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return temp;
    }

}