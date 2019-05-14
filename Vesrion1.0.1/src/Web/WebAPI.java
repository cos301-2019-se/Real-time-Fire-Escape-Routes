package Web;
import org.json.JSONObject;

/**
 * @Description: The purpose of this Class is to simplify the HTTP server by
 * seperating functions related ONLY to the web related content here so that
 * it can be easily called by the HTTP server
 * */
public class WebAPI {

    private static boolean verbose = false; //USED for debugging purposes
    private static Database USERDB = new Database();

    public static JSONObject register(String name, String password){
        JSONObject Response = new JSONObject();
        try{
            boolean exist = USERDB.search(name, "");
            if(exist){
                Response.put("status", false);
                Response.put("msg","User already Exists");
            }else{
                USERDB.write(name, password);
                Response.put("status", true);
                Response.put("msg","User Successfully created");

            }
        }catch (Exception e){
            if(verbose)
                System.out.println("CRITICAL - REGISTER FAILED");
        }

        return Response;
    }
    public static JSONObject login(String name, String password){

        JSONObject Response = new JSONObject();
        try{
            boolean status= USERDB.search(name, password);
            Response.put("status", status);
            if(status)
                Response.put("msg","Login success");
            else
                Response.put("msg","Invalid user/pass");
        }catch(Exception e){
            if(verbose)
                System.out.println("CRITICAL - LOGIN FAILED");
        }
        return Response;
    }
}
