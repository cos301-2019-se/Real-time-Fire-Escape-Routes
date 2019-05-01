import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import Builder.Building;
import Builder.BuildingManager;
import Builder.Person;
import Builder.Room;
import io.mappedbus.MappedBusWriter;
import org.json.*;

public class HTTPServer extends Server{
        static final File WEB_ROOT = new File("./html");
        static final String DEFAULT_FILE = "index.html";
        static final String FILE_NOT_FOUND = "404.html";
        static final String METHOD_NOT_SUPPORTED = "not_supported.html";
        static final int PORT = 8080;
        Database USERDB = new Database();
        // verbose mode
        //Enable this for addition console logs
        static final boolean verbose = true;

        public HTTPServer(){

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
        // return supported MIME Types
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
                // we manage our particular client connection
                BufferedReader in = null;
                PrintWriter out = null;
                BufferedOutputStream dataOut = null;
                String fileRequested = null;

                try {

                    // we read characters from the client via input stream on the socket
                    in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    // we get character output stream to client (for headers)
                    out = new PrintWriter(connect.getOutputStream());
                    // get binary output stream to client (for requested data)
                    dataOut = new BufferedOutputStream(connect.getOutputStream());

                    // get first line of the request from the client
                    while (!in.ready()){/*DONT REMOVE THIS it prevents a null pointer*/}
                    String input = in.readLine();
                    // we parse the request with a string tokenizer

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
//                            System.out.println(payload);
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
                                case "unity":{
                                    res = sendToRTFE(req);
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
                } finally {
                    try {
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
                }
            }
        }

        private JSONObject sendToRTFE(JSONObject req) {
            JSONObject Response = new JSONObject();
            try{
                /*
                MappedBusWriter writer = new MappedBusWriter("tmp/test", 100000L, 32);
                writer.open();
                String data = req.toString();
                writer.write(req.toString().getBytes(),0,data.length());
                writer.close();
                */
                test(req);
//                Thread.currentThread().notify();
//                notifyAll();
                boolean status= false;
                Response.put("status", status);
                Response.put("msg","Failed");
            }catch(Exception e){
                if(verbose) {
                    System.out.println("CRITICAL - UNITY FAIL");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace().toString());
                }
            }
            return Response;
        }

        private String getJSONStr(StringBuilder payload) {
            return payload.substring(payload.indexOf("{"));
        }

        private JSONObject login(String name, String password){

            JSONObject Response = new JSONObject();
            try{
                boolean status= USERDB.search(name, password);
                Response.put("status", status);
                Response.put("msg","Invalid user/pass");
            }catch(Exception e){
                if(verbose)
                    System.out.println("CRITICAL - LOGIN FAILED");
            }
            return Response;
        }

        private JSONObject register(String name,String password){
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
    }

    private void test(JSONObject data){

        BuildingManager BobTheBuilder = new BuildingManager(data);
        Building test = BobTheBuilder.construct();
//        test.AssignRoutes();


        Vector<Room> rooms =  test.getFloor(0).getRooms();

        Vector<Integer> assignedDoorsArray = rooms.get(0).assignPeople();
        Vector<Person> People = rooms.get(0).getPeopleInRoom();

        for (int i = 0; i < People.size(); i++) {
            String doorname = rooms.get(0).doors.get(assignedDoorsArray.get(0)).doorName;
            System.out.println("Person - " + People.get(i).getName()+" Assigned to door - "+doorname);
        }
        System.out.println("L 316");

    }

}
