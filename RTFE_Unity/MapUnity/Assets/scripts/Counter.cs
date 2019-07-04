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

   public string ip = "http://127.0.0.1:8080/";


    // Start is called before the first frame update
    void Start()
    {
        runOnce = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (!runOnce)//calls data from server to get how building must look only calls server again once building is built
        {
            StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\"}", "buildingGeneration"));
            runOnce = true;
        }
        if(Input.GetKeyDown("space"))
         {
                SceneManager.LoadScene( SceneManager.GetActiveScene().buildIndex );
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
               // Debug.Log("Received: " + uwr.downloadHandler.text);
                buildingString myObject = JsonUtility.FromJson<buildingString>(uwr.downloadHandler.text);
                //Debug.Log("doors: "+myObject.doors);
                //Debug.Log("msg: "+myObject.msg);
                //Debug.Log("rooms: "+myObject.rooms);
                //Debug.Log("numberFloors: "+myObject.numberFloors);
                //Debug.Log("people: "+myObject.people);
                //Debug.Log("status: "+myObject.status);
                buildingOb.GetComponent<Building>().addStrings(myObject);
                buildingOb.GetComponent<Building>().createArrays();
                s.BuildNavMesh();
                StartCoroutine(postRequest(ip+"building", "{\"type\":\"assignPeople\"}","assignPeople"));
               // StartCoroutine(postRequest("http://192.168.1.39:8080/building", "{\"type\":\"assignPeople\"}", "assignPeople"));
            }
            else if(type == "assignPeople")
            {
                //Debug.Log("Received: " + uwr.downloadHandler.text);
                routes myObject = JsonUtility.FromJson<routes>(uwr.downloadHandler.text);
                // Debug.Log("numRoutes: "+myObject.numRoutes);
                //Debug.Log("People: "+myObject.people);
                // myObject.people = "15 * 29.0,10.2 % 30 * 29.0,10.2 % 32 * 29.0,10.2 % 13* 17.6,-5.0 % 0 * 29.0,10.2 % 13 * 29.0,10.2 % 20 * 29.0,10.2 % 31 * 29.0,10.2 % 6 * 29.0,10.2 % 11 * 29.0,10.2 % 28 * 29.0,10.2 % 2 * 29.0,10.2 % 7 * 29.0,10.2 % 9 * 29.0,10.2 % 27 * 29.0,10.2 % 12 * 29.0,10.2 % 14 * 29.0,10.2 % 21 * 29.0,10.2 % 25 * 26.0,10.2 % 23 * 29.0,10.2 % 17 * 29.0,10.2 % 24 * 29.0,10.2 % 22 * 29.0,10.2 % 35 * 29.0,10.2 % 29 * 29.0,10.2 % 34 * 29.0,10.2 % 8 * 29.0,10.2 % 5 * 29.0,10.2 % 33 * 29.0,10.2 % 10 * 29.0,10.2 % 18 * 29.0,10.2 % 19 * 29.0,10.2 % 1 * 17.6,-5.0 % 4 * 17.6,-5.0 % 26 * 17.6,-5.0 % 36 * 17.6,-5.0";
                // myObject.numRoutes = 3;


                //1*0,12.6,0.0 - 4*0,12.6,0.0 - 15*0,24.0,5.2 - 6*0,6.0,4.5%0,0.0,5.2
                myObject.people = myObject.people.Replace(" ", string.Empty);
                Debug.Log("people to assign: "+myObject.people);
                string[] x = myObject.people.Split('-');
                var values = new float[x.Length][];

                List<GameObject> Exits = new List<GameObject>();

                for (int i = 0; i < x.Length; i++)
                {
                    string[] x1 = x[i].Split('*');
                    float personNumber = float.Parse(x1[0], System.Globalization.CultureInfo.InvariantCulture);//getting person number
                    string[] x2 = x1[1].Split('%');
                    List<Vector3> pointList = new List<Vector3>();
                    for (int j = 0; j < x2.Length; j++)
                    {
                        string[] x3 = x2[j].Split(',');
                        float yfloor = float.Parse(x3[0], System.Globalization.CultureInfo.InvariantCulture);
                        float xpos = float.Parse(x3[1], System.Globalization.CultureInfo.InvariantCulture);
                        float zpos = float.Parse(x3[2], System.Globalization.CultureInfo.InvariantCulture);
                        pointList.Add(new Vector3(xpos, yfloor, zpos));
                    }

                    Vector3 go;
                    GameObject g = null; ;
                    if (Exits.Count == 0)
                    {
                        Exits.Add(Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(pointList[pointList.Count-1].x, pointList[pointList.Count-1].y+1.5f, pointList[pointList.Count-1].z), new Quaternion(0, 0, 0, 1)) as GameObject);
                        Exits[Exits.Count - 1].GetComponent<number>().x = pointList[pointList.Count - 1].x;
                        Exits[Exits.Count - 1].GetComponent<number>().y = pointList[pointList.Count - 1].y;
                        Exits[Exits.Count - 1].GetComponent<number>().z = pointList[pointList.Count - 1].z;
                        go = pointList[pointList.Count - 1];
                        g = Exits[Exits.Count - 1];
                        string r = "route" + Exits.Count;
                        Material myMaterial = Resources.Load("materials/" + r) as Material;
                        g.GetComponent<Renderer>().material = myMaterial;
                        Debug.Log("one" + r);
                    }
                    else
                    {
                        bool f = false;
                        for(int j = 0; j < Exits.Count;j++)
                        {
                            if(Exits[j].GetComponent<number>().x == pointList[pointList.Count - 1].x 
                            && Exits[j].GetComponent<number>().y == pointList[pointList.Count - 1].y 
                            && Exits[j].GetComponent<number>().z == pointList[pointList.Count - 1].z) 
                            {
                                go = new Vector3(Exits[j].GetComponent<number>().x, Exits[j].GetComponent<number>().y, Exits[j].GetComponent<number>().z);
                                f = true;
                                g = Exits[j];
                                Debug.Log("two");
                            }
                        }
                        if(!f)
                        {
                            //Exits.Add(Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(pointList[pointList.Count - 1].x, pointList[pointList.Count - 1].y+1.5f, pointList[pointList.Count - 1].z), new Quaternion(0, 0, 0, 1)) as GameObject);

                            //go = pointList[pointList.Count - 1];
                            //g = Exits[Exits.Count-1];
                            //string r = "route" + Exits.Count;
                            //Material myMaterial = Resources.Load("materials/" + r) as Material;
                            //g.GetComponent<Renderer>().material = myMaterial;
                            //Debug.Log("three"+r);
                            Exits.Add(Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(pointList[pointList.Count - 1].x, pointList[pointList.Count - 1].y + 1.5f, pointList[pointList.Count - 1].z), new Quaternion(0, 0, 0, 1)) as GameObject);
                            Exits[Exits.Count - 1].GetComponent<number>().x = pointList[pointList.Count - 1].x;
                            Exits[Exits.Count - 1].GetComponent<number>().y = pointList[pointList.Count - 1].y;
                            Exits[Exits.Count - 1].GetComponent<number>().z = pointList[pointList.Count - 1].z;
                            go = pointList[pointList.Count - 1];
                            g = Exits[Exits.Count - 1];
                            string r = "route" + Exits.Count;
                            Material myMaterial = Resources.Load("materials/" + r) as Material;
                            g.GetComponent<Renderer>().material = myMaterial;
                            Debug.Log("one" + r);
                        }
                    }
                 
                    var goArray = FindObjectsOfType<GameObject>();

                    for (int j = 0; j < goArray.Length; j++)
                    {
                        if (goArray[j].GetComponent<number>() != null)
                        {
                            if (goArray[j].GetComponent<number>().objectNumber == personNumber)
                            {
                                if (goArray[j].GetComponent<AgentController>() != null)
                                {
                                //Debug.Log("x: "+gotoExit.GetComponent<number>().x+" y: "+gotoExit.GetComponent<number>().y + " z: " + gotoExit.GetComponent<number>().z);
                                    goArray[j].GetComponent<AgentController>().goTo(pointList);
                                    if (g != null)
                                        goArray[j].GetComponent<AgentController>().Setcolor(g);
                                    else
                                        Debug.Log("whaaaat");
                                }
                            }
                        }
                    }
                }



                /*
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
                */
            }
        }
    }
}
