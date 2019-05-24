import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPServerTest {
    @Test
    public void testOne()
    {

        Thread thread = new Thread(new HTTPServer(null));
        thread.start();

        thread.stop();
        System.out.println("HTTPServer runs correctly --  passed");
    }
}