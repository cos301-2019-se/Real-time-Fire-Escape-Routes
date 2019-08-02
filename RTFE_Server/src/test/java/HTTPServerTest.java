import org.junit.Assert;
import org.junit.Test;

public class HTTPServerTest {
    @Test
    public void CreatonOfHTTPThread()
    {

        Thread thread = new Thread(new HTTPServer());
        Assert.assertTrue(thread!=null);
        thread.start();
        Assert.assertTrue(thread.isAlive());

        thread.stop();
//        System.out.println("HTTPServer runs correctly --  passed");
    }
}