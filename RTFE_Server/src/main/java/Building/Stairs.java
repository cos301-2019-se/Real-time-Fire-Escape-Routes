package Building;

import java.util.Vector;

public class Stairs extends Room {
    public Stairs(RoomType type) {
        super(type);
    }

    void rotateSouth(){
        Corner one = Corners.get(0);
        Vector<Corner> wallOne = Walls.get(0);

        Corner two = Corners.get(1);
        Vector<Corner> wallTwo = Walls.get(1);

        Corner three = Corners.get(2);
        Vector<Corner> wallThree = Walls.get(2);

        Corner four = Corners.get(3);
        Vector<Corner> wallFour = Walls.get(3);

        Corners.clear();
        Walls.clear();


    }

}
