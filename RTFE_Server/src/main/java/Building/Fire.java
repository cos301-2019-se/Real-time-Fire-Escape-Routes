package Building;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Fire {

    double[] coords = new double[2];
    private double radius ;
    boolean verbose = true;

    public Fire(double x, double z, double Radius) {
        coords[0] = x;
        coords[1] = z;
        radius = Radius;
    }


    /**
     * @Url: https://math.stackexchange.com/questions/275529/check-if-line-intersects-with-circles-perimeter
     */
    boolean intersect(Node a, Node b) {
        if(pointInInsideCircle(a)){
            if(verbose){
                System.out.println("Trigger on Node A");
                printDebug(a,b);
            }
            return true;
        }
        else if(pointInInsideCircle(b)){
            if(verbose){
                System.out.println("Trigger on Node B");
                printDebug(a,b);
            }
            return true;
        }
        else if(FindLineCircleIntersections(a,b)>0){
            if(verbose){
                System.out.println("Trigger on Line intersect");
                printDebug(a,b);
            }
            return true;
        }
        else{
            return false;
        }
/*
        if ((Math.abs((b.coordinates[0] - a.coordinates[0]) * coords[1] + coords[0] * (a.coordinates[1] -
                b.coordinates[1]) + (a.coordinates[0] - b.coordinates[0]) * a.coordinates[1] +
                (a.coordinates[1] - b.coordinates[1]) * a.coordinates[0]) / Math.sqrt(Math.pow((b.coordinates[0] - a.coordinates[0]), 2) +
                Math.pow((a.coordinates[1] - b.coordinates[1]), 2)) <= radius)) {


 */

    }


    private boolean pointInInsideCircle(Node a) {
        double absX = Math.pow(Math.abs(a.coordinates[0] - coords[0]), 2.0);
        double absY = Math.pow(Math.abs(a.coordinates[1] - coords[1]), 2.0);
        return Math.sqrt(absX + absY) < radius;
    }

    private void printDebug(Node a,Node b){
        System.out.println("Node A: " + a.nodeId + " at" + Arrays.toString(a.coordinates));
        System.out.println("Node B: " + b.nodeId + " at" + Arrays.toString(b.coordinates));
        System.out.println("Fire" + " at:" + Arrays.toString(coords) + " radius " + radius);
    }

    // Find the points of intersection.
    /**
     * @url: http://csharphelper.com/blog/2014/09/determine-where-a-line-intersects-a-circle-in-c/
     * */
    private int FindLineCircleIntersections(Node point1, Node point2)
    {
        double dx, dy, A, B, C, det, t;

        dx = point2.coordinates[0] - point1.coordinates[0];
        dy = point2.coordinates[1] - point1.coordinates[1];

        A = dx * dx + dy * dy;
        B = 2 * (dx * ( point1.coordinates[0] - coords[0]) + dy * ( point1.coordinates[1] - coords[1]));
        C = ( point1.coordinates[0] - coords[0]) * ( point1.coordinates[0] - coords[0]) +
                ( point1.coordinates[1] - coords[1]) * ( point1.coordinates[1] - coords[1]) -
                radius * radius;

        det = B * B - 4 * A * C;
        if ((A <= 0.0000001) || (det < 0))
        {
            // No real solutions.
            return 0;
        }
        else if (det == 0)
        {
            t = -B / (2 * A);
            return 1;
        }
        else
        {
            // Two solutions.
            t = (float)((-B + Math.sqrt(det)) / (2 * A));
            t = (float)((-B - Math.sqrt(det)) / (2 * A));;
            return 2;
        }
    }

    private boolean intersect2(Node a,Node b){


        return false;
    }

}
