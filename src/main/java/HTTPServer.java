import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import ApiEndpoints.*;
import Building.*;
import Builder.BuildingManager;
import org.json.*;

import static ApiEndpoints.WebAPI.*;


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


                        /** Determining the API endpoint requested */

                        System.out.println(fileRequested.toString());
                        String endPoint = fileRequested.toString();
                        JSONObject response = new JSONObject();
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
                        if(verbose)
                            System.out.println("Server -> Client:"+ response.toString());
                        out.println(response.toString());
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
                    System.out.println(e.getMessage());
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
        
        private String getJSONStr(StringBuilder payload) {
            return payload.substring(payload.indexOf("{"));
        }
    }


}