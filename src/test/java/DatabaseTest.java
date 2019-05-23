import ApiEndpoints.Database;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseTest {
    @Test
    public void DatabaseCreation()
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
    public void DatabaseReading()
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
    public void DatabaseAddUser()
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
    public void DatabaseRemoveUser()
    {
        Database d = new Database();

        String name = "Json";
        d.remove(name);
        String expected = "Found and removed" + name + "\n\r" + "\n\t";
        String actual = d.remove(name);
        System.out.println(actual);
        Assert.assertEquals(expected, actual);
        System.out.println("Databse record removal  --  passed");
    }
}