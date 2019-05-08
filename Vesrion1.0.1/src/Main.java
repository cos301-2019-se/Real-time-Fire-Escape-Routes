import Building.*;
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
            Building Current = null;
            //##############################
            //#         HTTP Server        #
            //##############################

            Thread thread = new Thread(new HTTPServer());
            thread.start();
            //##############################
            //#            RTFE            #
            //##############################
/*
            Thread thread1 = new Thread( new RTFEServer());

            thread1.start();
            thread1.wait();
  */

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
