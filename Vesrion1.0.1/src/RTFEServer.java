import Builder.Building;
import Builder.BuildingManager;
import io.mappedbus.MappedBusMessage;
import io.mappedbus.MappedBusReader;
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
        System.out.println("RTFE Server");
        System.out.println("--------------------------");
        try {
            MappedBusReader reader = new MappedBusReader("tmp/test", 100000L, 32);
            reader.open();
            MappedBusMessage message = null;
//            Thread.currentThread().wait();
            while (reader.next()) {
//                if (reader.next()) {
                    boolean recovered = reader.hasRecovered();
                    int type = reader.readType();

                    reader.readMessage(message);
                    System.out.println("Read: " + message + ", hasRecovered=" + recovered);
//                }
            }
            JSONObject data = new JSONObject();
            reader.close();
            BuildingManager BobTheBuilder = new BuildingManager(data);
            Building test = BobTheBuilder.construct();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
