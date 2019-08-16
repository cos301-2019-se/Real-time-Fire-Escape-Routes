import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestFunctions {

    public boolean SendRequest(JSONObject jsonData) throws Exception {

        System.out.println("Test->Server: "+jsonData.toString());
        URL obj = new URL("http://localhost:8080/database");
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
            System.out.println("Server->Test: "+response.toString());
            return true;
        } else {
            return false;
        }
    }
}
