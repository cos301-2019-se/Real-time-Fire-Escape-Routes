<<<<<<< HEAD:Vesrion1.0.1/src/Server.java
import Building.*;

public abstract class Server implements Runnable{
    Building building;
    Server(Building b){
        building=b;
    };
    void  start(){}; //Needs to be defined in which ever class inherits from this one
}
=======
import Building.*;

public abstract class Server implements Runnable{
    Building building;
    Server(Building b){
        building=b;
    };
    void  start(){}; //Needs to be defined in which ever class inherits from this one
}
>>>>>>> TravisTesting:src/main/java/Server.java
