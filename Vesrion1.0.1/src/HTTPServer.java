import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import Building.*;
import Builder.BuildingManager;
import org.json.*;

import static Web.WebAPI.*;


public class HTTPServer extends Server{
        static final File WEB_ROOT = new File("./html");
        static final String DEFAULT_FILE = "index.html";
        static final String FILE_NOT_FOUND = "404.html";
        static final String METHOD_NOT_SUPPORTED = "not_supported.html";
        static final int PORT = 8080;
        static final boolean verbose = false;
        private JSONObject lastBuild = null;

        public HTTPServer(Building b){
            super(b);
        }
        @Override
        void start(){}

        @Override
        public void run() {
            System.out.println("--------------------------");
            System.out.println("HTTP Server");
            System.out.println("--------------------------");
            try {
                ServerSocket serverConnect = new ServerSocket(PORT);
                System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
                while (true) {
                    HTTPClientConnection clientConnection = new HTTPClientConnection(serverConnect.accept());
                    if (verbose) {
                        System.out.println("Connecton opened. (" + new Date() + ")");
                    }
                    Thread thread = new Thread(clientConnection);
                    thread.start();
                }

            } catch (IOException e) {
                System.err.println("Server Connection error : " + e.getMessage());
            }
        }

        private byte[] readFileData(File file, int fileLength) throws IOException {
            FileInputStream fileIn = null;
            byte[] fileData = new byte[fileLength];

            try {
                fileIn = new FileInputStream(file);
                fileIn.read(fileData);
            } finally {
                if (fileIn != null)
                    fileIn.close();
            }

            return fileData;
        }
        private String getContentType(String fileRequested) {
            if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
                return "text/html";
            else
                return "text/plain";
        }
        private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
            File file = new File(WEB_ROOT, FILE_NOT_FOUND);
            int fileLength = (int) file.length();
            String content = "text/html";
            byte[] fileData = readFileData(file, fileLength);

            out.println("HTTP/1.1 404 File Not Found");
            out.println("Server: Java HTTP Server from SSaurel : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println(); // blank line between headers and content, very important !
            out.flush(); // flush character output stream buffer

            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();

            if (verbose) {
                System.out.println("File " + fileRequested + " not found");
            }
        }

    class HTTPClientConnection implements Runnable {
        private Socket connect;
        HTTPClientConnection(Socket c) {
            connect = c;
        }
        @Override
        public void run() {
            if(connect != null) {
                BufferedReader in = null;
                PrintWriter out = null;
                BufferedOutputStream dataOut = null;
                String fileRequested = null;
                try {
                    in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    out = new PrintWriter(connect.getOutputStream());
                    dataOut = new BufferedOutputStream(connect.getOutputStream());
                    String input="";
                    input = in.readLine();

                    StringTokenizer parse = new StringTokenizer(input);
                    String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                    // we get file requested
                    fileRequested = parse.nextToken().toLowerCase();

                    // we support only GET and HEAD methods, we check
                    if (!method.equals("GET")  && !method.equals("HEAD") && !method.equals("POST")) {
                        if (verbose) {
                            System.out.println("501 Not Implemented : " + method + " method.");
                        }
                        // we return the not supported file to the client
                        File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                        int fileLength = (int) file.length();
                        String contentMimeType = "text/html";
                        //read content to return to client
                        byte[] fileData = readFileData(file, fileLength);
                        // we send HTTP Headers with data to client
                        out.println("HTTP/1.1 501 Not Implemented");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + contentMimeType);
                        out.println("Content-length: " + fileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush(); // flush character output stream buffer
                        // file
                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();

                    }
                    else if(method.equals("POST")){

                        out.println("HTTP/1.1 200 OK");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + "application/json");
                        out.println(); // blank line between headers and content, very important !
                        out.flush();
                        /**Getting the req.body*/
                            StringBuilder payload = new StringBuilder();
                            while(in.ready()){
                                payload.append((char) in.read());
                            }
                            String test = getJSONStr(payload);
                            JSONObject req = new JSONObject(test);
                            if(verbose)
                                System.out.println("Client -> Server: "+ req.toString());


                        /**Processing the req.body*/
                            String reqType = (String)req.get("type");
                            JSONObject res = new JSONObject();
                            switch (reqType){
                                case "login":{
                                    res = login((String)req.get("name"), (String)req.get("pass"));
                                    break;
                                }
                                case "register":{
                                    res = register((String)req.get("name"), (String)req.get("pass"));
                                    break;
                                }
                                case "getBuildings":{
                                    res.put("msg",listDir());
                                    res.put("status",true);
                                    break;
                                }
                                case "buildingData":
                                case "build":{
                                    res = sendToRTFE(req);
                                    break;
                                }
                                case "getNumRooms":{
                                    res = getNumRooms(req);
                                    break;
                                }
                                default:{
                                    res.put("status",false);
                                    res.put("msg","cant identify type");
                                }
                            }
                        /**Responding to the client*/
                            if(verbose)
                                System.out.println("Server -> Client:"+ res.toString());
                            out.println(res.toString());
                            out.flush();

                    }
                    else { // GET or HEAD method

                        if (fileRequested.endsWith("/")) {
                            fileRequested += DEFAULT_FILE;
                        }
                        File file = new File(WEB_ROOT, fileRequested);
                        int fileLength = (int) file.length();
                        String content = getContentType(fileRequested);
                        if (method.equals("GET")) { // GET method so we return content
                            byte[] fileData = readFileData(file, fileLength);
                            // send HTTP Headers
                            out.println("HTTP/1.1 200 OK");
                            out.println("Date: " + new Date());
                            out.println("Content-type: " + content);
                            out.println("Content-length: " + fileLength);
                            out.println(); // blank line between headers and content, very important !
                            out.flush(); // flush character output stream buffer
                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();
                        }
                        if (verbose) {
                            System.out.println("File " + fileRequested + " of type " + content + " returned");
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    try {
                        fileNotFound(out, dataOut, fileRequested);
                    } catch (IOException ioe) {
                        System.err.println("Error with file not found exception : " + ioe.getMessage());
                    }
                } catch (IOException ioe) {
                    System.err.println("Server error : " + ioe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
//                    System.out
                }
                finally {
                    try {
                        out.flush();
                        dataOut.flush();
                        in.close();
                        out.close();
                        dataOut.close();
                        connect.close(); // we close socket connection
                    } catch (Exception e) {
                        System.err.println("Error closing stream : " + e.getMessage());
                    }

                    if (verbose) {
                        System.out.println("Connection closed.\n");
                    }
//                    connect.close();
                }
            }
        }

        private JSONObject getNumRooms(JSONObject req) {
            JSONObject Response = new JSONObject();
            try{
                Response.put("status", true);
                Response.put("msg","There are "+ building.getFloor(0).getRooms().size()+" rooms");
                boolean status= false;
            }catch(Exception e){
                if(verbose) {
                    System.out.println("CRITICAL - UNITY FAIL");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace().toString());
                }
            }
            return Response;
        }

        private JSONObject sendToRTFE(JSONObject req) throws Exception {
            JSONObject Response = new JSONObject();
            try{
                switch ( (String)req.get("type")){
                    case "build":{
                        Response.put("msg",build(req));
                        break;
                    }
                    case "buildingData":{
                        Response.put("msg",BuildingToUnityString(Response));
                    }

                }
                Response.put("status", true);
            }catch(Exception e){
                if(verbose) {
                    System.out.println("CRITICAL - UNITY FAIL");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace().toString());
                }
                Response.put("msg","Exception: "+e.getMessage());
                Response.put("status", false);
            }
            return Response;
        }

