package ApiEndpoints;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
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
    public Database()
    {
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
    //DATABASE CODE @Kinson
    /**
     * function is currently static and only creates table users if no table users exists
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    public void createTable(){

        try{
            query = con.createStatement();
            query.execute("create table if not exists users(id integer AUTO_INCREMENT, name varchar(250), email varchar(250) primary key, password varchar(250), userType varchar(250), deviceID integer, userDate date);");
            query.execute("create table if not exists buildings(building_id integer primary key, building_name varchar(250), num_floors integer, building_date date, building_location varchar(250), building_data longtext);");
            query.execute("create table if not exists user_building(ub_id integer primary key, ub_user_id integer, ub_building_id integer, ub_user_status varchar(250));");
            query = null;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    /**
     * function returns all the users in a specific building
     * @param building_id: an integer of the building ID
     * @return String of users in building with their data
     */
    public String getUsersInBuilding(int building_id)
    {
        ResultSet result = select("select ub_user_id from user_building where ub_building_id = " + building_id);
        Vector<String> ret = new Vector<String>();
        try{
            while(result.next()){
                ret.add(String.valueOf(result.getInt("ub_user_id")));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ret.toString();
    }
    /**
     * function used to return all users in users table
     */
    public String getUsers() {
        ResultSet result = select("select * from users order by id desc");
        Vector<String> ret = new Vector<String>();
        try{
            while(result.next()){
                Vector<String> current = new Vector<String>();

                current.add(String.valueOf(result.getInt("id")));
                current.add(result.getString("email"));
                current.add(result.getString("name"));
                current.add(result.getString("password"));
                current.add(result.getString("userType"));
                current.add(result.getString("deviceID"));
                ret.add(current.toString());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ret.toString();
    }
    /**
     * function can be used to insert new users to the users table
     * @param name: is a string of user name
     * @param pass: is a string of user password
     */
    public boolean insert(String name, String email, String pass, String type){
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
            query.execute("insert into users(name, email, password, userType) values(\'"+name+"\'"+", " + "\'"+email+"\'"+", " + "\'"+generatedPassword+"\'"+", " + "\'"+type+"\')");

            query = null;
        }catch(Exception e){
            val = false;
//            lock.unlock();
            System.out.println(e.getMessage());
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
     * function searches user table for user, or logs in the user if password is provided
     * @param email: the email for the user
     * @param pass: the password for the user, or empty if just using function to search
     */
    public boolean search(String email,String pass)
    {
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
        }
        if(pass.compareTo("") == 0)
        {
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
        else
        {
            try{

                query = con.createStatement();
                ResultSet result = select("select count(*) as rowcount from users where email = '"+email+"' and password = '" + generatedPassword + "'");
                query = null;
                if (result.getInt("rowcount") > 0) return true;
            }catch(Exception e){

                System.out.println(e.getMessage());
            }
            return false;
        }

    }
    public boolean oldSearch(String email,String pass)
    {

        if(pass.compareTo("") == 0)
        {
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
        else
        {

            try{

                query = con.createStatement();
                ResultSet result = select("select count(*) as rowcount from users where email = '"+email+"' and password = '" + pass + "'");
                query = null;
                if (result.getInt("rowcount") > 0) return true;
            }catch(Exception e){

                System.out.println(e.getMessage());
            }
            return false;
        }

    }



}
