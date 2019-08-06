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
    public string status;
    public string emergency;
}

public class Counter : MonoBehaviour
{
    public bool isSimulation = false;
    bool runOnce;
    public GameObject buildingOb;
   // public GameObject meshOb;
     public NavMeshSurface s;
    public bool alarm;
    public bool alarmOnce;

    //private string ip = "http://127.0.0.1:8080/";
    private string ip = "http://192.168.137.1:8080/";
    //private string ip = "http://192.168.43.237:8080/";
    // private string ip = "https://6c53bafd-db31-4e2e-aac4-49c2a447c8ad.mock.pstmn.io/";


    // Start is called before the first frame update
    void Start()
    {
        alarm = false;
        alarmOnce = false;
        runOnce = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (!runOnce)//calls data from server to get how building must look only calls server again once building is built
        {
            if(isSimulation)
                StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\",\"mode\":\"simulation\"}", "buildingGeneration"));
            else
                StartCoroutine(postRequest(ip + "buildingGeneration", "{\"type\":\"buildingData\"}", "buildingGeneration"));
            runOnce = true;
        }
        if(Input.GetKeyDown("space"))
         {
                SceneManager.LoadScene( SceneManager.GetActiveScene().buildIndex );
         }
        if (Input.GetKeyDown(KeyCode.F))
        {
            alarm = !alarm;

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
                
                allPeople += goArray2[j].GetComponent<number>().objectNumber.ToString()+"&";
                allPeople += Mathf.RoundToInt( Mathf.Floor(goArray2[j].transform.position.y)/3) + "&";
                allPeople += goArray2[j].transform.position.x.ToString() + "&";
                if(j < goArray2.Length-1)
                    allPeople += goArray2[j].transform.position.z.ToString() + "-";
                else
                    allPeople += goArray2[j].transform.position.z.ToString();

            }
        }

        return allPeople;

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
            if (type == "buildingGeneration")
            {
                buildingString myObject = JsonUtility.FromJson<buildingString>(uwr.downloadHandler.text);
                buildingOb.GetComponent<Building>().addStrings(myObject);
                buildingOb.GetComponent<Building>().createArrays();
                s.BuildNavMesh();
                if (!alarm)
                {   if(isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false,\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));
                }
                else
                {
                    if(isSimulation)
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":true,\"mode\":\"simulation\"}", "assignPeople"));
                    else
                        StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":true}", "assignPeople"));
                }// StartCoroutine(postRequest("http://192.168.1.39:8080/building", "{\"type\":\"assignPeople\"}", "assignPeople"));
            }
            else if (type == "assignPeople")
            {
                var goArray2 = FindObjectsOfType<GameObject>();
                for (int j = 0; j < goArray2.Length; j++)
                {
                    if (goArray2[j].GetComponent<number>() != null)
                    {
                        goArray2[j].GetComponent<number>().exists = false;
                    }
                }
                routes myObject = JsonUtility.FromJson<routes>(uwr.downloadHandler.text);

                if(myObject != null)
                if (myObject.people != "" && myObject.status != "failed")
                {
                    myObject.people = myObject.people.Replace(" ", string.Empty);
                    Debug.Log("people to assign: " + myObject.people);
                    Debug.Log("if is emergency: " + myObject.emergency);

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
                            pointList.Add(new Vector3(xpos, (yfloor * 3f) + 1.5f, zpos));
                        }

                        Vector3 go;
                        GameObject g = null; ;
                        if (Exits.Count == 0)
                        {
                            Exits.Add(Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(pointList[pointList.Count - 1].x, pointList[pointList.Count - 1].y, pointList[pointList.Count - 1].z), new Quaternion(0, 0, 0, 1)) as GameObject);
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
                            for (int j = 0; j < Exits.Count; j++)
                            {
                                if (Exits[j].GetComponent<number>().x == pointList[pointList.Count - 1].x
                                && Exits[j].GetComponent<number>().y == pointList[pointList.Count - 1].y
                                && Exits[j].GetComponent<number>().z == pointList[pointList.Count - 1].z)
                                {
                                    go = new Vector3(Exits[j].GetComponent<number>().x, Exits[j].GetComponent<number>().y, Exits[j].GetComponent<number>().z);
                                    f = true;
                                    g = Exits[j];
                                    // Debug.Log("two");
                                }
                            }
                            if (!f)
                            {
                                Exits.Add(Instantiate(Resources.Load("Sphere", typeof(GameObject)) as GameObject, new Vector3(pointList[pointList.Count - 1].x, pointList[pointList.Count - 1].y, pointList[pointList.Count - 1].z), new Quaternion(0, 0, 0, 1)) as GameObject);
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

                        bool found = false;



                        for (int j = 0; j < goArray.Length; j++)
                        {
                            if (goArray[j].GetComponent<number>() != null)
                            {
                                if (goArray[j].GetComponent<number>().objectNumber == personNumber)
                                {

                                    goArray[j].GetComponent<number>().exists = true;
                                    if (goArray[j].GetComponent<AgentController>() != null)
                                    {
                                        goArray[j].GetComponent<AgentController>().goTo(pointList);
                                        if (myObject.emergency == "true")
                                        {
                                            goArray[j].GetComponent<AgentController>().emergency = true;
                                    
                                        }
                                            
                                        else
                                        {
                                            goArray[j].GetComponent<AgentController>().emergency = false;
                                       
                                                
                                        }
                                            

                                        found = true;

                                        if (g != null)
                                            goArray[j].GetComponent<AgentController>().Setcolor(g);
                                        else
                                            Debug.Log("whaaaat");
                                    }
                                }
                            }
                        }

                        if (!found)
                        {
                            if (myObject.emergency == "true")
                                buildingOb.GetComponent<Building>().addPerson(pointList, personNumber, g, true);
                            else
                                buildingOb.GetComponent<Building>().addPerson(pointList, personNumber, null, false);
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
                        if (!alarm)
                            if (goArray1[j].GetComponent<AgentController>() != null)
                                goArray1[j].GetComponent<AgentController>().Setcolor(null);
                    }
                }

              
            }
        }

        while (alarmOnce)
        {
            yield return new WaitForSeconds(1.0f);
            if(alarm == false)
            {
                alarmOnce = false;
            }
        }
        if (alarm)
            alarmOnce = true;



        findPeople();
        if (alarm)
        {
            string s = findPeople();
            if(isSimulation)
                StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":true," + "\"peopleLocations\":\"" + s + "\",\"mode\":\"simulation\"}", "assignPeople"));
            else
                StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":true," + "\"peopleLocations\":\"" + s + "\"}", "assignPeople"));
        }
        else {
            yield return new WaitForSeconds(5.0f);
            if (isSimulation)
                StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false,\"mode\":\"simulation\"}", "assignPeople"));
            else
                StartCoroutine(postRequest(ip + "building", "{\"type\":\"assignPeople\",\"alarm\":false}", "assignPeople"));

        }
    }

    //IEnumerator MistBottom()
    //{
    //    GameObject cam = GameObject.Find("MainCamera");


    //    float xPos = cam.transform.position.y + 15f;
    //    float zPos = Random.Range(-1.0f, 1.0f);
    //    string mistString = "mist/mist";
    //    mistString += Random.Range(1, 8);
    //    //Debug.Log(mistString);
    //    GameObject mist = Instantiate(Resources.Load(mistString, typeof(GameObject)) as GameObject, new Vector3(xPos, -2.77f, zPos), new Quaternion(0, 0, 0, 1)) as GameObject;
    //    mist.GetComponent<Slide>().speed = Random.Range(0.008f, 0.05f);
    //    mistListBottom.Add(mist);

    //    //delays spawn
    //    yield return new WaitForSeconds(Random.Range(6.0f, 3.5f));
    //    StartCoroutine("MistBottom");

    //}
}
