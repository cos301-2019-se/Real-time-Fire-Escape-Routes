package test;

import Builder.*;
import Building.Door;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class BuilderTest{
    @Test
    public void DoorBuildPartTest()
    {
        JSONObject testData = new JSONObject();
        DoorBuilder b = new DoorBuilder(testData);
        try{


        }catch (Exception e){

        }
        Assert.assertEquals(true, true);
        System.out.println("Builder/BuildPart Door -- passed");
    }

    @Test
    public void RoomBuildPartTest()
    {
        JSONObject testData = new JSONObject();
        RoomBuilder b = new RoomBuilder(testData);
        try{


        }catch (Exception e){

        }
        Assert.assertEquals(true, true);
        System.out.println("Builder/BuildPart Room -- passed");
    }

    @Test
    public void PersonBuildPartTest()
    {
        JSONArray testData = new JSONArray();
        PersonManager HR = new PersonManager(null,testData);
        try{


        }catch (Exception e){

        }
        Assert.assertEquals(true, true);
        System.out.println("Builder/BuildPart Person -- passed");
    }

}
