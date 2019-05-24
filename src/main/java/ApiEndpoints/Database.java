package ApiEndpoints;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Vector;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    public String fileName;
    File f;
    Lock lock;
    public Database()
    {
        fileName = "../../database.txt";
        f = new File(fileName);
        lock = new ReentrantLock();

    }
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

    public void write(String name, String pass)
    {
        lock.lock();
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
    public boolean search(String name,String pass)
    {
        boolean found = false;
        String line = null;
        String [] currentLine = new String[2];
        try {
            FileReader fileReader =
                    new FileReader(f);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null && !found) {
                currentLine = line.split(",");
//                System.out.println(currentLine[0]);
                if(currentLine[0].equals(name) )
                {
//                    System.out.println("found user");
                    found = true;
                    break;
                }

            }

            bufferedReader.close();
//            System.out.println("Found:"+found);
//            System.out.println("pass match:"+currentLine[1].equals(pass));
            if(pass.equals("") || found == false){//normal search
                return found;
            }else{  // Login Attempt
                if(currentLine[1].equals(pass))
                    return true; // Pass match
                else
                    return false; // Pass failed
            }

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
        return false;
    }

    public String remove(String name)
    {
        lock.lock();
        Vector<String> tempData = new Vector<String>();
        boolean found = false;
        String line = null;
        String ret = "";
        try {
            FileReader fileReader =
                    new FileReader(f);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                if(line.equals(name))
                {
                    found = true;
                    bufferedReader.readLine();
                }
                else
                {
                    tempData.add(line);
                }
            }
            if(found)
            {
                FileWriter fileWriter =
                        new FileWriter(f, false);
                fileWriter.close();
                for(int i = 0; i < (tempData.size()- 1) ; i += 2)
                {
                    write(tempData.get(i), tempData.get(i+1));
                }
                ret += "Found and removed ";
                ret += name += "\n\r";
                ret += "\n\t";
            }
            else
            {
                ret += "Name " + name + " not found" + "\n\r";
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
        lock.unlock();
        return ret;
    }
}
