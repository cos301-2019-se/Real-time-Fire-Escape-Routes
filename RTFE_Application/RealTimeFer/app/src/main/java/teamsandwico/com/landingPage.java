package teamsandwico.com;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static teamsandwico.com.FirewatchNotification.CHANNEL_ID;

public class landingPage extends AppCompatActivity {
    //note: activity elements
    private RelativeLayout layout_login;
    private TextView text_result, text_update, text_simulation, text_status;
    private EditText input_name;
    private Button button_parse;
    private Button button_check;
    private pl.droidsonroids.gif.GifImageView image_loading;
    //note: estimote variable objects
    private String estimoteID = "firewatch-gxo";
    private String estimoteToken = "3e5682fc68eb512d031fd3116d102b13";
    private EstimoteCloudCredentials cc = null;
    private ProximityObserver observer = null;
    //note: regular variable objects
    private int screentap = 0;
    private int delay = 5000;
    private OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler();
    private String defaultHost = "192.168.137.1:8080";
//    private String defaultHost = "10.0.0.5:8080";
    private String[] position = new String[2], status = new String[2], route = new String[2];
    private String hostIP;
    private int currentIP = 1;
    private String currentHost;
    public boolean simAlarm = false;
    public boolean login = false;
    public WifiManager wifiManager;
    public JSONArray sensor;
    //note: notification
    private NotificationManagerCompat noti_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        //note: initiating activity elements
        layout_login = findViewById(R.id.layout_login);
        text_result = findViewById(R.id.text_result);
        text_update = findViewById(R.id.text_update);
        text_simulation = findViewById(R.id.text_simulation);
        text_status = findViewById(R.id.text_status);
        input_name = findViewById(R.id.input_name);
        button_parse = findViewById(R.id.button_parse);
        button_check = findViewById(R.id.button_check);
        image_loading = findViewById(R.id.image_loading);

        text_result.setMovementMethod(new ScrollingMovementMethod());
        text_update.setVisibility(View.GONE);
        text_status.setVisibility(View.GONE);
        //note: initiating notification
        noti_manager = NotificationManagerCompat.from(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        String[] temp = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()).split("\\.");
        hostIP = temp[0]+"."+temp[1]+"."+temp[2];

        if (!wifiManager.isWifiEnabled()){
            Toast.makeText(this, "Wifi is disabled, please enable it", Toast.LENGTH_SHORT).show();
        }
        //note: initiating estimote required elements
//        cc = new EstimoteCloudCredentials(estimoteID, estimoteToken);
//        observer = new ProximityObserverBuilder(getApplicationContext(), cc)
//                .withBalancedPowerMode()
//                .onError(new Function1<Throwable, Unit>() {
//                    @Override
//                    public Unit invoke(Throwable e) {
//                        text_result.append("Error on estimote: "+ e.getMessage());
//                        return null;
//                    }
//                })
//                .build();

