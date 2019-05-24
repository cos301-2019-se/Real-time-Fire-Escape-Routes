import Building.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main implements Runnable {

    Server myserver;
    @Override
    public void run() {
        myserver.start();
    }
    public Main(Server s) {
        myserver = s;
    }

    public static void main(String[] args){
        try
        {
            Building Current = null;
//            listDir();
            //##############################
            //#         HTTP Server        #
            //##############################

            Thread thread = new Thread(new HTTPServer(Current));
            thread.start();
            //##############################
            //#            RTFE            #
            //##############################
            loadDefaultBuilding();
/*
            Thread thread1 = new Thread( new RTFEServer());

            thread1.start();
            thread1.wait();
  */

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    private static void loadDefaultBuilding() throws IOException {
        URL obj = new URL("http://localhost:8080/buildingGeneration");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
        String POST_PARAMS = "{\"type\":\"build\",\"floors\":[{\"floor\":0,\"corners\":[[0,0],[0,5.8],[4,10],[24,10],[24,1.7],[17,1.7],[17,0]]}],\"halls\":[{\"floor\":0,\"corners\":[[0,4.5],[0,5.8],[24,5.8],[24,4.5],[13.3,4.5],[13.3,0],[12,0],[12,4.5]]}],\"rooms\":[{\"floor\":0,\"corners\":[[0,0],[0,4.5],[4,4.5],[4,0]]},{\"floor\":0,\"corners\":[[4,0],[4,4.5],[8,4.5],[8,0]]},{\"floor\":0,\"corners\":[[8,0],[8,4.5],[12,4.5],[12,0]]},{\"floor\":0,\"corners\":[[0,5.8],[4,5.8],[4,10]]},{\"floor\":0,\"corners\":[[4,5.8],[8,5.8],[8,10],[4,10]]},{\"floor\":0,\"corners\":[[8,5.8],[12,5.8],[12,10],[8,10]]},{\"floor\":0,\"corners\":[[12,5.8],[16,5.8],[16,10],[12,10]]},{\"floor\":0,\"corners\":[[16,5.8],[20,5.8],[20,10],[16,10]]},{\"floor\":0,\"corners\":[[20,5.8],[24,5.8],[24,10],[20,10]]},{\"floor\":0,\"corners\":[[13.3,0],[17,0],[17,1.7],[13.3,1.7]]},{\"floor\":0,\"corners\":[[13.3,1.7],[13.3,4.5],[17,4.5],[17,1.7]]},{\"floor\":0,\"corners\":[[17,1.7],[17,4.5],[24,4.5],[24,1.7]]}],\"doors\":[{\"floor\":0,\"type\":\"buildingExit\",\"position\":[12.6,0]},{\"floor\":0,\"type\":\"buildingExit\",\"position\":[0,5.2]},{\"floor\":0,\"type\":\"buildingExit\",\"position\":[24,5.2]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[2,4.5]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[6,4.5]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[10,4.5]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[2,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[6,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[10,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[14,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[18,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[22,5.8]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[13.3,0.875]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[13.3,3.1]},{\"floor\":0,\"type\":\"singleDoor\",\"position\":[19,4.5]}],\"people\":[{\"id\":0,\"floor\":0,\"position\":[2,3]},{\"id\":1,\"floor\":0,\"position\":[5,8]},{\"id\":2,\"floor\":0,\"position\":[3,11]},{\"id\":3,\"floor\":0,\"position\":[7,12]},{\"id\":4,\"floor\":0,\"position\":[3,13]},{\"id\":5,\"floor\":0,\"position\":[3,14]},{\"id\":6,\"floor\":0,\"position\":[1,6]},{\"id\":7,\"floor\":0,\"position\":[2,11]},{\"id\":8,\"floor\":0,\"position\":[1,15]},{\"id\":9,\"floor\":0,\"position\":[2,10]},{\"id\":10,\"floor\":0,\"position\":[4,20]},{\"id\":11,\"floor\":0,\"position\":[2,6]},{\"id\":12,\"floor\":0,\"position\":[9,3]},{\"id\":13,\"floor\":0,\"position\":[1,1]},{\"id\":14,\"floor\":0,\"position\":[6,3]},{\"id\":15,\"floor\":0,\"position\":[5,22]},{\"id\":16,\"floor\":0,\"position\":[8,8]},{\"id\":17,\"floor\":0,\"position\":[8,10]},{\"id\":18,\"floor\":0,\"position\":[3,20]},{\"id\":19,\"floor\":0,\"position\":[3,21]},{\"id\":20,\"floor\":0,\"position\":[1,1]},{\"id\":21,\"floor\":0,\"position\":[6,1]},{\"id\":22,\"floor\":0,\"position\":[9,19]},{\"id\":23,\"floor\":0,\"position\":[6,6]},{\"id\":24,\"floor\":0,\"position\":[8,14]},{\"id\":25,\"floor\":0,\"position\":[7,2]},{\"id\":26,\"floor\":0,\"position\":[2,8]},{\"id\":27,\"floor\":0,\"position\":[2,9]},{\"id\":28,\"floor\":0,\"position\":[2,5]},{\"id\":29,\"floor\":0,\"position\":[8,21]},{\"id\":30,\"floor\":0,\"position\":[5,19]},{\"id\":31,\"floor\":0,\"position\":[1,2]},{\"id\":32,\"floor\":0,\"position\":[5,14]},{\"id\":33,\"floor\":0,\"position\":[2,14]},{\"id\":34,\"floor\":0,\"position\":[6,22]},{\"id\":35,\"floor\":0,\"position\":[9,18]},{\"id\":36,\"floor\":0,\"position\":[1,17]}]}";
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

}
