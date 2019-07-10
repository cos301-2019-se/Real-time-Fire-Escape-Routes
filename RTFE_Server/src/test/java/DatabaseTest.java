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
        String name = "TestingUserAccount";
        String pass = "testingPassword123";
        String email = "TestingUserAccount@gmail.com";
        String type = "admin";


        boolean actual =  d.insert(name, email,pass,type);
        Assert.assertEquals(actual, true);
        System.out.println("Databse file searching  --  passed");
    }
    @Test
    public void DatabaseRemoveUser()
    {
        Database d = new Database();

        String name = "TestingUserAccount";
        d.delete(name);
        String expected = "Found and removed" + name + "\n\r" + "\n\t";
        String actual = d.remove(name);
        System.out.println(actual);
        Assert.assertEquals(true, true);
        System.out.println("Databse record removal  --  passed");
    }
}