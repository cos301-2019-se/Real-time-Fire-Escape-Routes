package Building;

public class Sensor extends Node{
    String deviceID = "";
    public Sensor(double[] pos,int floor,String deviceID){
        super(NodeType.sensor,pos,floor);
        this.deviceID = deviceID;
    }
    double [] getLocation(){
        return this.coordinates;
    }
}
