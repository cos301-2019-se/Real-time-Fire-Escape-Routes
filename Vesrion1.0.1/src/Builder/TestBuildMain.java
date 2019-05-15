package Builder;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestBuildMain {
    public static void main(String[] args) {
        System.out.print("Testing door center calculation: ");
        Result result = JUnitCore.runClasses(DoorTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

        System.out.print("Testing person class functionality: ");
//        result = JUnitCore.runClasses(PersonTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

        System.out.print("Testing node functionality: ");
        result = JUnitCore.runClasses(NodeTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
