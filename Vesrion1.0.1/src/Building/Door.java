package Building;

public class Door {
    private static int numDoors =0;
    public Node node;
    private double [] coordinates;
    private double size;
    private NodeType type;
    public int id;
    public Door(NodeType Type,double[] c)
    {
        id= numDoors++;
        type = Type;
        coordinates = new double[2];
        for(int i = 0; i < 3; i++)
        {
            coordinates[0] = c[0];
            coordinates[1] = c[1];
        }
        node = new Node(type, coordinates);
    }
    public double[] getCenter()
    {
        return coordinates;
    }
}
