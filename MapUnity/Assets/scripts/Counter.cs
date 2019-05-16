using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using Newtonsoft.Json;

public class Counter : MonoBehaviour
{
    bool runOnce;

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
    }

    IEnumerator postRequest(string url, string json)
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
            Debug.Log("Received: " + uwr.downloadHandler.text);

            agentInstructions myObject = JsonUtility.FromJson<agentInstructions>(uwr.downloadHandler.text);
            Debug.Log(myObject.status);
            Debug.Log(myObject.msg);

            string [] x = myObject.msg.Split(',');
            var x1 = new string[x.Length][];
            for(int i = 0; i < x.Length; i++)
            {
                x1[i] = x[i].Split('-');
            }


            int [][] x2 = new int[x.Length][];
            for (int i = 0; i < x.Length; i++)
            {
                x2[i] = new int[2];
                for (int j = 0; j < 2; j++)
                {
                    //x2[i][j] = (int)x1[i][j];
                    x2[i][j] = int.Parse(x1[i][j]);
                    Debug.Log(x2[i][j]);
                }
            }

            var goArray = FindObjectsOfType<GameObject>();
            for(int i =0; i < x.Length; i++)
            {
                for (int j = 0; j < goArray.Length; j++)
                {
                    if(goArray[j].GetComponent<number>() != null)
                    if (goArray[j].GetComponent<number>().objectNumber == x2[i][0])
                    {
                        goArray[j].GetComponent<AgentController>().goTo(x2[i][1]);
                    }
                }
            }

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
             Debug.Log("No Doors");
        }
        if (roomList.Count == 0) 
        {
            Debug.Log("No Rooms");
        }
        if (agentList.Count == 0) 
        {
            Debug.Log("No Agents");
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


        StartCoroutine(postRequest("http://localhost:8080/buildingGeneration", "{\"type\":\"buildingData\"}"));//kinson
    }
}
