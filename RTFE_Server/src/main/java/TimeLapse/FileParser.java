package TimeLapse;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class FileParser {
    private long updateInterval = 5000;
    String filename = "timelapse.txt";
    private long currentTime = 0;
    public FileParser(){}

    public void start(){
        File file = new File(filename);
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                String [] params =  line.split(" ");
                long time = Long.parseLong(params[0].split(":")[1]);
                if(currentTime == 0){
                    currentTime = time;
                }
                if(currentTime<time) {
                    TimeUnit.SECONDS.sleep((updateInterval / 1000));
                    currentTime = time;
                }else{
                    processPerson(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean processPerson(String line) throws Exception {
        String [] params =  line.split(" ");
        JSONObject data = new JSONObject();
        data.put("type","personUpdate");
        data.put("id",Integer.parseInt(params[1].split(":")[1]));
        data.put("floor",Integer.parseInt(params[4].split(":")[1]));
        String position = params[3].split(":")[1];
        position = position.replace("[","");
        position = position.replace("]","");
        double [] pos = {Double.parseDouble(position.split(",")[0]),Double.parseDouble(position.split(",")[1])};
        data.put("position",pos);

        boolean status = SendRequest(data);
        return false;
    }

    public boolean SendRequest(JSONObject jsonData) throws Exception {

        System.out.println("Timelapse->Server: "+jsonData.toString());
        URL obj = new URL("http://localhost:8080/building");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
        String POST_PARAMS = jsonData.toString();

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
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Server->Timelapse: "+response.toString());
            return true;
        } else {
            return false;
        }
    }
}
