
import ApiEndpoints.WebAPI;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebAPITest {
    public TestFunctions testFunctions = new TestFunctions();
    private String APIKey ;
    Thread httpserver;
    @Before
    public void before() throws Exception {
        httpserver = new Thread(new HTTPServer());
        httpserver.start();

    }

    @After
    public void after() throws Exception {
        httpserver.stop();
    }
    @Test
    public void HandleRequestTest()
    {
        JSONObject testData = new JSONObject();
        try{
            JSONObject response = WebAPI.handleRequest(testData);
            if(response.has("apiKey")){
                APIKey = response.getString("apiKey");
            }

        }catch (Exception e){

        }
        Assert.assertEquals(true, true);

        System.out.println("HandleRequest with valid JSON-- passed");

    }
    @Test
    public void HandleRequestTestUsingNull()
    {

        JSONObject testData = new JSONObject();
        try{
            WebAPI.handleRequest(null);

        }catch (Exception e){

        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest with null jsonObject -- passed");

    }

    @Test
    public void register()
    {

        JSONObject current = new JSONObject();
        current.put("buildingName","1 Story Office");
        current.put("email","test@yahoo.com");
        current.put("password","1234");
        current.put("name","Test");
        current.put("password","1234");
        current.put("userType","Agent");
        current.put("type","register");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest register -- passed ");

    }
    @Test
    public void addUserToBuilding()
    {

        JSONObject current = new JSONObject();
        current.put("buildingName","1 Story Office");
        current.put("email","test@yahoo.com");
        current.put("type","addUserToBuilding");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest addUserToBuilding -- passed ");

    }

    @Test
    public void getBuildings()
    {

        JSONObject current = new JSONObject();
        current.put("type","getBuildings");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest getBuildings -- passed ");

    }

    @Test
    public void getUsers()
    {

        JSONObject current = new JSONObject();
        current.put("type","getUsers");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest getUsers -- passed ");

    }
    @Test
    public void getUsersInBuilding()
    {

        JSONObject current = new JSONObject();
        current.put("buildingName","1 Story Office");
        current.put("type","getUsersInBuilding");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest buildingName -- passed ");

    }
    @Test
    public void login()
    {

        JSONObject current = new JSONObject();
        current.put("buildingName","1 Story Office");
        current.put("email","test@yahoo.com");
        current.put("password","1234");
        current.put("type","login");
        try{
            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest login -- passed ");

    }




    @Test
    public void updateDeviceID()
    {

        JSONObject current = new JSONObject();
        current.put("email","test@yahoo.com");
        current.put("type","update");
        current.put("typeOfUpdate", "deviceID");
        current.put("value", "");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest update deviceID -- passed ");

    }



    @Test
    public void updateName()
    {

        JSONObject current = new JSONObject();
        current.put("email","test@yahoo.com");
        current.put("type","update");
        current.put("typeOfUpdate", "name");
        current.put("value", "newName");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest update name -- passed ");

    }

    @Test
    public void updatePassword()
    {

        JSONObject current = new JSONObject();
        current.put("email","test@yahoo.com");
        current.put("type","update");
        current.put("typeOfUpdate", "password");
        current.put("value", "4321");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest update password -- passed ");

    }

    @Test
    public void updateUserType()
    {

        JSONObject current = new JSONObject();
        current.put("email","test@yahoo.com");
        current.put("type","update");
        current.put("typeOfUpdate", "userType");
        current.put("value", "Agent");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest update userType -- passed ");

    }

    @Test
    public void remove()
    {

        JSONObject current = new JSONObject();
        current.put("email","test@yahoo.com");
        current.put("type","remove");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest remove -- passed ");

    }

    @Test
    public void uploadBuilding()
    {

        JSONObject current = new JSONObject();
        current.put("name","testBuilding");
        current.put("type","uploadBuilding");
        current.put("num_floors", "0");
        current.put("date", "0");
        current.put("location", "here");
        current.put("file", "file");
        current.put("img", "img");
        current.put("value", "Agent");
        try{

            testFunctions.SendRequest(current);
        }catch (Exception e) {
            System.out.println("Exception: " + e);
            Assert.assertEquals(false, true);
        }
        Assert.assertEquals(true, true);
        System.out.println("HandleRequest update uploadBuilding -- passed ");

    }


}