        button_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input_name.getText().equals("")) {
                    loginState("disable");
                    button_parse.setVisibility(View.GONE);
                    image_loading.setVisibility(View.VISIBLE);
                    new Login().execute();
                }
            }
        });

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printStatus();
            }
        });
    }

    public String getCurrentHost(){
        return currentHost;
    }

    private String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        String formatter = new SimpleDateFormat("E, MMM dd HH:mm:ss").format(currentTime);
        return formatter;
    }

    private String getNextIpAddress(){
        currentHost = hostIP + "." + currentIP + ":8080";
        currentIP++;
        text_result.append(currentHost+"\n");
        return currentHost;
    }
    /**
     * function will return the devices unique id of the current device
     * @return string: devices unique id
     */
    private String getUniqueId(){
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void getWifi(){
        Toast.makeText(this, "Getting wifi...", Toast.LENGTH_SHORT).show();
        sensor = new JSONArray();
        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult result: results){
//            int level = WifiManager.calculateSignalLevel(result.level, 100);

            int level = Math.abs(result.level);

            if (level <= 50) {
                JSONObject temp = new JSONObject();
                try{
                    temp.put("bssid", result.BSSID);
                    temp.put("level", level);
                    sensor.put(temp);
                }catch(Exception e){
                    text_result.append("Error: " + e.getMessage());
                }
            }
        }
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        printStatus();
    }
    /**
     * function identifies the color from the route
     * @param route: string of the route value
     * @return an hexadecimal color value
     */
    private int identifyColor(String route){
        switch(route.replaceAll("\\s+","")){
            case "0": return getResources().getColor(R.color.Orange);
            case "1": return getResources().getColor(R.color.Green);
            case "2": return getResources().getColor(R.color.Blue);
            case "3": return getResources().getColor(R.color.Red);
            case "4": return getResources().getColor(R.color.Pink);
            case "5": return getResources().getColor(R.color.Purple);
            default: return getResources().getColor(R.color.White);
        }
    }

    private void loginState(String state){
        if (state.equals("disable")) {
            input_name.setEnabled(false);
        }else if (state.equals("enable")){
            input_name.setEnabled(true);
        }
    }

    private void loginSuccess(){
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            getWifi();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            getWifi();
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            getWifi();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            getWifi();
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
        }
    }
    /**
     * function will print json object to the debug terminal
     * @param string: is a string of the json object
     */
    private void printJson(String string){
        try {
            JSONObject json = new JSONObject(string);
            text_result.append("Status: " + json.getString("status") + "\n");
            text_result.append("Msg: " + json.getString("msg") + "\n");
        }catch (Exception e){
            text_result.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void printStatus(){
        new StatusCheck().execute();
        new SimulationStatusCheck().execute();
        int index = 0 ; //change back to 0 and uncomment from 270 - 273
        if (simAlarm && status[0] != null && status[0].contains("safe")){
            index = 1;
            text_simulation.setVisibility(View.VISIBLE);
        }else text_simulation.setVisibility(View.GONE);

        if (position[index] != null && status[index] != null && route[index] != null) {
            if (route[index] != null && !route[index].contains("safe")) {
                View main = findViewById(R.id.relativeLayout);
                main.setBackgroundColor(identifyColor(route[index]));
                text_update.setTextColor(getResources().getColor(R.color.White));
                text_status.setTextColor(getResources().getColor(R.color.White));
            }

            if (index == 0) {
                String msg = "Please Take route " + route[index];
                sendFirewatchNotification(status[index], msg, identifyColor(route[index]));
            }else
                noti_manager.cancelAll();

//            String updateMsg =
//                    "Time: " + getCurrentTime() + "\n" +
//                            "Status: " + status[index] + "\n" +
//                            "Position: " + position[index] + "\n" +
//                            "Route: " + route[index] + "\n";

            String updateMsg = getCurrentTime() + "\n" +
                    "please follow route";

            String updateStatus = route[index];

            text_update.setText(updateMsg);
            text_status.setText(updateStatus);
            if (text_update.getVisibility() == View.GONE)
                text_update.setVisibility(View.VISIBLE);

            if (text_status.getVisibility() == View.GONE)
                text_status.setVisibility(View.VISIBLE);
        } else if (status[index] != null){
            //note: display when status is safe
            View main = findViewById(R.id.relativeLayout);
            main.setBackgroundColor(identifyColor("-1"));
            text_update.setTextColor(getResources().getColor(R.color.Black));
            text_status.setTextColor(getResources().getColor(R.color.Black));

            noti_manager.cancelAll();

//            String updateMsg =
//                    "Time: " + getCurrentTime() + "\n" +
//                            "Status: " + status[index] + "\n";

            String updateMsg = getCurrentTime() + "\n" +
                    "status of the building";

            String updateStatus = status[index];

            text_update.setText(updateMsg);
            text_status.setText(updateStatus);
            if (text_update.getVisibility() == View.GONE)
                text_update.setVisibility(View.VISIBLE);

            if (text_status.getVisibility() == View.GONE)
                text_status.setVisibility(View.VISIBLE);
        }else{
            //note: display when status equals null or no building is detected
            View main = findViewById(R.id.relativeLayout);
            main.setBackgroundColor(identifyColor("-1"));
            text_update.setTextColor(getResources().getColor(R.color.Black));
            text_status.setTextColor(getResources().getColor(R.color.White));
            text_update.setText("No building is detected");
            text_simulation.setVisibility(View.GONE);


            if (text_update.getVisibility() == View.GONE)
                text_update.setVisibility(View.VISIBLE);

            if (text_status.getVisibility() == View.GONE)
                text_status.setVisibility(View.VISIBLE);
        }
    }
    /**
     * removes interface of the log in
     */
    private void removeLogin(){
        layout_login.setVisibility(View.GONE);
    }
    /**
     * function is used to detect screen tap, in order to activate developer terminal
     * @param v
     */
    public void screenTapped(View v){
        screentap++;
        if (screentap == 5 && text_result.getVisibility() == View.GONE) {
            text_result.setVisibility(View.VISIBLE);

            text_result.append("Developer Terminal:\n");
            text_result.append("Android Id: " + getUniqueId() + "\n");
        }else if (screentap > 9 && text_result.getVisibility() == View.VISIBLE){
            screentap = 0;
            text_result.setVisibility(View.GONE);

            text_result.setText("");
        }
    }

    public void sendFirewatchNotification(String heading, String body, int color){
        Intent intent = new Intent(this, landingPage.class);
        PendingIntent landingPageIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.fire)
                .setContentTitle(heading)
                .setContentText(body)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(landingPageIntent)
                .build();

        noti_manager.notify(1, notification);
    }

    public void showToast(String text, int image){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));

        TextView text_toast = layout.findViewById(R.id.toast_text);
        ImageView image_toast = layout.findViewById(R.id.toast_image);

        text_toast.setText(text);
        image_toast.setImageResource(image);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();

    }

    private void stateOfActivity(String state){
        switch(state){
            case "logged":
                button_check.setVisibility(View.VISIBLE);
                break;
        }
    }
    /**
     * class contains async call to service in order to login
     */
    public class Login extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
             try{
                 String json = "{'type': validateDeviceId," +
                         " 'email' : '"+input_name.getText().toString()+"'," +
                         " 'deviceID' : '"+getUniqueId()+"'}";

                 final MediaType JSON
                         = MediaType.parse("application/json; charset=utf-8");

                 RequestBody body = RequestBody.create(JSON, json);

//                 String url = getNextIpAddress();
                 String url = defaultHost;

                 Request request = new Request.Builder()
                         .url("http://" +
                                 url +
                                 "/database")
                         .post(body)
                         .build();

                 Response resp = client.newCall(request).execute();
                 return resp.body().string();
             }catch(Exception e){
                 return "Error on connection: " + e.getMessage();
//                 if (getCurrentHost() == defaultHost)
//                    return "Error on connection: " + e.getMessage();
//                 else
//                     return doInBackground();
             }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            text_result.append(s);
            printJson(s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getString("status").equals("true")){
                    login = true;
                    image_loading.setVisibility(View.GONE);
                    removeLogin();
                    stateOfActivity("logged");
                    loginSuccess();
                }else{
                    throw new Exception("Invalid credentials");
                }
            }catch(Exception e){
                String msg = "Could not sign in!";
                int img = R.drawable.ic_toast_error;
                showToast(msg, img);
                loginState("enable");
                image_loading.setVisibility(View.GONE);
                button_parse.setVisibility(View.VISIBLE);
                text_result.append("Error on login: " + e.getMessage());
            }


        }
    }
    /**
     * class contains async call to service in order to call status check
     */
    public class StatusCheck extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            try{
                String json = "{'type': 'personInfo'," +
                        " 'name' : '"+input_name.getText().toString() + "'," +
                        " 'device_id' : '"+ getUniqueId() + "'," +
                        (sensor != null? " 'sensors' : " + sensor.toString(): "") +
                        "}";

                System.out.println(json);

                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, json);

                String url = defaultHost;

                Request request = new Request.Builder()
                        .url("http://" +
                                url +
                                "/building")
                        .post(body)
                        .build();

                Response resp = client.newCall(request).execute();
                return resp.body().string();
            }catch(Exception e){
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                String[] message = json.getString("message").split("-");
                position[0] = null;
                status[0] = null;
                route[0] = null;
                for (String item: message) {
                    String[] value = item.split(":");
                    switch (value[0].replaceAll("\\s+","")){
                        case "ID":
                            text_result.append("ID: " + value[1] + "\n");
                            break;
                        case "Position":
                            position[0] = value[1];
                            text_result.append("Position: " + position[0] + "\n");
                            break;
                        case "Status":
                            status[0] = value[1];
                            text_result.append("Status: " + status[0] + "\n");
                            break;
                        case "AssignedRouteID":
                            route[0] = value[1];
                            text_result.append("Route: " + route[0] + "\n");
                            break;
                    }
                }
            }catch(Exception e){
                text_result.append("Error on function Post: " + e.getMessage() + "\n");
            }
        }
    }

    public class SimulationStatusCheck extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params){
            try{
                String json = "{'type': 'personInfo'," +
                        " 'mode' : 'simulation',"+
                        " 'name' : '"+input_name.getText().toString()+"'," +
                        " 'device_id' : '"+ getUniqueId() +"'}";

                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, json);

                String url = defaultHost;

                Request request = new Request.Builder()
                        .url("http://" +
                                url +
                                "/building")
                        .post(body)
                        .build();

                Response resp = client.newCall(request).execute();
                return resp.body().string();
            }catch(Exception e){
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                String[] message = json.getString("message").split("-");
                position[1] = null;
                status[1] = null;
                route[1] = null;
                for (String item: message) {
                    String[] value = item.split(":");
                    switch (value[0].replaceAll("\\s+","")){
                        case "ID":
                            text_result.append("Simulated ID: " + value[1] + "\n");
                            break;
                        case "Position":
                            position[1] = value[1];
                            text_result.append("Simulated Position: " + position[1] + "\n");
                            break;
                        case "Status":
                            status[1] = value[1];
                            simAlarm = !status[1].contains("safe");
                            text_result.append("Simulated Status: " + status[1] + "\n");
                            break;
                        case "AssignedRouteID":
                            route[1] = value[1];
                            text_result.append("Simulated Route: " + route[1] + "\n");
                            break;
                    }
                }
            }catch(Exception e){
                text_result.append("Error on function Post: " + e.getMessage() + "\n");
            }
        }
    }

}
