
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class ServerTester {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DatabaseTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

        System.out.print("Testing HTTP Server: ");
        result = JUnitCore.runClasses(HTTPServerTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        System.out.print("Testing RTFE Server: ");
        result = JUnitCore.runClasses(RTFEServerTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
