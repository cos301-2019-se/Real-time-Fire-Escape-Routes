package Builder;

public class babyMaker extends Builder {

    babyMaker(Object _data) {
        super(_data);
    }
    Person buildPart(){
        try{

            Person t= new Person(Integer.toString((Integer)data.get("id")));
            double[] xy = new double[2];
            // Extract numbers from JSON array.
            for (int i = 0; i <2; ++i) {
                xy[i] = data.getJSONArray("position").getDouble(i);
            }
            t.setPosition(xy);
            return t;
        }catch(Exception e ){
            System.out.println("Person FAIL");
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}
