import Building.*;

public abstract class Server implements Runnable{
    Building current;
    Server(Building b){
        current=b;
    };
    void  start(){}; //Needs to be defined in which ever class inherits from this one
}
