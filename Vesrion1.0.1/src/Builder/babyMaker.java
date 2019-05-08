package Builder;

import Building.Person;

public class babyMaker extends Builder {

    babyMaker(Object _data) {
        super(_data);
    }
    Person buildPart(){
        /*
        try{
            /*
            System.out.println("BabyMaker: "+data.toString());
            Person t= new Person(Integer.toString((Integer)data.get("id")));
            double[] xy = new double[2];
            // Extract numbers from JSON array.
            xy[0] = data.getJSONArray("position").getDouble(0);
            xy[1] = data.getJSONArray("position").getDouble(2);

            t.setPosition(xy);
            return t;
        }catch(Exception e ){
            System.out.println("Person FAIL");
            System.out.println(e.getStackTrace());
        }*/
        return null;
    }
}
