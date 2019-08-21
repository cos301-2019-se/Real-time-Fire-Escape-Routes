package ApiEndpoints;

import Building.*;
import Building.Person;
import Building.Routes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Vector;
/**
 * BuildingAPI class is used by the '/building' endpoint in the HTTPServer
 * and handles all requests related to the Building itself and the people
 * within it.
 * */
public class BuildingAPI extends API {
    private static boolean verbose = true;
    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains the JSON data that was sent to the server
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    private static Building building;
    synchronized public static JSONObject handleRequest(JSONObject request)throws Exception {
        JSONObject response;
        AuthorizeRequest(request);
        if(verbose){
            System.out.println("BuildingAPI: "+ request.toString());
        }
        try{
            building = chooseBuilding(request);
            if(building==null){
                throw new Exception("No building Active");
            }
        }
        catch (Exception e){
            response = new JSONObject();
            response.put("status", false);
            response.put("message", e.getMessage());
            return response;
        }
        switch ((String)request.get("type")){
            case "assignPeople":{
                response = new JSONObject();
                response.put("fires",unityFireLocations());
//       building.assignPeople();
                if(request.has("peopleLocations")){
                    unityUpdatePeopleLocation(request);
                }
                if(request.has("alarm")){
                    if(request.getBoolean("alarm")){
                        building.emergency = true;
                        building.assignPeople();
                        response.put("emergency","true");
                        response.put("people", assignPeopleRoutes());
                        response.put("numRoutes",building.getRoutes().size());
                        response.put("status",true);
                        return response;
                    }
                    else{
                        building.emergency = false;
                        response.put("emergency","false");
                        response.put("people", peopleLocations());
                        response.put("numRoutes",building.getRoutes().size());
                        response.put("status",true);
                        return response;
                    }
                }
//                building.assignPeople();
//                response.put("people", assignPeopleRoutes());
//                if(building.emergency)
//                    response.put("emergency","true");
//                else
//                    response.put("emergency","false");
//                response.put("people", peopleLocations());
//                response.put("numRoutes",building.getRoutes().size());
//                response.put("status",true);
                return response;

            }
            case "bind":{
                response = new JSONObject();
                response.put("message", bindPerson(request));
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
            case"getFire":{
                response = new JSONObject();
                response.put("message", building.getFires());
                response.put("status",true);
                return response;
            }
            case"getSimulationPeople":{
                response = new JSONObject();
                response.put("message", peopleLocationsJSON());
                response.put("status",true);
                return response;
            }
            case "getNumRooms":{
                response = getNumRooms(request);
                return response;
            }
            case "personInfo":{
                response = new JSONObject();
                response.put("message", getPersonInfo(request));
                if(building.emergency){
                    response.put("status",true);
                }
                else{
                    response.put("status",false);
                }
                return response;
            }
            case "personUpdate":{
                return UpdatePersonLocation(request);
            }
            case "removePerson":{
                response = new JSONObject();
                response.put("message", building.remove(request.getLong("id")));
                response.put("status",true);
                return response;
            }
            case "unbind":{
                response = new JSONObject();
                response.put("message", unBindPerson(request));
                response.put("status",true);
                return response;
            }
        }
        throw new Exception("Unsupported Request");
    }


    /**
     * This function adds a fire inside the building
     * @param request: Contains the relevant information to create a fire in the building
     * @return Returns a JSONObject specifying if routes were affected or not.
     * */
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


    /**
     * This function converts all the people data from the building into a string that is used by the simulation subsystem
     * it contains the people's initial location, as well as the path they need to follow to evacuate the building
     * @return a very long string formatted in a specific way
     * */
    private static String assignPeopleRoutes(){
//        building.emergency = true;
        Vector<Person> people = building.getPeople();
        String data = "";
        for (int i = 0; i < people.size(); i++) {
            Person c = people.get(i);
            if(c.getAssignedRoute()!=null) {
                double[] pos = c.getAssignedRoute().getGoal().coordinates;
                data += c.getName() + " *" ;
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
    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains 2 fields, the DEVICE that needs to be linked a ID
     * @return returns a JSON object that contains a success or fail message
     * */
    private static boolean bindPerson(JSONObject request) {
        long id = request.getLong("id");
        String deviceId = request.getString("device_id");
        return building.bindPerson(id,deviceId);
    }

    /**
     * This function removes all the people from the building
     * @return Returns the number of differnt rooms within the building
     * */
    private static String clearPeople(){
        building.clearPeople();
        return "Removed people from building";
    }


    /**
     * This function will be used to retrieve all the information for a specific person
     * @param request: Contains the ID of the person who's info is requested
     * @return Returns a JSONObject containing the following information.
     * ID
     * Name
     * Position within building
     * the status of the building
     * Assigned Route
     *
     * OR
     *
     * Returns a different message if not inside the building
     * */
    private static String getPersonInfo(JSONObject request) {
        if(building== null){
            return "Person is not in building";
        }
        Vector<Person> people = building.getPeople();
        String id = (String)request.get("device_id");
        Person person = null;
        for (Person p:people) {
            if(p.deviceID!=null) {
                if (p.deviceID.compareTo(id) == 0) {
                    person = p;
                    break;
                }
            }
        }
        if(person == null){
            try {
                if (request.has("sensors")) {
                    JSONArray sensors = request.getJSONArray("sensors");
                    for (int i = 0; i < sensors.length(); i++) {
                        JSONObject sensor = sensors.getJSONObject(i);
                        if (building.hasSensorInBuilding(sensor.getString("bssid"))) {
                            JSONObject sensorLocation = building.sensorLocationInBuilding(sensor.getString("bssid"));
                            JSONArray pos = sensorLocation.getJSONArray("position");
                            int floor = sensorLocation.getInt("floor");
                            double [] newPosition = {pos.getDouble(0),pos.getDouble(1)};
                            Person phoneToBePlaced = new Person(String.valueOf(System.currentTimeMillis()),newPosition);
                            phoneToBePlaced.deviceID = request.getString("device_id");
                            building.addPerson(phoneToBePlaced,floor);
                        }
                    }
                }
            }
            catch (Exception e){
                System.out.println("Something when wrong parsing the sensor data");
            }
        }
        if(request.has("sensors")) {
            try {
                JSONArray sensors = request.getJSONArray("sensors");
                for (int i = 0; i < sensors.length(); i++) {
                    JSONObject sensor = sensors.getJSONObject(i);
                    if (building.hasSensorInBuilding(sensor.getString("bssid"))) {
                        JSONObject sensorLocation = building.sensorLocationInBuilding(sensor.getString("bssid"));
                        JSONObject updateRequest = new JSONObject();

                        updateRequest.put("device_id", request.getString("device_id"));
                        updateRequest.put("floor", sensorLocation.getInt("floor"));
                        updateRequest.put("position", sensorLocation.getJSONArray("position"));
                        UpdatePersonLocation(updateRequest);
                    }
                }
            }
            catch (Exception e){
                System.out.println("Something when wrong parsing the sensor data");
            }
        }
        String status = "";
        if(person != null){
            status +="ID: "+person.deviceID+" - ";
            status +="Name: "+person.name+" - ";
            status +="Position: "+Arrays.toString(person.getPosition())+" - ";
            if(building.emergency){
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
    /**
     * This function counts the number of rooms within a building
     * @param req: Contains the relevant information to create a fire in the building
     * @return Returns the number of differnt rooms within the building
     * */
    private static JSONObject getNumRooms(JSONObject req) throws Exception {
        JSONObject Response = new JSONObject();
        int size = 0;
        for (int i = 0; i < building.getFloors().size(); i++) {
            size += building.getFloor(i).getRooms().size();
        }
        try{
            Response.put("status", true);
            Response.put("msg","There are "+size+" rooms");
            boolean status= false;
        }catch(Exception e){
            Response.put("status", false);
            Response.put("msg","An Error occured trying to fetch the Rooms of a building: "+e.getMessage());
            throw e;
        }
        return Response;
    }

    /**
     * This function returns all the routes in the building
     * @param response: Contains the relevant information to calculate the routes in the building
     * @return Returns all the routes in the building
     * */
    private static JSONObject getRoutes(JSONObject response) throws JSONException {
        JSONObject Response = new JSONObject();
        try{
            Response.put("status", true);
            Response.put("people", assignPeopleRoutes());
        }catch(Exception e){
            Response.put("Exception",e.getMessage()) ;
            Response.put("status",false);
            return Response;
        }
        return Response;
    }

    /**
     * This function converts all the people data from the building into a string that is used by the simulation subsystem
     * it contains the people's initial location, as well as the path they need to follow to evacuate the building
     * @return a very long string formatted in a specific way
     * */
    private static String peopleLocations(){
//        building.emergency = true;
        Vector<Person> people = building.getPeople();
        String data = "";
        for (int i = 0; i < people.size(); i++) {
            Person c = people.get(i);
            if(c.getAssignedRoute()!=null) {
                double[] pos = c.getAssignedRoute().getGoal().coordinates;
                data += c.getName() + " *" ;
                data+= " "+c.floor+","+c.getPosition()[0]+","+c.getPosition()[1];
                if (i < people.size() - 1) {
                        data += " - ";

                }
            }
            else{
//                double[] pos = c.getAssignedRoute().getGoal().coordinates;
                double[] pos =building.getRoutes().get(0).getGoal().coordinates;
                data += c.getName() + " *" ;
                data+= " "+c.floor+","+c.getPosition()[0]+","+c.getPosition()[1];
                if (i < people.size() - 1) {
                       data += " - ";

                }
            }
//            System.out.println("PersonID: "+c.getPersonID()+" goal:"+ Arrays.toString(c.getAssignedRoute().getGoal().coordinates));
        }
        return data ;
    }

    /**
     * This function converts all the people data from the building into a string that is used by the simulation subsystem
     * it contains the people's initial location, as well as the path they need to follow to evacuate the building
     * @return a very long string formatted in a specific way
     * */
    private static JSONArray peopleLocationsJSON(){
//        building.emergency = true;
        Vector<Person> people = building.getPeople();
        JSONArray data =new JSONArray();
        for (int i = 0; i < people.size(); i++) {
            Person c = people.get(i);
            JSONObject person = new JSONObject();
            person.put("id",c.getPersonID());
            if(!c.deviceID.equals("")){
                person.put("deviceID",c.deviceID);
            }
            person.put("floor",c.floor);
            person.put("position",c.getPosition());
            data.put(person);
//            System.out.println("PersonID: "+c.getPersonID()+" goal:"+ Arrays.toString(c.getAssignedRoute().getGoal().coordinates));
        }
        return data ;
    }
    private static String unityFireLocations(){
        JSONArray fires = building.getFires();
        /**
         * Adding Rooms to the response
         * */
//        JSONArray fires = (JSONArray)last.get("rooms");
        String data ="";
        for (int i = 0; i < fires.length() ; i++) {
            JSONObject current = (JSONObject)  fires.get(i);
            data += current.getInt("floor") + " * ";
            JSONArray corners = (JSONArray)current.get("corners");
            for (int j = 0; j < corners.length(); j++) {
//                JSONArray c = (JSONArray)corners.get(j);
                double[] c = (double[])corners.get(j);
//                data += c.getDouble(0)+","+c.getDouble(1);
                data += c[0]+","+c[1];
                if(j < corners.length()-1)
                    data+=" % ";
            }
            if( i < fires.length() -1)
                data+= " - ";
        }
        return data;
    }
    /**
     * This function is used to remove a person from a building
     *
     * */
    private static JSONObject removePerson(JSONObject request){
        JSONObject response = new JSONObject();
        boolean status = false;
        long personID = -1;
        try{
            if(request.has("id"))
                personID = request.getLong("id");
            else if(request.has("device_id")){
                String deviceID = (String)request.get("device_id");
                Vector <Person> people = building.getPeople();
                for (Person p :people) {
                    if(p.deviceID.compareTo(deviceID)==0){
                        personID = p.getPersonID();
                    }
                }
            }
            else{
                throw new Exception("Please use either 'device_id' or 'id' when removing a person");
            }
            status = building.remove(personID );
        }
        catch (Exception e){
            JSONObject error = new JSONObject();
            error.put("message", "FATAL ERROR at RemovePerson: "+e.getMessage());
            error.put("status",false);
            System.out.println("FATAL ERROR at RemovePerson: "+e.getMessage());
            return error;
        }
        if(status){
            response.put("message", "Person has been removed from the building" );
            building.assignPeople();
        }else{
            response.put("message", "Person was not found");
        }
        response.put("status",status);
        return response;
    }
    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains the DEVICE that needs to be removed from the building
     * @return returns a JSON object that contains a success or fail message
     * */
    private static boolean unBindPerson(JSONObject request) {
        String deviceId = request.getString("device_id");
        return building.unBindPerson(deviceId);
    }

    private static void unityUpdatePeopleLocation(JSONObject request){
        String peopleData = (String)request.get("peopleLocations");
        if(peopleData=="")
            return;
        peopleData = peopleData.replaceAll(",",".");
        boolean add = true;
        String [] people = peopleData.split("-");
        for (String personData:people) {
            add = true;
            String [] person = personData.split("&");
            JSONObject PersonUpdate = new JSONObject();
            double [] pos = {Double.parseDouble(person[2]),Double.parseDouble(person[3])};
            for (Routes r:building.getRoutes()) {
                if(Arrays.equals(pos, r.getGoal().coordinates)){
                    add = false;
                }
            }
            int floor = Integer.parseInt(person[1]);
            long id = Long.parseLong(person[0]);
            PersonUpdate.put("floor",floor);
            JSONArray position = new JSONArray(pos);
            PersonUpdate.put("position",position);
            PersonUpdate.put("id",id);
            if(add)
                UpdatePersonLocation(PersonUpdate);
        }
    }
    /**
     * This function is used to update a person's location within a building it can either accept a person ID or a device ID that needs to be updated
     *
     * */
    private static JSONObject UpdatePersonLocation(JSONObject request){
        JSONObject response = new JSONObject();
        JSONArray pos = request.getJSONArray("position");
        int floor = request.getInt("floor");
        double [] newPosition = {pos.getDouble(0),pos.getDouble(1)};
        boolean status = false;
        try{
            long personID = request.getLong("id");
            status = building.updatePersonLocation(personID,floor,newPosition );
        }
        catch (Exception e1){
            try{
                if(floor>=building.getFloors().size()){
                    throw new IndexOutOfBoundsException("Please select a valid floor");
                }
                String deviceID = (String)request.get("device_id");
                status = building.updatePersonLocation(deviceID,floor,newPosition );
            }catch (Exception e2){
                JSONObject error = new JSONObject();
                error.put("message", "FATAL ERROR at UpdatePersonLocation: "+e2.getMessage());
                error.put("status",false);
                System.out.println("FATAL ERROR at UpdatePersonLocation: "+e2.getMessage());
                return error;
            }
        }
        if(status){
            response.put("message", "Person information has been updated" );
            building.assignPeople();
        }else{
            response.put("message", "Person was not found");
        }
        response.put("status",status);
        return response;
    }



}
