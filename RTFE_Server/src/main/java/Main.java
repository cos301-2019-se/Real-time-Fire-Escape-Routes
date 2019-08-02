/**
 * @file Main.java
 * @brief This is the Main java file executed to run the Server
 *
 * @author Louw, Matthew Jason
 * @author Bresler,  Mathilda Anna
 * @author Braak, Pieter Albert
 * @author Reva, Kateryna
 * @author  Li, Xiao Jian
 *
 * @date 28/05/2019
 */
import Building.Building;
import java.util.Vector;

public class Main implements Runnable {

    Server myserver;
    @Override
    public void run() {
        myserver.start();
    }
    public Main(Server s) {
        myserver = s;
    }

    public static void main(String[] args){
        try
        {
//            Vector<Building> LoadedBuildings = new Vector<>();
            Thread thread = new Thread(new HTTPServer());
            thread.start();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
