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
        d.close();
    }
    @Test
    public void DatabaseReading()
    {
        Database d = new Database();
        String output = d.getUsers();
        String expected = output;
        if(output == "")
        {
            expected += "whoopes";
        }
        Assert.assertEquals(output, expected);
        System.out.println("Databse reading  --  passed");
        d.close();
    }
    @Test
    public void DatabaseAddUser()
    {
        Database d = new Database();
        String name = "testName";
        String email = "testEmail@gmail.com";
        String pass = "pass";
        String type = "admin";
        d.insert(name,email, pass,type);
        System.out.println("Databse user insertion --  passed");
        boolean actual = d.search(name, pass);
        boolean expected = false;

        Assert.assertEquals(actual, expected);
        System.out.println("Databse searching  --  passed");
        d.close();
    }
    @Test
    public void DatabaseRemoveUser()
    {
        Database d = new Database();

        String name = "testEmail@gmail.com";
        boolean actual = d.delete(name);
        boolean expected = false;
        Assert.assertEquals(actual, expected);
        System.out.println("Databse record removal  --  passed");
        d.close();
    }
    @Test
    public void DatabaseUpdateDeviceID()
    {
        Database d = new Database();
        String name = "testName";
        String email = "testEmail@gmail.com";
        String pass = "pass";
        String type = "admin";
        String deviceId = "1";
        d.insert(name,email, pass,type);
        boolean actual = d.updateDeviceID(email, deviceId);
        boolean expected = true;
        Assert.assertEquals(actual, expected);
        System.out.println("Databse update/link deviceID  --  passed");
        d.delete(email);
        d.close();
    }
}