        private String BuildingToUnityString(JSONObject response)throws Exception {
            if (lastBuild == null)
                throw new Exception("Please build a building first") ;

            String responseMessage = "No people to add yet";
            response.put("numberFloors",building.getFloors().size());

            /**
             * Adding Rooms to the response
             * */
            JSONArray rooms = (JSONArray)lastBuild.get("rooms");
            String data ="";
            for (int i = 0; i < rooms.length() ; i++) {
                JSONObject current = (JSONObject) rooms.get(i);
                data += current.getInt("floor") + " * ";
                JSONArray corners = (JSONArray)current.get("corners");
                for (int j = 0; j < corners.length(); j++) {
                    JSONArray c = (JSONArray)corners.get(j);
                    data += c.getDouble(0)+","+c.getDouble(1);
                    if(j < corners.length()-1)
                        data+=" % ";
                }
                if( i < rooms.length() -1)
                    data+= " - ";
            }

            /**
             * Adding Halls to the response
             * */
            rooms = (JSONArray)lastBuild.get("halls");
            data += " - ";
            for (int i = 0; i < rooms.length() ; i++) {
                JSONObject current = (JSONObject) rooms.get(i);
                data += current.getInt("floor") + " * ";
                JSONArray corners = (JSONArray)current.get("corners");
                for (int j = 0; j < corners.length(); j++) {
                    JSONArray c = (JSONArray)corners.get(j);
                    data += c.getDouble(0)+","+c.getDouble(1);
                    if(j < corners.length()-1)
                        data+=" % ";
                }
                if( i < rooms.length() -1)
                    data+= " - ";
            }

            /**
             * Adding Floors to the response
             * */
            rooms = (JSONArray)lastBuild.get("floors");
            data += " - ";
            for (int i = 0; i < rooms.length() ; i++) {
                JSONObject current = (JSONObject) rooms.get(i);
                data += current.getInt("floor") + " * ";
                JSONArray corners = (JSONArray)current.get("corners");
                for (int j = 0; j < corners.length(); j++) {
                    JSONArray c = (JSONArray)corners.get(j);
                    data += c.getDouble(0)+","+c.getDouble(1);
                    if(j < corners.length()-1)
                        data+=" % ";
                }
                if( i < rooms.length() -1)
                    data+= " - ";
            }
            response.put("rooms",data);

            /**
             * Adding Doors to the response
             * */
            data = "";
            JSONArray doors = (JSONArray)lastBuild.get("doors");
            for (int i = 0; i < doors.length() ; i++) {
                JSONObject current = (JSONObject) doors.get(i);
                data += current.getInt("floor") + " * ";
                data += (String)current.get("type") + " * ";
                JSONArray pos = (JSONArray)current.get("position");
                data += pos.getDouble(0)+","+pos.getDouble(1);

                if( i < rooms.length() -1)
                    data+= " - ";
            }
            response.put("doors",data);



            return responseMessage;
        }

        private String getJSONStr(StringBuilder payload) {
            return payload.substring(payload.indexOf("{"));
        }



    }
    private String clearPeople(){
            return"";
    }

    private String build(JSONObject data){
        String temp="Building built successfully";
        lastBuild = data;
        try {
        BuildingManager BobTheBuilder = new BuildingManager(data);
        building = BobTheBuilder.construct();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return temp;
    }

}