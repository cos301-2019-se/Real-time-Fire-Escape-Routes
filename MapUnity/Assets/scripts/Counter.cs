using System.Collections;
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
}

public class Counter : MonoBehaviour
{
    bool runOnce;
    public GameObject buildingOb;
   // public GameObject meshOb;
     public NavMeshSurface s;
    public string ip = "http://192.168.1.41:8080/";

    // Start is called before the first frame update
    void Start()
    {
        runOnce = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (!runOnce)
        {
            findAll();
            runOnce = true;
        }
        if(Input.GetKeyDown("space"))
         {
                SceneManager.LoadScene( SceneManager.GetActiveScene().buildIndex ) ;
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
        }
        else
        {
            if(type == "buildingGeneration")
            {
                Debug.Log("Received: " + uwr.downloadHandler.text);
                buildingString myObject = JsonUtility.FromJson<buildingString>(uwr.downloadHandler.text);
                Debug.Log("doors: "+myObject.doors);
                Debug.Log("msg: "+myObject.msg);
                Debug.Log("rooms: "+myObject.rooms);
                Debug.Log("numberFloors: "+myObject.numberFloors);
                Debug.Log("people: "+myObject.people);
                Debug.Log("status: "+myObject.status);
                buildingOb.GetComponent<Building>().addStrings(myObject);
                buildingOb.GetComponent<Building>().createArrays();
                s.BuildNavMesh();
                StartCoroutine(postRequest(ip+"building", "{\"type\":\"assignPeople\"}","assignPeople"));
               // StartCoroutine(postRequest("http://192.168.1.39:8080/building", "{\"type\":\"assignPeople\"}", "assignPeople"));
            }
            else if(type == "assignPeople")
            {
                Debug.Log("Received: " + uwr.downloadHandler.text);
                routes myObject = JsonUtility.FromJson<routes>(uwr.downloadHandler.text);
                Debug.Log("numRoutes: "+myObject.numRoutes);
                Debug.Log("People: "+myObject.people);

               // myObject.people = "15 * 29.0,10.2 % 30 * 29.0,10.2 % 32 * 29.0,10.2 % 13* 17.6,-5.0 % 0 * 29.0,10.2 % 13 * 29.0,10.2 % 20 * 29.0,10.2 % 31 * 29.0,10.2 % 6 * 29.0,10.2 % 11 * 29.0,10.2 % 28 * 29.0,10.2 % 2 * 29.0,10.2 % 7 * 29.0,10.2 % 9 * 29.0,10.2 % 27 * 29.0,10.2 % 12 * 29.0,10.2 % 14 * 29.0,10.2 % 21 * 29.0,10.2 % 25 * 26.0,10.2 % 23 * 29.0,10.2 % 17 * 29.0,10.2 % 24 * 29.0,10.2 % 22 * 29.0,10.2 % 35 * 29.0,10.2 % 29 * 29.0,10.2 % 34 * 29.0,10.2 % 8 * 29.0,10.2 % 5 * 29.0,10.2 % 33 * 29.0,10.2 % 10 * 29.0,10.2 % 18 * 29.0,10.2 % 19 * 29.0,10.2 % 1 * 17.6,-5.0 % 4 * 17.6,-5.0 % 26 * 17.6,-5.0 % 36 * 17.6,-5.0";
              // myObject.numRoutes = 3;

                myObject.people = myObject.people.Replace(" ", string.Empty);
                string [] x = myObject.people.Split('%');
                var values = new float[x.Length][];
                for(int i = 0; i < x.Length; i++)
                {
                    values[i] = new float[3];
                    string[] x1 = x[i].Split('*');
                    values[i][0] = float.Parse(x1[0], System.Globalization.CultureInfo.InvariantCulture);
                    string[] x2 = x1[1].Split(',');
                   
                    values[i][1] = float.Parse(x2[0], System.Globalization.CultureInfo.InvariantCulture);
                    values[i][2] = float.Parse(x2[1], System.Globalization.CultureInfo.InvariantCulture);
                }

                GameObject[] Exits = new GameObject[myObject.numRoutes];
                for(int i = 0; i < myObject.numRoutes; i++)
                    Exits[i] = null;

                for(int i = 0; i < values.Length; i++)//people
                {
                    GameObject gotoExit = null;
                    for(int j = 0; j < myObject.numRoutes; j++)//exits
                    {
                        if(Exits[j] != null)
                        {
                            if(Exits[j].GetComponent<number>().x== values[i][1] && Exits[j].GetComponent<number>().z == values[i][2])
                            {
                                gotoExit = Exits[j];
                                break;
                            } 
                        }
                        else
                        {
                            Exits[j] = Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(values[i][1], 1,values[i][2]), new Quaternion(0, 0, 0, 1)) as GameObject;
                            
                            Exits[j].GetComponent<number>().x = values[i][1];
                            Exits[j].GetComponent<number>().y = 1;
                            Exits[j].GetComponent<number>().z = values[i][2];
                            
                            gotoExit = Exits[j];
                            //Debug.Log("new x: "+gotoExit.GetComponent<number>().x+"new y: "+gotoExit.GetComponent<number>().y);
                            //Debug.Log("new");
                            string r = "route"+(i+1);
                            //Debug.Log(r);
                            Material myMaterial = Resources.Load("materials/"+r) as Material; 
                            gotoExit.GetComponent<Renderer>().material = myMaterial; 
                            break;
                        }
                    }

                    var goArray = FindObjectsOfType<GameObject>();

                    for (int j = 0; j < goArray.Length; j++)
                    {
                        if(goArray[j].GetComponent<number>() != null)
                        {
                            if (goArray[j].GetComponent<number>().objectNumber == values[i][0])
                            {
                                if(goArray[j].GetComponent<AgentController>() != null)
                                {
                                    if(gotoExit != null)
                                    {
                                        //Debug.Log("x: "+gotoExit.GetComponent<number>().x+" y: "+gotoExit.GetComponent<number>().y + " z: " + gotoExit.GetComponent<number>().z);
                                        goArray[j].GetComponent<AgentController>().goTo(gotoExit);
                                    }
                                } 
                            }
                        }
                    }
                }
            }
     

            // string [] x = myObject.msg.Split(',');
            // var x1 = new string[x.Length][];
            // for(int i = 0; i < x.Length; i++)
            // {
            //     x1[i] = x[i].Split('-');
            // }


            // int [][] x2 = new int[x.Length][];
            // for (int i = 0; i < x.Length; i++)
            // {
            //     x2[i] = new int[2];
            //     for (int j = 0; j < 2; j++)
            //     {
            //         //x2[i][j] = (int)x1[i][j];
            //         x2[i][j] = int.Parse(x1[i][j]);
            //         Debug.Log(x2[i][j]);
            //     }
            // }

            // var goArray = FindObjectsOfType<GameObject>();
            // for(int i =0; i < x.Length; i++)
            // {
            //     for (int j = 0; j < goArray.Length; j++)
            //     {
            //         if(goArray[j].GetComponent<number>() != null)
            //         if (goArray[j].GetComponent<number>().objectNumber == x2[i][0])
            //         {
            //             goArray[j].GetComponent<AgentController>().goTo(x2[i][1]);
            //         }
            //     }
            // }

        }
    }
   // [{9,3},{2,6}]
    void findAll()
    {
        var goArray = FindObjectsOfType<GameObject>();
        var doorList = new List<door>();
        var roomList = new List<room>();
        var agentList = new List<agent>();

        for (var i = 0; i < goArray.Length; i++) 
        {
            if (goArray[i].layer == 10) //room = 10
            {
                room tempRoom = new room();
                tempRoom.id = i;
                tempRoom.x[0] = goArray[i].GetComponent<Renderer>().bounds.min.x;
                tempRoom.x[1] = goArray[i].GetComponent<Renderer>().bounds.max.x;

                tempRoom.y[0] = goArray[i].GetComponent<Renderer>().bounds.min.y;
                tempRoom.y[1] = goArray[i].GetComponent<Renderer>().bounds.max.y;

                tempRoom.z[0] = goArray[i].GetComponent<Renderer>().bounds.min.z;
                tempRoom.z[1] = goArray[i].GetComponent<Renderer>().bounds.max.z;
                goArray[i].GetComponent<number>().objectNumber = i;
                roomList.Add(tempRoom);

            }
            else if (goArray[i].layer == 11) //door = 11
            {
                door tempDoor = new door();
                tempDoor.id = i;
                tempDoor.x[0] = goArray[i].GetComponent<Renderer>().bounds.min.x;
                tempDoor.x[1] = goArray[i].GetComponent<Renderer>().bounds.max.x;

                tempDoor.y[0] = goArray[i].GetComponent<Renderer>().bounds.min.y;
                tempDoor.y[1] = goArray[i].GetComponent<Renderer>().bounds.max.y;

                tempDoor.z[0] = goArray[i].GetComponent<Renderer>().bounds.min.z;
                tempDoor.z[1] = goArray[i].GetComponent<Renderer>().bounds.max.z;
                goArray[i].GetComponent<number>().objectNumber = i;
                doorList.Add(tempDoor);
            }
            else if (goArray[i].layer == 8) //agent = 8
            {
                agent tempAgent = new agent();
                tempAgent.id = i;
                tempAgent.position[0] = goArray[i].GetComponent<Renderer>().transform.position.x;
                tempAgent.position[1] = goArray[i].GetComponent<Renderer>().transform.position.y;
                tempAgent.position[2] = goArray[i].GetComponent<Renderer>().transform.position.z;
                goArray[i].GetComponent<number>().objectNumber = i;
                agentList.Add(tempAgent);
            }
            else
            {

            }
        }

        if (doorList.Count == 0) 
        {
             //Debug.Log("No Doors");
        }
        if (roomList.Count == 0) 
        {
            //Debug.Log("No Rooms");
        }
        if (agentList.Count == 0) 
        {
            //Debug.Log("No Agents");
        }

        mapObjects mo = new mapObjects();
        mo.people = agentList.ToArray();
        mo.rooms = roomList.ToArray();
        mo.doors = doorList.ToArray();

        Debug.Log("doors: "+doorList.Count+" rooms: "+roomList.Count+" agents: "+agentList.Count);

        string json = JsonConvert.SerializeObject(mo);

        Debug.Log(json);
        //StartCoroutine(postRequest("http://127.0.0.1:5000/", json));
        //StartCoroutine(postRequest("http://10.5.50.231:8080/", json));//kinson
       //StartCoroutine(postRequest("http://10.5.50.202:8080/", json));//kinson


        StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\"}","buildingGeneration"));//kinson
    }
}
