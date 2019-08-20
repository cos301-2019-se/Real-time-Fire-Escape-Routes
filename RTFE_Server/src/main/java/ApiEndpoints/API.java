package ApiEndpoints;

import Building.Building;
import org.json.JSONObject;

/**
 * Abstract Class that will be used by subclasses that makes use of a dedicated API endpoint
 * */
public abstract class API {

    /** @brief: buildings.get(0) = Live view, buildings.get(1) = Simulation view */
    public static Building [] buildings = new Building[2];

    /** @brief: lastbuild.get(0) = Live view, lastbuild.get(1) = Simulation view */
    public static JSONObject [] lastbuild =  new JSONObject[2];

//    protected Building building;

    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains the JSON data that was sent to the server
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    public static JSONObject handleRequest(JSONObject request, Building building) throws Exception {
        return null;
    }
    /**
     * ChooseBuilding will take the request object and return the appropriate Building object the request is intended for.
     * @param request: The initial request that was sent to the server, Needs to have a "mode" field if simulation wants to be accessed.
     * */
    static Building chooseBuilding(JSONObject request) throws Exception {
        Building building;
        if(request.has("mode")){
            if(request.getString("mode").compareToIgnoreCase("simulation") == 0){
                try{
                    building = buildings[1];
                }catch (Exception e){
                    throw new Exception("No simulation active");
                }
            }
            else{
                building = buildings[0];
            }
        }
        else{
            try{
                building = buildings[0];
            }catch (Exception e){
                throw new Exception("No Live building active");
            }
        }
        return building;
    }

    /**
     * ChooseLastBuild will take the request object and return the appropriate Building object the request is intended for.
     * @param request: The initial request that was sent to the server, Needs to have a "mode" field if simulation wants to be accessed.
     * */
    static JSONObject chooseLastBuild(JSONObject request) throws Exception {
        JSONObject building;
        if(request.has("mode")){
            if(request.getString("mode").compareToIgnoreCase("simulation") == 0){
                try{
                    building = lastbuild[1];
                }catch (Exception e){
                    throw new Exception("No simulation active");
                }
            }
            else{
                building = lastbuild[0];
            }
        }
        else{
            try{
                building = lastbuild[0];
            }catch (Exception e){
                throw new Exception("No Live building active");
            }
        }
        return building;
    }
}
