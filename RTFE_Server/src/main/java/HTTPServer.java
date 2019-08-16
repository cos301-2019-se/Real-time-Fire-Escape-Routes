/**
 * @file HTTPServer.java
 * @brief This file contains the main Server code which handles the incoming server requests
 *
 * @author Louw, Matthew Jason
 * @author Bresler,  Mathilda Anna
 * @author Braak, Pieter Albert
 * @author Reva, Kateryna
 * @author  Li, Xiao Jian
 *
 * @date 28/05/2019
 */

import ApiEndpoints.BuildingAPI;
import ApiEndpoints.BuildingGenerationAPI;
import ApiEndpoints.WebAPI;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


public class HTTPServer extends Server{
        static final File WEB_ROOT = new File("./html");
        static final String DEFAULT_FILE = "index.html";
        static final String FILE_NOT_FOUND = "404.html";
        static final String METHOD_NOT_SUPPORTED = "not_supported.html";
        static final int PORT = 8080;
        static final boolean verbose = true;

        public HTTPServer(){
//            super();
        }
        @Override
        void start(){}

    /**
     * Run function
     * @brief This function opens a new socket and starts a new thread for the server to run
     *
     ** The run procedure. It can do the following:
     *   - wait for incoming requests
     *   - throw an exception if a socket cannot be opened
     * @date 28/05/2019
     */
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

    /**
     * readFileData function
     * @brief This function takes in a file opbject and it's length and returns a bytestream of the file content
     * @param file
     * @param fileLength
     * @return a bytestream containing the file data
     * @date 28/05/2019
     */
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

    /**
     * getContentType function
     * @brief This function determines the expected format of the data and returns the expected format in
     * a standard format
     * @param fileRequested describing the expected code format
     * @return a String in the expected MEME type format
     * @date 28/05/2019
     */
        private String getContentType(String fileRequested) {

            if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
                return "text/html";
            if(fileRequested.endsWith(".css"))
                return "text/css";
            if(fileRequested.endsWith(".js"))
                return "text/javascript";
            return "text/plain";
        }

    /**
     * getContentType function
     * @brief This function determines the expected format of the data and returns the expected format in
     * a standard format
     * @param request describing the expected code format
     * @return a String in the expected format
     * @date 28/05/2019
     */
        private String getContentType(StringBuilder request){
            String [] reqBody = request.toString().split("\n");
            return reqBody[0].split(" ")[1];
        }

    /**
     * fileNotFound function
     * @brief
     *
     * @param out as the printWriter to be used in the function
     * @param dataOut as the OutputStream used in the function
     * @param fileRequested containing the requested file name
     * @return no return value
     * @date 28/05/2019
     */
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
    /**
     * @class HTTPClientConnection
     * @brief Handles multiple threads and redirects requests to the appropriate classes
     *
     * @date 28/05/2019
     */
    class HTTPClientConnection implements Runnable {
        private Socket connect;
        HTTPClientConnection(Socket c) {
            connect = c;
        }

