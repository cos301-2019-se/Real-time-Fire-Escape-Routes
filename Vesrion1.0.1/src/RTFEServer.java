import Builder.Building;
import Builder.BuildingManager;
import org.json.JSONObject;

public class RTFEServer extends Server {
    public RTFEServer(){};

    @Override
    void  start(){

        System.out.println("--------------------------");
        System.out.println("RTFE Server");
        System.out.println("--------------------------");


    }

    public void run(){
        System.out.println("--------------------------");
        System.out.println("RTFE Server2");
        System.out.println("--------------------------");
        try {
            JSONObject data = new JSONObject();
            BuildingManager BobTheBuilder = new BuildingManager(data);
            Building test = BobTheBuilder.construct();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
