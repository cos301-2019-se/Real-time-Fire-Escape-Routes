package Building;

public class Fire {

    double[] coords = new double[2];
    private double radius = 1.0;

    public Fire(double x,double z){
        coords[0] = x;
        coords[1] = z;
    }


    /**
     * @Url: https://math.stackexchange.com/questions/275529/check-if-line-intersects-with-circles-perimeter
     * */
    boolean intersect(Node a,Node b){
        return (Math.abs((b.coordinates[0] - a.coordinates[0])*coords[1] +  coords[0]*(a.coordinates[1] -
                b.coordinates[1]) + (a.coordinates[0] - b.coordinates[0])*a.coordinates[1] +
                (a.coordinates[1] - b.coordinates[1])*a.coordinates[0])/ Math.sqrt(Math.pow((b.coordinates[0] - a.coordinates[0]),2) +
                Math.pow((a.coordinates[1] - b.coordinates[1]),2)) <= radius);
    }
}
