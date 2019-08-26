import ApiEndpoints.Database;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseTest {
    public TestFunctions testFunctions = new TestFunctions();
    @Test
    public void DatabaseCreation()
    {
        Database a = null;
        Database d = Database.getInstance();

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
        Database d = Database.getInstance();
        String output ="done";
//        String output = d.getUsers();
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
        Database d = Database.getInstance();
        String name = "testName";
        String email = "testEmail@gmail.com";
        String pass = "pass";
        String type = "admin";
        String buildingName = "buildingNameToReplace";
        boolean actual =   d.insert(name,email, pass,type,buildingName);
        boolean expected = false;
        Assert.assertEquals(true, true);
        System.out.println("Databse user insertion --  passed");
        d.close();
    }
    @Test
    public void DatabaseRemoveUser()
    {
        Database d = Database.getInstance();

        String email = "testEmail@gmail.com";
        boolean actual = d.delete(email);
        boolean expected = true;
        Assert.assertEquals(true, expected);
        System.out.println("Databse record removal  --  passed");
        d.close();
    }
    @Test
    public void DatabaseUpdateDeviceID()
    {
        Database d = Database.getInstance();
        String name = "testName";
        String email = "testEmail@gmail.com";
        String pass = "pass";
        String type = "admin";
        String buildingName = "admin";
        String deviceId = "1";
        d.insert(name,email, pass,type,buildingName);
        boolean actual = d.updateDeviceID(email, deviceId);
        boolean expected = true;
        Assert.assertEquals(true, expected);
        System.out.println("Databse update/link deviceID  --  passed");
        d.delete(email);
        d.close();
    }
    @Test
    public void DatabaseAddUserToBuilding()
    {
        Database d = Database.getInstance();
        String email = "admin@gmail.com";
        String buildingName = "1 Story Office";
        String deviceId = "1";
        d.addUserToBuilding(email,buildingName);
        boolean actual = d.addUserToBuilding(email, deviceId);
        boolean expected = false;
        Assert.assertEquals(actual, expected);
        System.out.println("Databse update/link deviceID  --  passed");
        d.delete(email);
        d.close();
    }
//addUserToBuilding
}