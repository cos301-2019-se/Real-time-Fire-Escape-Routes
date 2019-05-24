import Building.*;

public abstract class Server implements Runnable{
    Building building;
    Server(Building b){
        building=b;
    };
    void  start(){}; //Needs to be defined in which ever class inherits from this one
}
