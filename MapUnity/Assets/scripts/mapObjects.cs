using System.Collections;
using System.Collections.Generic;
using UnityEngine;
public class room
{
    public int id;
    public float [] x = new float[2];
    public float [] y = new float[2];
    public float [] z = new float[2];
}

public class door
{
    public int id;
    public float[] x = new float[2];
    public float[] y = new float[2];
    public float[] z = new float[2];
}

public class agent
{
    public int id;
    public float[] position = new float[3];

}

public class mapObjects
{   
    public string type = "unity";
    public agent [] people;
    public door [] doors;
    public room [] rooms;

    //public List<GameObject> doorList  = new List<GameObject>();
    //public List<GameObject> roomList = new List<GameObject>();
    //public List<GameObject> agentList = new List<GameObject>();
}


public class agentInstructions
{
    public bool status;
    public string msg;


    //public List<GameObject> doorList  = new List<GameObject>();
    //public List<GameObject> roomList = new List<GameObject>();
    //public List<GameObject> agentList = new List<GameObject>();
}

public class fullInstructions
{
    public bool type;
    public string floors;
    public string halls;
    public string rooms;
    public string people;
    //public List<GameObject> doorList  = new List<GameObject>();
    //public List<GameObject> roomList = new List<GameObject>();
    //public List<GameObject> agentList = new List<GameObject>();
}
