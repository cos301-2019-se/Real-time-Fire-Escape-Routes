/**
 * @file Server.java
 * @brief This file contains the code that is inherited by specified Server classes
 *
 * @author Louw, Matthew Jason
 * @author Bresler,  Mathilda Anna
 * @author Braak, Pieter Albert
 * @author Reva, Kateryna
 * @author  Li, Xiao Jian
 *
 * @date 28/05/2019
 */
import Building.*;

public abstract class Server implements Runnable{
    Building building;
    Server(Building b){
        building=b;
    };
    void  start(){}; //Needs to be defined in which ever class inherits from this one
}
