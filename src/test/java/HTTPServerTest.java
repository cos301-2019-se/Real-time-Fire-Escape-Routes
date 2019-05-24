<<<<<<< HEAD:Vesrion1.0.1/src/HTTPServerTest.java
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
=======
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
>>>>>>> TravisTesting:src/test/java/HTTPServerTest.java
}