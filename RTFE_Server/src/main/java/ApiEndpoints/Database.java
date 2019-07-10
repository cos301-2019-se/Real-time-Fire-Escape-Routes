package ApiEndpoints;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    public String fileName;
    File f;
    Lock lock;
    Connection con = null;
    Statement query = null;
    public Database()
    {
        fileName = "../../database.txt";
        f = new File(fileName);
        lock = new ReentrantLock();

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connected to DB!!");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        createTable();
    }
    //DATABASE CODE @Kinson
    /**
     * function is currently static and only creates table users if no table users exists
     */
    public void createTable(){

        try{
            query = con.createStatement();
            query.execute("create table if not exists users(id integer AUTO_INCREMENT, name varchar(250), email varchar(250) primary key, password varchar(250), userType varchar(250), deviceID integer, userDate date);");
            query.execute("create table if not exists buildings(building_id integer primary key, building_name varchar(250), num_floors integer, building_date date, building_location varchar(250), building_data longtext);");
            query = null;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

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

        boolean val = true;
        try{
            query = con.createStatement();
            query.execute("insert into users(name, email, password, userType) values(\'"+name+"\'"+", " + "\'"+email+"\'"+", " + "\'"+pass+"\'"+", " + "\'"+type+"\')");

            query = null;
        }catch(Exception e){
            val = false;

            System.out.println(e.getMessage());
        }
        lock.unlock();
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
        output();
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
        boolean val;
        try{
            query = con.createStatement();
            query.execute("update users set password = " + "\'" + password + "\' where email = " + "\'"+email+"\'");
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
     * @param name: is a string of user name
     */
    public boolean delete(String name){
        lock.lock();
        boolean val = false;
        try{

            query = con.createStatement();
            val = query.execute("delete from users WHERE name = " + "\'" + name + "\'");
            query = null;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        lock.unlock();
        return val;
    }
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

    public void write(String name, String email, String pass, String type)
    {
        lock.lock();
//        insert(name, email, pass, type);
        try {

            FileWriter fileWriter =
                    new FileWriter(f, true);
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            Scanner input = new Scanner(System.in);
            bufferedWriter.write(name+","+pass);
//            bufferedWriter.write(pass);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
        }
        lock.unlock();
    }

    /*
     * If ONLY a name is provided it will check if that name exists in the database
     *
     * if a password is provided it will check if that password matches the one in the db
     * */

    public boolean search(String email,String pass)
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

//        boolean found = false;
//        String line = null;
//        String [] currentLine = new String[2];
//        try {
//            FileReader fileReader =
//                    new FileReader(f);
//
//            BufferedReader bufferedReader =
//                    new BufferedReader(fileReader);
//
//            while((line = bufferedReader.readLine()) != null && !found) {
//                currentLine = line.split(",");
////                System.out.println(currentLine[0]);
//                if(currentLine[0].equals(name) )
//                {
////                    System.out.println("found user");
//                    found = true;
//                    break;
//                }
//
//            }
//
//            bufferedReader.close();
////            System.out.println("Found:"+found);
////            System.out.println("pass match:"+currentLine[1].equals(pass));
//            if(pass.equals("") || found == false){//normal search
//                return found;
//            }else{  // Login Attempt
//                if(currentLine[1].equals(pass))
//                    return true; // Pass match
//                else
//                    return false; // Pass failed
//            }
//
//        }
//        catch(FileNotFoundException ex) {
//            System.out.println(
//                    "Unable to open file '" +
//                            fileName + "'");
//        }
//        catch(IOException ex) {
//            System.out.println(
//                    "Error reading file '"
//                            + fileName + "'");
//        }
//        return false;
    }

    public String remove(String name)
    {
//        lock.lock();
//        Vector<String> tempData = new Vector<String>();
//        boolean found = false;
//        String line = null;
//        String ret = "";
//        try {
//            FileReader fileReader =
//                    new FileReader(f);
//
//            BufferedReader bufferedReader =
//                    new BufferedReader(fileReader);
//
//            while((line = bufferedReader.readLine()) != null) {
//
//                if(line.equals(name))
//                {
//                    found = true;
//                    bufferedReader.readLine();
//                }
//                else
//                {
//                    tempData.add(line);
//                }
//            }
//            if(found)
//            {
//                FileWriter fileWriter =
//                        new FileWriter(f, false);
//                fileWriter.close();
//                for(int i = 0; i < (tempData.size()- 1) ; i += 2)
//                {
//                    write(tempData.get(i), tempData.get(i+1));
//                }
//                ret += "Found and removed ";
//                ret += name += "\n\r";
//                ret += "\n\t";
//            }
//            else
//            {
//                ret += "Name " + name + " not found" + "\n\r";
//            }
//            bufferedReader.close();
//        }
//        catch(FileNotFoundException ex) {
//            System.out.println(
//                    "Unable to open file '" +
//                            fileName + "'");
//        }
//        catch(IOException ex) {
//            System.out.println(
//                    "Error reading file '"
//                            + fileName + "'");
//        }
//        lock.unlock();
//        return ret;
        return null;
    }

}
