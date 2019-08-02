package ApiEndpoints;

import Builder.BuildingManager;
import Building.Building;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * BuildingAPI class is used by the '/buildingGeneration' endpoint in the HTTPServer
 * and handles all requests related to creating the Building
 * */
public class BuildingGenerationAPI extends API{
    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains the JSON data that was sent to the server
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    public static JSONObject handleRequest(JSONObject request)throws Exception {
        JSONObject Response = new JSONObject();
        switch ( (String)request.get("type")){
            case "build":{
                Response.put("msg",build(request));
                Response.put("status",true);
                return Response;
            }
            case "buildingData":{
                Response.put("msg",BuildingToUnityString(Response,request));
                Response.put("status",true);
                return Response;
            }
        }

        throw new Exception("Unsupported Request");
    }
    /**
     * This function is used to convert the current Building information to a usable
     * JSONObject for Unity to create a visual representation of the building
     * @param response: Initial repsonse created by the "handleRequest" to which it will also attach additional information
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    private static String BuildingToUnityString(JSONObject response,JSONObject request)throws Exception {
        Building temp = chooseBuilding(request);
        JSONObject last = chooseLastBuild(request);
        if (temp == null)
            throw new Exception("Please build a building first") ;

        String responseMessage = "No people to add yet";
        int numberOfFloors = temp.getFloors().size();
        response.put("numberFloors",numberOfFloors);

        /**
         * Adding Rooms to the response
         * */
        JSONArray rooms = (JSONArray)last.get("rooms");
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
         //Temp fix for unity
        rooms = (JSONArray)last.get("halls");
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
            rooms = (JSONArray) last.get("stairs");
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
        /*
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
        JSONArray doors = (JSONArray)last.get("doors");
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
        JSONArray people = (JSONArray)last.get("people");
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

    /**
     * This function is used to 'Load' a building into the server upon which all actions take place
     * @param data: Contains various coordinate information about the building, stairs, dummy people
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    private static String build(JSONObject data)throws Exception{
        Building newbuilding = null ;
        JSONObject last = null;


        String temp="Building built successfully";
        boolean isSimulation = false;
        if(isSimulation){
            newbuilding = buildings[1];
            last =  lastbuild[1];
        }
        else{
            newbuilding = buildings[0];
            last =  lastbuild[0];
        }
        if(data.has("mode")){
            if(data.getString("mode").compareToIgnoreCase("simulation")==0){
                isSimulation = true;
            }
        }
        if(isSimulation){
            lastbuild[1] = data;
        }
        else{
            lastbuild[0] = data;
        }
        try {
            BuildingManager BobTheBuilder = new BuildingManager(data);
            if(newbuilding != null){
                newbuilding.clearPeople();
            }
            newbuilding = BobTheBuilder.construct();
            if(isSimulation){
                buildings[1] = newbuilding;
            }
            else{
                buildings[0] = newbuilding;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return temp;
    }

}