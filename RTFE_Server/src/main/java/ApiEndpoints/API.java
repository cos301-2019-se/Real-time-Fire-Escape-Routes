package ApiEndpoints;

import org.json.JSONObject;
import Building.Building;
/**
 * Abstract Class that will be used by subclasses that makes use of a dedicated API endpoint
 * */
public abstract class API {
    public static Building building;
    public static JSONObject lastbuild;
    /**
     * This function will be used to process the request handed over to the API
     * @param request: Contains the JSON data that was sent to the server
     * @return returns a JSON object with the appropriate response messages for the initial request
     * */
    public static JSONObject handleRequest(JSONObject request) throws Exception {
        return null;
    }
}
