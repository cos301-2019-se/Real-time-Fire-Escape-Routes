package ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * The Database class is used for administration of information that doesn't directly affect the building.
 * The Database makes use of sqlite to accomplish fast, secure and reliable access
 * */
public class Database {
    public String fileName;
    File f;
    Lock lock;
    Connection con = null;
    Statement query = null;
    MessageDigest md;
    byte[] salt;
    SQLInjectionEscaper shield;
    public Database()
    {
        shield = new SQLInjectionEscaper();

        try {
            md = MessageDigest.getInstance("SHA-1");
            salt = getSalt();
            md.update(salt);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        fileName = "../../database.txt";
        f = new File(fileName);
        lock = new ReentrantLock();

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connected to DB!!");
        }catch(Exception e){
            System.out.println("Failed to connect to database");
            System.out.println(e.getMessage());
        }
        createTable();
    }

    public void wakeup()
    {

    }
    //DATABASE CODE @Kinson
    /**
     * function that creates a salt for the user passwords
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * function is currently static and only creates table users if no table users exists
     */
    public void createTable(){

        try{
            query = con.createStatement();
//            query.execute("drop table if exists buildings;");
            query.execute("create table if not exists users(id integer primary key, name varchar(250), email varchar(250), password varchar(250), userType varchar(250), deviceID integer, userDate date );");
            query.execute("create table if not exists buildings(building_id integer primary key, building_name varchar(250), num_floors integer, building_date date, building_location varchar(250));");
            query.execute("create table if not exists user_building(ub_id integer primary key, ub_user_id integer, ub_building_id integer, ub_user_status varchar(250));");
            query.execute("create table if not exists apiKeys(key_id integer primary key, apikey varchar(250), date_created date, date_expire date, authorizationLevel integer);");

            query = null;
        }catch (Exception e){
            System.out.println("createTable : " + e.getMessage());
        }

    }
    public boolean addUserToBuilding(String email , String buildingName)
    {
        lock.lock();
         boolean val = true;
        try{
            query = con.createStatement();
            ResultSet results =  select("select * from users where email = '" + email + "'");
            int u_id = results.getInt("id");
            ResultSet results2 = select("select building_id, count(*) as rowcount from buildings where building_name = '" + buildingName +"'");
            int b_id = results2.getInt("building_id");
            query = null;
            query = con.createStatement();
            query.execute("update user_building set ub_user_status = 'inactive' where ub_user_id = '" + u_id + "'");
            query.execute("insert into user_building(ub_user_id, ub_building_id, ub_user_status) values(" + u_id + ", " + b_id  +" , 'active')");
            query = null;
        }catch(Exception e){
            val = false;
//            lock.unlock();
            System.out.println("addUserToBuilding: " + e.getMessage());
        }
        finally{

            lock.unlock();
        }
        return val;
    }

