package Building;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/** 
* Person Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug 2, 2019</pre> 
* @version 1.0 
*/ 
public class PersonTest {
    Person test;
    @Before
public void before() throws Exception {
    double [] pos = {1,2};
    test= new Person("test",pos );
    Person.numPeople=0;
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: setPosition(double[] p) 
* 
*/ 
@Test
public void testSetPosition() throws Exception {

    double [] pos = {3,4};
    String expected = Arrays.toString(pos);
    test.setPosition(pos);
    Assert.assertEquals(expected,Arrays.toString(test.getPosition()));
} 

/** 
* 
* Method: setAssignedRoute(Routes assignedRoute) 
* 
*/ 
@Test
public void testSetAssignedRoute() throws Exception {
    Routes r = new Routes("testingRoute");
    test.setAssignedRoute(r);
    Assert.assertEquals(r, test.getAssignedRoute());
} 

/** 
* 
* Method: getPersonID() 
* 
*/ 
@Test
public void testGetPersonID() throws Exception {
    int expected = 0;
    Assert.assertEquals(expected, test.personID);
} 

/** 
* 
* Method: getName() 
* 
*/ 
@Test
public void testGetName() throws Exception {
    String expected = "test";
    Assert.assertEquals(expected, test.name);
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPosition() 
* 
*/ 
@Test
public void testGetPosition() throws Exception {
    double [] pos = {1,2};
    String expected = Arrays.toString(pos);
    Assert.assertEquals(expected,Arrays.toString(test.getPosition()));
} 

/** 
* 
* Method: getAssignedRoute() 
* 
*/ 
@Test
public void testGetAssignedRoute() throws Exception {
    Assert.assertEquals(null, test.getAssignedRoute());

} 


} 
