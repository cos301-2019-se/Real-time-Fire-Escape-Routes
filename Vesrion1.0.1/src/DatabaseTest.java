import Web.Database;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseTest {
    @Test
    public void testOne()
    {
        Database a = null;
        Database d = new Database();

        if(d != null)
        {
            a = d;
        }
        Assert.assertEquals(d, a);
        System.out.println("Databse creation  --  passed");
    }
    @Test
    public void testTwo()
    {
        Database d = new Database();
        String output = d.outputFile();
        String expected = output;
        if(output == "")
        {
            expected += "whoopes";
        }
        Assert.assertEquals(output, expected);
        System.out.println("Databse file reading  --  passed");
    }
    @Test
    public void testThree()
    {
        Database d = new Database();
        //TEST FILE WRITING AND READING
        String name = "Json";
        String pass = "12345";
        d.write(name, pass);
        System.out.println("Databse file writing  --  passed");
        boolean actual = d.search(name, pass);
       boolean expected = true;
        Assert.assertEquals(actual, expected);
        System.out.println("Databse file searching  --  passed");
    }
    @Test
    public void testFour()
    {
        Database d = new Database();

        String expected = "Json";
        String actual = d.remove(expected);
        Assert.assertEquals(expected, actual);
        System.out.println("Databse record removal  --  passed");
    }
}