    /**
     * function returns all the users in a specific building
     * @param building_name: an integer of the building ID
     * @return String of users in building with their data
     */
    public JSONArray getUsersInBuilding(String building_name)
    {
        JSONArray ret = new JSONArray();
        try{
            if(building_name.compareTo("loading...") == 0)
            {
                building_name = "1 Story Office";
            }
            ResultSet building_id_set = select("select * from buildings where building_name = '" + building_name + "'");
            int building_id = building_id_set.getInt("building_id");

            ResultSet result = select("select * from user_building where ub_building_id = '" + building_id + "'");
            while(result.next()){
                int id = result.getInt("ub_user_id");
                ResultSet nameResults = select("select * from users where id = '" + id + "'");
                JSONObject current = new JSONObject();

                current.put("email",nameResults.getString("email"));
                current.put("name",nameResults.getString("name"));
                current.put("userType",nameResults.getString("userType"));
                current.put("deviceID",nameResults.getString("deviceID"));
                ret.put(current);
            }
            while(result.next()){

            }
        }catch (Exception e){
            System.out.println("getuib: " + e.getMessage());
        }
        return ret;
    }
    /**
     * function used to return all users in users table
     */
    public JSONArray getBuildings() {
        ResultSet result = select("select * from buildings");
        JSONArray ret = new JSONArray();
        try{
            while(result.next()){
                JSONObject current = new JSONObject();

                current.put("building_name",result.getString("building_name"));
                ret.put(current);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ret;
    }


    /**
     * function used to return all users in users table
     */
    public JSONArray getUsers() {
        output();
        ResultSet result = select("select * from users order by id desc");
        JSONArray ret = new JSONArray();
        try{
            while(result.next()){
                JSONObject current = new JSONObject();

                current.put("email",result.getString("email"));
                current.put("name",result.getString("name"));
                current.put("userType",result.getString("userType"));
                current.put("deviceID",result.getString("deviceID"));
                ret.put(current);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ret;
    }
    /**
     * function can be used to insert new users to the users table
     * @param buildingParamName: is a string of user name
     * @param numFloors: is a string of user password
     */
    public boolean insertBuilding(String buildingParamName, int numFloors, Date bdate, String buildingLocation) {
        lock.lock();
        boolean val = true;
        try
        {
            query = con.createStatement();
            query.execute("insert into buildings(building_name, num_floors,building_date, building_location) values(\'"+buildingParamName+"'"+", " +numFloors +", " + "'"+bdate+"'"+", " + "'"+buildingLocation+"')");
            query = null;
        } catch(Exception e) {
            System.out.println("Insert building: " + e);
            val = false;
        }
        finally {
            lock.unlock();
        }
        return val;
        //        create table if not exists buildings(building_id integer primary key, building_name varchar(250), num_floors integer, building_date date, building_location varchar(250)
    }

    /**
     * function can be used to insert new users to the users table
     * @param name: is a string of user name
     * @param pass: is a string of user password
     */
    public boolean insert(String name, String email, String pass, String type, String buildingName){
        name = shield.escapeString(name, true);
        email = shield.escapeString(email, true);
        pass = shield.escapeString(pass, true);
        type = shield.escapeString(type, true);
        buildingName = shield.escapeString(buildingName, true);
        lock.lock();
        String generatedPassword = null;
        try {
            byte[] bytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            lock.unlock();
        }
        boolean val = true;
        try{
            query = con.createStatement();
            query.execute("insert into users(name, email, password, userType) values(\'"+ shield.escapeString(name, true)+"\'"+", " + "\'"+email+"\'"+", " + "\'"+generatedPassword+"\'"+", " + "\'"+type+"\')");
            ResultSet results =  select("select * from users where email = '" + email + "'");
            int u_id = results.getInt("id");
            ResultSet results2 = select("select building_id, count(*) as rowcount from buildings where building_name = '" + buildingName +"'");
            int b_id = results2.getInt("building_id");
            query = null;
            query = con.createStatement();
            query.execute("insert into user_building(ub_user_id, ub_building_id, ub_user_status) values(" + u_id + ", " + b_id  +" , 'active')");
            query = null;
        }catch(Exception e){
            val = false;
//            lock.unlock();
            System.out.println("Register: " + e.getMessage());
        }
        finally{

            lock.unlock();
        }
        return !val;
    }
    /**
     * function can be used to edit user email
     * @param email: is a string of user email for identification to find row to update
     * @param newEmail: is a string of new email
     */
    public boolean updateEmail(String email, String newEmail)
    {
        email = shield.escapeString(email, true);
        newEmail = shield.escapeString(newEmail, true);
        lock.lock();
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set email = " + "\'" + newEmail + "\' where email = " + "\'" +email +"\'");
            val = true;
            query = null;
        }catch(Exception e){
            val = false;
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function can be used to edit deviceID
     * @param email: is a string of user email for identification to find row to update
     * @param deviceID: is a string of devide ID to change to
     */
    public boolean updateDeviceID(String email, String deviceID)
    {
        email = shield.escapeString(email, true);
        deviceID = shield.escapeString(deviceID, true);
        lock.lock();
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set deviceID = " + "\'" + deviceID + "\' where email = " + "\'" +email+"\'");
            val = true;
            query = null;
        }catch(Exception e){
            val = false;
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function can be used to edit duser password
     * @param email: is a string of user email for identification to find row to update
     * @param password: is a string of devide ID to change to
     */
    public boolean updatePassword(String email, String password)
    {
        email = shield.escapeString(email, true);
        password = shield.escapeString(password, true);
        lock.lock();
        String generatedPassword = null;
        try {
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            lock.unlock();
        }
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set password = " + "\'" + generatedPassword + "\' where email = " + "\'"+email+"\'");
            val = true;
            query = null;
        }catch(Exception e){
            lock.unlock();
            val = false;
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function can be used to edit user type
     * @param email: is a string of user email for identification to find row to update
     * @param type: is a string of user type to change to
     */
    public boolean updateType(String email, String type)
    {
        email = shield.escapeString(email, true);
        type = shield.escapeString(type, true);
        lock.lock();
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set userType = " + "\'" + type + "\' where email = " + "\'"+email+"\'");
            val = true;
            query = null;
        }catch(Exception e){
            val = false;
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function can be used to edit uder name
     * @param email: is a string of user email for identification to find row to update
     * @param name: is a string of user name to change to
     */
    public boolean updateName(String email, String name)
    {
        email = shield.escapeString(email, true);
        name = shield.escapeString(name, true);
        lock.lock();
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set name = " + "\'" + name + "\' where email = " + "\'"+email+"\'");
            val = true;
            query = null;
        }catch(Exception e){
            val = false;
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function can be used to REMOVE a user from the users table
     * @param email: is a string of user name
     */
    public boolean delete(String email){
        email = shield.escapeString(email, true);
        lock.lock();
        boolean val = false;
        try{

            query = con.createStatement();
            query.execute("delete from users WHERE email = " + "\'" + email + "\'");
            val = true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
    /**
     * function outputs all the values in the users table
     */
    public void output()
    {
        ResultSet result = select("select * from users order by id desc");
        try{
            while(result.next()){
                System.out.print("|id : " + result.getInt("id"));
                System.out.print("    |email : " + result.getString("email"));
                System.out.print("    |name : " + result.getString("name"));
                System.out.print("    |password : " + result.getString("password"));
                System.out.print("    |userType : " + result.getString("userType"));
                System.out.print("    |deviceID : " + result.getString("deviceID"));
                System.out.println("");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * function works as a means to create sql queries to the database and will return a bool to determine if query was successful
     * @param sql: is an sql query
     * @return boolean: true - query is successful, false - query failed
     */
    public boolean execute(String sql){
        try{
            query = con.createStatement();
            query.execute(sql);
            query = null;
            return true;
        }catch(Exception e){
            printError(e,sql);
        }

        return  false;
    }

    /**
     * function can be used to make selections to the db
     * @param sql: is a sql selection query
     * @return ResultSet: which holds all the data returned from the select query
     */
    public ResultSet select(String sql){
        try{
            query = con.createStatement();
            ResultSet result = query.executeQuery(sql);
            query = null;
            return result;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * function will close the connection to the db
     */
    public void close(){
        try{
            con.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * function prints errors from any query
     * @param e: is the error which was caught
     * @param sql: is the sql which caused the error
     */
    private void printError(Exception e, String sql){
        System.out.println("Error in " + sql + ": " + e.getMessage() );
    }

    //DATABSE CODE
    public String outputFile()
    {
        String line = null;
        String ret = "";
        try {
            FileReader fileReader =
                    new FileReader(f);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                ret += line += " - ";
                ret += bufferedReader.readLine();
                ret += "\n\r";
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return ret;
    }



    /*
     * If ONLY a name is provided it will check if that name exists in the database
     *
     * if a password is provided it will check if that password matches the one in the db
     * */
    /**
     * function searches user table for user with sepcified email and checks device ID.
     * @param email: the email for the user
     * @param deviceId: the deviceID to be set or checked
     * @return If the deviceID is not set then set device ID and return TRUE, if set and deviceID parameters = devideID set in database return TRUE,
     * else return FALSE
     */


    public boolean validateDeviceId(String email, String deviceId)
    {
        email = shield.escapeString(email, true);
        deviceId = shield.escapeString(deviceId, true);
        boolean validated = false;
        try{

            query = con.createStatement();
            ResultSet result = select("select email, deviceID, count(*) as rowcount from users where email = '"+email+"'");
            query = null;
            if (result.getInt("rowcount") > 0){
                if(result.getString("deviceID") == null)
                {
                    updateDeviceID(email, deviceId);
                    validated =  true;
                }
                else if(result.getString("deviceID").compareTo(deviceId) == 0)
                {
                    validated =  true;
                }
                else
                {
                    validated =  false;
                }
            }
        }catch(Exception e){
            System.out.println("Search: " +e.getMessage());
        }
        return validated;
    }

    /**
     * function searches user table for user, or logs in the user if password is provided
     * @param email: the email for the user
     * @param pass: the password for the user, or empty if just using function to search
     */

    public boolean search(String email,String pass)
    {
        email = shield.escapeString(email, true);
//        pass = shield.escapeString(pass, true);
        String generatedPassword = null;
        for(int k = 0; k < 2; k++)
        {
            try {
                byte[] bytes = md.digest(pass.getBytes());
                StringBuilder sb = new StringBuilder();
                for(int i=0; i< bytes.length ;i++)
                {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                generatedPassword = sb.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

            try{

                query = con.createStatement();
                ResultSet result = select("select count(*) as rowcount from users where email = '"+email+"' and password = '" + generatedPassword + "'");
                query = null;
                if (result.getInt("rowcount") > 0) return true;
            }catch(Exception e){

                System.out.println("Search: " +e.getMessage());
            }
            return false;
//        }

    }

    public String getUserType(String email)
    {
        email = shield.escapeString(email, true);

        try{

            query = con.createStatement();
            ResultSet result = select("select * from users where email = '"+email+"'");
            query = null;
            if (result != null)
                return result.getString("userType");
        }catch(Exception e){
            System.out.println("getUserType: " +e.getMessage());
        }
        return "invalid";


    }
    public boolean exists(String email)
    {
            email = shield.escapeString(email, true);

            try{

                query = con.createStatement();
                ResultSet result = select("select count(*) as rowcount from users where email = '"+email+"'");
                query = null;
                if (result.getInt("rowcount") > 0) return true;
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            return false;



    }

    public String generateKey(){
        String generatedKey = null;
        try {
            byte[] bytes = md.digest(String.valueOf(System.currentTimeMillis()).getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedKey = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        lock.lock();
        try{
            query = con.createStatement();
            Date now = new Date(System.currentTimeMillis());
            Date expire = new Date(System.currentTimeMillis()+1800000);// 30mins
            query.execute("insert into apiKeys(apikey,date_created,date_expire) values(\'"+generatedKey+"\'"+"\'"+now+"\'"+"\'"+expire+"\'");
            query = null;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return  generatedKey;
    }
    public int validateKey(String key){
        lock.lock();
        int level = 0;
        try {
            query = con.createStatement();
            ResultSet result = select("select * from apiKeys where apikey = '"+key+"'");
            query = null;
            Date expireDate = result.getDate("date_expire");
            Date now = new Date(System.currentTimeMillis());
            if(now.before(expireDate)){
                level = result.getInt("authorizationLevel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return level;
    }


}

class SQLInjectionEscaper {

    public static String escapeString(String x, boolean escapeDoubleQuotes) {
        StringBuilder sBuilder = new StringBuilder(x.length() * 11/10);

        int stringLength = x.length();

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
                case 0: /* Must be escaped for 'mysql' */
                    sBuilder.append('\\');
                    sBuilder.append('0');

                    break;

                case '\n': /* Must be escaped for logs */
                    sBuilder.append('\\');
                    sBuilder.append('n');

                    break;

                case '\r':
                    sBuilder.append('\\');
                    sBuilder.append('r');

                    break;

                case '\\':
                    sBuilder.append('\\');
                    sBuilder.append('\\');

                    break;

                case '\'':
                    sBuilder.append('\\');
                    sBuilder.append('\'');

                    break;

                case '"': /* Better safe than sorry */
                    if (escapeDoubleQuotes) {
                        sBuilder.append('\\');
                    }

                    sBuilder.append('"');

                    break;

                case '\032': /* This gives problems on Win32 */
                    sBuilder.append('\\');
                    sBuilder.append('Z');

                    break;

                case '\u00a5':
                case '\u20a9':
                    // escape characters interpreted as backslash by mysql
                    // fall through

                default:
                    sBuilder.append(c);
            }
        }

        return sBuilder.toString();
    }
}