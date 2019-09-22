﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;
using UnityEngine.AI;
using UnityEngine.SceneManagement;


public class routes
{
    public int numRoutes;
    public string people;
    public string status;
    public string emergency;
    public string fires;
}

public class TemporaryPeople
{
    public float personId;
    public List<Vector3> routeList = new List<Vector3>();
}

public class TemporaryExit
{
    public Vector3 position;
}

public class Counter : MonoBehaviour
{
    public bool isSimulation = false;
    public bool runOnce;
    public GameObject buildingOb;
    // public GameObject meshOb;
    public NavMeshSurface s;
    public int alarm = -1;
    public bool once = true;
    public bool emergency = false;
    public float offset;


    //private string ip = "http://127.0.0.1:8080/";
    private string ip = "http://192.168.137.1:8080/";
    //private string ip = "http://192.168.43.237:8080/";
    // private string ip = "https://6c53bafd-db31-4e2e-aac4-49c2a447c8ad.mock.pstmn.io/";

    // Start is called before the first frame update
    void Start()
    {
        Debug.Log("+++++++++++++++ " + SceneManager.GetActiveScene().name);
       
        runOnce = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.G))
        {
            if (SceneManager.GetActiveScene().name == "SampleScene1")
            {
                SceneManager.LoadSceneAsync("SampleScene2");
            }
            if (SceneManager.GetActiveScene().name == "SampleScene2")
            {
                SceneManager.LoadSceneAsync("SampleScene1");
            }
        }

        if (!runOnce)//calls data from server to get how building must look only calls server again once building is built
        {
            buildingOb = Instantiate(Resources.Load("Builder", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
            //yield return new WaitForSeconds(1.0f);
            if (isSimulation)
                StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\",\"mode\":\"simulation\"}", "buildingGeneration"));
            else
                StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\"}", "buildingGeneration"));
            runOnce = true;
        }
        if (Input.GetKeyDown("space"))
        {
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
        }
        if (Input.GetKeyDown(KeyCode.F))
        {
            if( alarm != -1)
            {
                alarm = -1;
                if (alarm != -1)
                {
                    string s = findPeople();
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\",\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\"}", "assignPeople"));
                }
                else
                {
                    Debug.Log("again");
                   // yield return new WaitForSeconds(5.0f);
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+false+",\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));

                }
            }
            else
            {
                alarm = 0;  
                once = true;
            }    
        }
    }

    string findPeople()
    {
        string allPeople = "";
        var goArray2 = FindObjectsOfType<GameObject>();
        for (int j = 0; j < goArray2.Length; j++)
        {
            if (goArray2[j].GetComponent<number>() != null)
            {

                allPeople += goArray2[j].GetComponent<number>().objectNumber.ToString() + "&";
                allPeople += Mathf.RoundToInt(Mathf.Floor(goArray2[j].transform.position.y) / 3) + "&";
                allPeople += goArray2[j].transform.position.x.ToString() + "&";
                if (j < goArray2.Length - 1)
                    allPeople += goArray2[j].transform.position.z.ToString() + "-";
                else
                    allPeople += goArray2[j].transform.position.z.ToString();

            }
        }

        return allPeople;

    }

    private List<TemporaryPeople> createPeopleList(string allPeople)
    {
        List<TemporaryPeople> peopleList = new List<TemporaryPeople>();
        allPeople = allPeople.Replace(" ", string.Empty);
        string[] people = allPeople.Split('-');
        
        for (int i = 0; i < people.Length; i++)
        {
            TemporaryPeople person = new TemporaryPeople();

            string[] people1 = people[i].Split('*');
            person.personId = float.Parse(people1[0], System.Globalization.CultureInfo.InvariantCulture);//getting person number
            string[] allRoutes = people1[1].Split('%');

            for (int j = 0; j < allRoutes.Length; j++)
            {
                string[] x3 = allRoutes[j].Split(',');
                float yfloor = float.Parse(x3[0], System.Globalization.CultureInfo.InvariantCulture);
                float xpos = float.Parse(x3[1], System.Globalization.CultureInfo.InvariantCulture);
                float zpos = float.Parse(x3[2], System.Globalization.CultureInfo.InvariantCulture);
                person.routeList.Add(new Vector3(xpos, (yfloor * 3f) + 1.5f, zpos+(offset*1000.0f)));
               // person.routeList.Add(new Vector3(xpos, yfloor , zpos));
            }

            peopleList.Add(person);
        }
        return peopleList;
    }

    private List<TemporaryExit> createExitList(List<TemporaryPeople> allPeople)
    {
        List<TemporaryExit> exitList = new List<TemporaryExit>();
        for(int i = 0; i < allPeople.Count; i++)
        {
            bool foundMatch = false;
            for(int j = 0; j < exitList.Count; j++)
            {
                if(exitList[j].position.x == allPeople[i].routeList[allPeople[i].routeList.Count-1].x &&
                    exitList[j].position.y == allPeople[i].routeList[allPeople[i].routeList.Count-1].y &&
                    exitList[j].position.z == allPeople[i].routeList[allPeople[i].routeList.Count-1].z)
                    {
                        foundMatch = true;
                    }
            }

            if(!foundMatch)
            {
                if(allPeople[i].routeList.Count - 1 >= 0)
                {
                    TemporaryExit newExit = new TemporaryExit();
                    newExit.position = new Vector3(allPeople[i].routeList[allPeople[i].routeList.Count - 1].x, allPeople[i].routeList[allPeople[i].routeList.Count - 1].y, allPeople[i].routeList[allPeople[i].routeList.Count - 1].z);
                    exitList.Add(newExit);
                }  
            }
        }
        return exitList;
    }

    private void addPeopleIntoSimulation(List<TemporaryPeople> allPeople, bool localEmergency)
    {
        var goArray2 = FindObjectsOfType<GameObject>();
        for (int j = 0; j < goArray2.Length; j++)
        {
            if (goArray2[j].GetComponent<number>() != null)
            {
                goArray2[j].GetComponent<number>().exists = false;
            }
        }

        var allGameObjects = FindObjectsOfType<GameObject>();
        for (int i = 0; i < allPeople.Count; i++)
        {              
            bool FoundPerson = false;

            for (int j = 0; j < allGameObjects.Length; j++)
            {
                if (allGameObjects[i].GetComponent<number>() != null)
                {
                    if (allGameObjects[j].GetComponent<AgentController>() != null)
                    {
                        if (allGameObjects[j].GetComponent<number>().objectNumber == allPeople[i].personId)
                        {
                            FoundPerson = true;
                            allGameObjects[j].GetComponent<AgentController>().goTo(allPeople[i].routeList, localEmergency);
                            allGameObjects[j].GetComponent<number>().exists = true;
                        }
                    }
                }
            }

            if(!FoundPerson)
            {
                buildingOb.GetComponent<Building>().addPerson(allPeople[i].routeList, allPeople[i].personId, null, localEmergency);
            }           
        }

        var goArray1 = FindObjectsOfType<GameObject>();
        for (int j = 0; j < goArray1.Length; j++)
        {
            if (goArray1[j].GetComponent<number>() != null)
            {
                if (goArray1[j].GetComponent<number>().exists == false)
                {
                    Destroy(goArray1[j]);
                }
            }

            if (goArray1[j].GetComponent<AgentController>() != null)
            {
                goArray1[j].GetComponent<AgentController>().emergency = localEmergency;
            }
        }
    }

    private void placeExits(List<TemporaryExit> exitList)
    {
        for(int i = 0; i < exitList.Count; i++)
        {
                 
            GameObject go = Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(exitList[i].position.x, exitList[i].position.y, exitList[i].position.z), new Quaternion(0, 0, 0, 1)) as GameObject;
            go.GetComponent<number>().type = "exit";
            string r = "route" + (i+1);
            Material myMaterial = Resources.Load("materials/" + r) as Material;
            go.GetComponent<Renderer>().material = myMaterial;
        }
    }

    private void AssignPeopleToColor()
    {
        var goArray1 = FindObjectsOfType<GameObject>();
        for (int j = 0; j < goArray1.Length; j++)
        {
            if (goArray1[j].GetComponent<number>() != null)
            {
                if (goArray1[j].GetComponent<number>().type == "exit")
                {
                    for (int i = 0; i < goArray1.Length; i++)
                    {
                        if (goArray1[i].GetComponent<AgentController>() != null)
                        {
                            if(goArray1[i].GetComponent<AgentController>().listRoute.Count - 1 >= 0)
                            if (goArray1[i].GetComponent<AgentController>().listRoute[goArray1[i].GetComponent<AgentController>().listRoute.Count - 1]
                                == goArray1[j].transform.position)
                            {
                                Color color = goArray1[j].GetComponent<Renderer>().material.color;
                                goArray1[i].GetComponent<Renderer>().material.color = color;
                            }
                        }
                    }
                }
            }
        }
    }

    public void CreateFireObject(string firestring)
    {
        Debug.Log("inside fire object maker");
        if (firestring != "")
        {
            firestring = firestring.Replace(" ", string.Empty);
            string[] fires = firestring.Split('-');
            //Fire: 0 * 4.5,0.0 % 4.5,4.0 % 0.0,4.0 % 0.0,0.0
            for(int i = 0; i < fires.Length; i++)
            {
                string[] splitFloor = fires[i].Split('*');
                float floorNumber = float.Parse(splitFloor[0], System.Globalization.CultureInfo.InvariantCulture);
                List<Vector2> fireCornerList = new List<Vector2>();
                string[] corners = splitFloor[1].Split('%');

                for(int j = 0; j < corners.Length; j++)
                {                 
                    string[] xz = corners[j].Split(',');
                    float x = float.Parse(xz[0], System.Globalization.CultureInfo.InvariantCulture);
                    float z = float.Parse(xz[1], System.Globalization.CultureInfo.InvariantCulture);
                    fireCornerList.Add(new Vector2(x, z));
                   // Debug.Log(floorNumber + " " +xz[0]+" "+xz[1]);
                }
                buildingOb.GetComponent<Building>().addFire(fireCornerList, floorNumber);
            }
        }
        else
        {
            Debug.Log("Fire string is empty");
        }
    }

    IEnumerator postRequest(string url, string json, string type)
    {
        var uwr = new UnityWebRequest(url, "POST");
        byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(json);
        uwr.uploadHandler = (UploadHandler)new UploadHandlerRaw(jsonToSend);
        uwr.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer();
        uwr.SetRequestHeader("Content-Type", "application/json");

        //Send the request then wait here until it returns
        yield return uwr.SendWebRequest();

        if (uwr.isNetworkError)
        {
            Debug.Log("Error While Sending: " + uwr.error);
             if (alarm != -1)
                {
                    string s = findPeople();
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\",\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\"}", "assignPeople"));
                }
                else
                {
                    Debug.Log("again");
                   // yield return new WaitForSeconds(5.0f);
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+false+",\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));

                }
        }
        else
        {
            if (type == "buildingGeneration")
            {
                buildingString myObject = JsonUtility.FromJson<buildingString>(uwr.downloadHandler.text);

                if (myObject.rooms == null)
                {
                    Debug.Log("No rooms recalling");
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\",\"mode\":\"simulation\"}", "buildingGeneration"));
                    else
                        StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\"}", "buildingGeneration"));
                }
                else
                {
                    buildingOb.GetComponent<Building>().addStrings(myObject);
                    buildingOb.GetComponent<Building>().createArrays();
                    s.BuildNavMesh();
                    if (alarm != -1)
                    {
                        string s = findPeople();
                        if (isSimulation)
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":" + true + "," + "\"peopleLocations\":\"" + s + "\",\"mode\":\"simulation\"}", "assignPeople"));
                        else
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":" + true + "," + "\"peopleLocations\":\"" + s + "\"}", "assignPeople"));
                    }
                    else
                    {
                        Debug.Log("again");
                        // yield return new WaitForSeconds(5.0f);
                        if (isSimulation)
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":" + false + ",\"mode\":\"simulation\"}", "assignPeople"));
                        else
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));

                    }
                }
            }
            else if (type == "assignPeople")
            {
                routes myObject = JsonUtility.FromJson<routes>(uwr.downloadHandler.text);
                Debug.Log("my People: " + myObject.people);
                Debug.Log("status: " + myObject.status);
                Debug.Log("emergency: " + myObject.emergency);
                Debug.Log("Fire: " + myObject.fires);

                //if(myObject.fires == "")//delete this whole if statement later
                //{
                //    Debug.Log("Fire is empty putting in fake data");
                //    myObject.fires = "0 * 4.5,0.0 % 4.5,4.0 % 0.0,4.0 % 0.0,0.0";
                //    //0 * 5.8,4.0 % 5.8,8.0 % 10.0,8.0 % 10.0,4.0

                //}
                //Fire: 0 * 4.5,0.0 % 4.5,4.0 % 0.0,4.0 % 0.0,0.0

                if (myObject != null)
                {
                    if(myObject.people != "" && myObject.status != "failed")
                    {
                        bool localEmergency;
                        if(myObject.emergency == "true")
                            localEmergency = true;
                        else
                            localEmergency = false;

                        //creates a list of all people(ID and routes)
                        List<TemporaryPeople> peopleList = createPeopleList(myObject.people);
                        Debug.Log("number of people: " + peopleList.Count);

                        //add all new people into simulation,
                        addPeopleIntoSimulation(peopleList, localEmergency);

                        if(myObject.emergency == "true")
                        {
                            //create a list of all exits(position using vector3)
                            List<TemporaryExit> exitList = createExitList(peopleList);
                            Debug.Log("number of exits: "+exitList.Count);

                            //creates exit gameobjects and assigns a color
                            placeExits(exitList);

                            //assigns people to their colors
                            AssignPeopleToColor();

                            Debug.Log("Calling fire object maker");
                            CreateFireObject(myObject.fires);
                        }
                        
                        if (peopleList == null)
                        {
                            Debug.Log("ERROR: SOMETHING WENT WRONG YOU SHOULD NOT SEE THIS I THINK");
                        }
                    }
                    else
                    {
                        Debug.Log("ERROR: IETHER PEOPLE ARRAY IS EMPTY OR STATUS WAS FAILED");
                    }
                }
                else
                {
                    Debug.Log("ERROR: my object is equal to null should call again");
                }


                yield return new WaitForSeconds(1.0f);
                //findPeople();
                if (alarm != -1)
                {
                    if(once)
                    {
                        once = false;
                        string s = findPeople();
                        if (isSimulation)
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\",\"mode\":\"simulation\"}", "assignPeople"));
                        else
                            StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+true+"," + "\"peopleLocations\":\"" + s + "\"}", "assignPeople"));
                    }
                }
                else
                {
                    Debug.Log("again");
                   // yield return new WaitForSeconds(5.0f);
                    if (isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":"+false+",\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));

                }
              
            }
        }
    }
}