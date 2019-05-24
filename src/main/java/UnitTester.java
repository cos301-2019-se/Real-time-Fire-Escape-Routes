
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.*;

import java.util.Vector;

public class UnitTester {
    private static Vector<Object> Classes;

    public static void main(String[] args) {
        JUnitCore.runClasses();
        Vector<Class> classesToTest = new Vector<>();
        classesToTest.add(WebAPITest.class);
        classesToTest.add(BuildingAPITest.class);
        classesToTest.add(BuildingGenerationAPITest.class);
        classesToTest.add(BuilderTest.class);
//        classesToTest.add(DatabaseTest.class);

        System.out.println("================ Unit Tests ====================");

        for (int i = 0; i < classesToTest.size(); i++) {
            Class test = (Class) classesToTest.get(i);
            System.out.println("Currently testing: "+test.getName());
            Result Currentresult = JUnitCore.runClasses(test);
            if(Currentresult.wasSuccessful() == false) {
                for (Failure failure : Currentresult.getFailures()) {
                    System.out.println(failure.toString());
                }
            }
            System.out.println();
        }

//        System.out.println("Testing HTTP Server: ");
//        Result result = JUnitCore.runClasses(HTTPServerTest.class);
//
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//        System.out.println(result.wasSuccessful());

//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//        System.out.println(result.wasSuccessful());
    }
}
