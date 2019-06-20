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
        int numberOfFloors = API.building.getFloors().size();
        response.put("numberFloors",numberOfFloors);

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
        /* //Temp fix for unity
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
         * Adding Stairs to the response
         * */
        try {
            rooms = (JSONArray) API.lastbuild.get("stairs");
            String StairData = "";
            for (int i = 0; i < rooms.length(); i++) {
                JSONObject current = (JSONObject) rooms.get(i);
                int StairsFloor = current.getInt("floor");
                StairData += StairsFloor + " * ";

                if (StairsFloor == 0)
                    StairData += "0 * ";
                else if (StairsFloor == numberOfFloors - 1)
                    StairData += "2 * ";
                else
                    StairData += "1 * ";

                JSONArray corners = (JSONArray) current.get("corners");
                for (int j = 0; j < corners.length(); j++) {
                    JSONArray c = (JSONArray) corners.get(j);
                    StairData += c.getDouble(0) + "," + c.getDouble(1);
                    if (j < corners.length() - 1)
                        StairData += " % ";
                }
                if (i < rooms.length() - 1)
                    StairData += " - ";
            }
            response.put("stairs", StairData);
        }catch (Exception e){
            System.out.println("No Stairs to send to unity");
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
        /****/
        response.put("rooms",data);

        /**
         * Adding Doors to the response
         * */
        data = "";
        JSONArray doors = (JSONArray)API.lastbuild.get("doors");
        for (int i = 0; i < doors.length() ; i++) {
            JSONObject current = (JSONObject) doors.get(i);
            String type = (String)current.get("type");
            if(type.compareTo("stairs") == 0)
                continue;
            data += current.getInt("floor") + " * ";
            switch ((String)current.get("type")){
                case "buildingExit":
                    data+="0.9 * ";
                    break;
                case "singleDoor":
                    data+="0.9 * ";
                    break;
                case "doubleDoor":
                    data+="0.9 * ";
                    break;
                case "stairs":
                    data+="77 * ";
                    break;
                default:
                    data+="0.9 * ";
            }
            JSONArray pos = (JSONArray)current.get("position");
            data += pos.getDouble(0)+","+pos.getDouble(1);

            if( i < doors.length() -1)
                data+= " - ";
        }
        response.put("doors",data);


        /**
         * Adding People
         * */
        data = "";
        JSONArray people = (JSONArray)API.lastbuild.get("people");
        for (int i = 0; i < people.length() ; i++) {
            JSONObject current = (JSONObject) people.get(i);
            data += current.getInt("floor") + " * ";
            data += current.getInt("id")+" * ";
            JSONArray pos = (JSONArray)current.get("position");
            data += pos.getDouble(0)+","+pos.getDouble(1);
            if( i < people.length() -1)
                data+= " - ";
        }
        response.put("people",data);
        return responseMessage;
    }

    private static String build(JSONObject data){
        String temp="Building built successfully";
        API.lastbuild = data;
        try {
            BuildingManager BobTheBuilder = new BuildingManager(data);
            if(API.building != null)
                API.building.clearPeople();
            API.building = BobTheBuilder.construct();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return temp;
    }

}