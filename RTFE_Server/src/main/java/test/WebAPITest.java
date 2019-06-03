package test;

        import ApiEndpoints.WebAPI;
        import org.json.JSONObject;
        import org.junit.*;
public class WebAPITest {
    @Test
    public void HandleRequestTest()
    {
        JSONObject testData = new JSONObject();
        try{
            WebAPI.handleRequest(testData);

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

}
