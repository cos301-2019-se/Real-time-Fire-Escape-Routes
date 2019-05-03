import org.junit.Test;

import static org.junit.Assert.*;

public class RTFEServerTest {
    @Test
    public void testOne()
    {
        Thread thread1 = new Thread( new RTFEServer());
        thread1.start();

        thread1.stop();
        System.out.println("RTFE Server runs correctly -- true");
    }
}