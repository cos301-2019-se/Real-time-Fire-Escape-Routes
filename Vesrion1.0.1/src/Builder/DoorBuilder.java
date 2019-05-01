package Builder;

public class DoorBuilder extends Builder{

    DoorBuilder(Object _data) {
        super(_data);
    }
    Door buildPart(){
        try{
            double[] x = new double[2];
            double[] z = new double[2];
            double[] y = new double[2];
            // Extract numbers from JSON array.
            for (int i = 0; i <2; ++i) {
                x[i] =  data.getJSONArray("x").getDouble(i);
                y[i] =  data.getJSONArray("y").getDouble(i);
                z[i] =  data.getJSONArray("z").getDouble(i);
            }

            double[][] coords = {x,z,y};

            String name= "";
            try{
                name = Integer.toString((Integer) data.get("id"));
            }catch (Exception e){
                name = (String)data.get("id");
            }
            Door t = new Door(coords,null, name);
            System.out.println("DoorBuilder: "+data.toString());
            return t;
        }catch(Exception e){
            System.out.println("Door FAIL");
            System.out.println(e.getStackTrace());
        }

        return null;
    }
}
/**
 *             array[0][0] = 0;
 *             array[0][1] = 0;
 *             array[1][0] = 0;
 *             array[1][1] = 2;
 *             array[2][0] = 0;
 *             array[2][1] = 4;
 *             Node start1 = new Node("door 1", 1);
 *             Door door1 = new Door(array, start1, "door 1");
 * */