        /**
         * run function
         * @brief handles multiple clients as threads and redirects requests
         *
         * @return no return value
         * @date 28/05/2019
         */
        @Override
        public void run() {
            if(connect != null) {
                BufferedReader in = null;
//                InputStream in = null;
                PrintWriter out = null;
                BufferedOutputStream dataOut = null;
                String fileRequested = null;
                try {
                    in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    out = new PrintWriter(connect.getOutputStream());
                    dataOut = new BufferedOutputStream(connect.getOutputStream());
                    String input="";
                    input = in.readLine();
                    if(input == null){
                        throw new Exception("null problem");
                    }

                    StringTokenizer parse = new StringTokenizer(input);

                    String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                    // we get file requested
                    fileRequested = parse.nextToken().toLowerCase();

                    // we support only GET and HEAD methods, we check
                    if (!method.equals("GET")  && !method.equals("HEAD") && !method.equals("POST") && !method.equals("OPTIONS")) {
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
                    else if(method.equals("POST") || method.equals("OPTIONS")){
                        out.println("HTTP/1.1 200 OK");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + "application/json");
                        out.println(); // blank line between headers and content, very important !
                        out.flush();
                        JSONObject response = new JSONObject();
                        try {
                            
                            /**Getting the req.body*/
                                StringBuilder payload = new StringBuilder();
                                while(in.ready()){
                                    payload.append((char) in.read());
                                }
                                String type = getContentType(payload);
                                if (verbose)
                                    System.out.println("TYPE -> "+type);
                                JSONObject req = new JSONObject();

                                switch (type){
                                    case "multipart/form-data;":{
                                        req = getFormData(payload);
                                        break;
                                    }
                                    default:{
                                        String test = getJSONStr(payload);
                                        req = new JSONObject(test);
                                        break;
                                    }
                                }
                                if(verbose) {
                                    System.out.println("Client -> Server: "+ req.toString());
                                }

                            /** Determining the API endpoint requested */
                            System.out.println(fileRequested.toString());
                            String endPoint = fileRequested.toString();
                            endPoint = endPoint.substring(1);
                            endPoint = endPoint.toLowerCase();
                            switch (endPoint){
                                case "database":
                                    response = WebAPI.handleRequest(req);
                                    break;
                                case "buildinggeneration":
                                    response = BuildingGenerationAPI.handleRequest(req);
                                    break;
                                case "building":
                                    response = BuildingAPI.handleRequest(req);
                                    break;
                                default:
                                    response = new JSONObject();
                                    response.put("status",false);
                                    response.put("msg","invalid endpoint");
                            }
                        }
                        catch (Exception e){
                            response.put("status","failed");
                            response.put("message",e.getMessage());
                        }
                        if(verbose) {
                            System.out.println("Connecton opened. (" + new Date() + ")");
                            System.out.println("Server -> Client:" + response.toString());
                        }
                        out.println(response.toString());
                        out.flush();
                    }
                    else { // GET or HEAD method

                        if (fileRequested.endsWith("/")) {
                            fileRequested += DEFAULT_FILE;

                        }
                        fileRequested = fileRequested.replace("%20"," ");
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

                            System.out.println("File " + fileRequested + " of type " + content + " returned method: "+method);
                        }
                    }
                } catch (FileNotFoundException fnfe) {
                    try {
                        System.out.println("Attemping to send "+fileRequested);
                        fileNotFound(out, dataOut, fileRequested);
                    } catch (IOException ioe) {
                        System.err.println("Error with file not found exception : " + ioe.getMessage());
                    }
                } catch (IOException ioe) {
                    System.err.println("Server error : " + ioe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    out.println("{\"status\":\"failed\"}");
                    out.println("{\"message\":\""+e.getMessage()+"\"}");
                    out.flush();
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
                }
            }
        }
        
        private String getJSONStr(StringBuilder payload)  {
            try {
                return payload.substring(payload.indexOf("{"));
            }
            catch (Exception e){
                System.out.println("Something went wrong parsing the payload: "+payload);
            }
            return "{\"type\":\"assignPeople\"}";
        }
    }

    /**
     * getFormData function
     * @brief Converts the provided payload into a format used by the rest of the system
     *
     * @param payload
     * @return a JSONObject used by the rest of the system to execute the requests
     * @date 28/05/2019
     */
    private JSONObject getFormData(StringBuilder payload) throws Exception {
        JSONObject request = new JSONObject();
        String data = payload.toString();
        String [] parsedData = data.split("Content-Disposition: ");
        try {

            JSONObject buildingData = new JSONObject(parsedData[1].substring(parsedData[1].indexOf("{"), parsedData[1].indexOf("-------")));
            request.put("file",buildingData);
            String BuildingImage = parsedData[4].substring(parsedData[4].indexOf("Content-Type: image/jpeg")+28, parsedData[4].indexOf("-------"));
            request.put("img",BuildingImage);
        }
        catch (Exception e){
            throw new Exception("Building json is invalid");
        }
        String Type = parsedData[2].split("\n")[2];
            Type = Type.substring(0, Type.length() - 1);
            String BuildingName = parsedData[3].split("\n")[2];
            BuildingName = BuildingName.substring(0, BuildingName.length() - 1);

            String location = parsedData[6].split("\n")[2];
            location = location.substring(0, location.length() - 1);

            String numFloors = parsedData[5].split("\n")[2];
            numFloors = numFloors.substring(0, numFloors.length() - 1);
        request.put("type",Type);
        request.put("num_floors",Integer.parseInt(numFloors));
        request.put("name",BuildingName);
        request.put("date",new Date(System.currentTimeMillis()));
        request.put("location",location);
        return request;
    }
/*
* Param 0 - None
* Param 1 - Building JSON
* Param 2 - Type
* Param 3 - Name of building
* Param 4 - image ofbuilding
* Param 5 - Number of floors
* Param 6 - location
* */

}