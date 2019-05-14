using System.Collections;
using System.Collections.Generic;
using UnityEngine;



public class Building : MonoBehaviour
{
    public string type;
    public int numberFloors;
    //public string floors = "0 * 0,0 % 0,20 % 20,20 % 20,0 - 1 * 0,0 % 0,18 % 18,18 % 18,0 - 2 * 0,0 % 0,16 % 16,16 % 16,0";
    //public string halls = "0 * 0,0 % 0,5 % 5,5 % 5,0 - 1 * 0,0 % 0,5 % 5,5 % 5,0 - 2 * 0,0 % 0,5 % 5,5 % 5,0";
    public string rooms;
    public string doors;
    public string people;
    //above will be used to convert from json


    void Start()
    {
        createArrays();

    }

    List<Floor> floorList = new List<Floor>();


    public void createArrays()
    {
        type = "unity";
        numberFloors = 3;
        rooms = "0 * 0,0 % 0,10 % 10,10 % 10,0 - 1 * 5,5 % 5,10 % 10,10 % 10,5 - 2 * 5,5 % 5,10 % 10,10 % 10,5"; //- 0 * 0,0 % 0,20 % 20,20 % 20,0 - 1 * 0,0 % 0,18 % 18,18 % 18,0 - 2 * 0,0 % 0,16 % 16,16 % 16,0 - 0 * 0,0 % 0,5 % 5,5 % 5,0 - 1 * 0,0 % 0,5 % 5,5 % 5,0 - 2 * 0,0 % 0,5 % 5,5 % 5,0";
        doors = "0 * 1 * 0,2 - 1 * 1  * 0,2 - 2 * 1 * 0,2";//floor*type*x,y
        people = "0 * 0 * 7,7 - 1 * 1  * 7,7 - 2 * 2 * 7,7";


        //--------finding doors
        Debug.Log("doors string: " + doors);//--------------splitting doors
        doors = doors.Replace(" ", string.Empty);
        string[] doorsA = doors.Split('-');

        float[][] doorsArr = new float[doorsA.Length][];
        for (int i = 0; i < doorsA.Length; i++)
        {
            doorsArr[i] = new float[4];

            string[] doorsA1 = doorsA[i].Split('*');

            doorsArr[i][0] = float.Parse(doorsA1[0]);//floor
            doorsArr[i][1] = float.Parse(doorsA1[1]);//type
            string[] xy = doorsA1[2].Split(',');
            doorsArr[i][2] = float.Parse(xy[0]);//x
            doorsArr[i][3] = float.Parse(xy[1]);//z

            Debug.Log("door: " + doorsArr[i][0] + " " + doorsArr[i][1] + " " + doorsArr[i][2] + doorsArr[i][3] + " ");//--------------splitting doors
        }
        //--------finding doors end


        //--------creating floors
        rooms = rooms.Replace(" ", string.Empty);
        for (int i = 0; i < numberFloors; i++)
        {
            Floor floorPtr = gameObject.AddComponent(typeof(Floor)) as Floor;
            floorPtr.addAllDoors(doorsArr);//adding all doors into floor
            floorPtr.floorNumber = i;
            floorList.Add(floorPtr);
        }
        //--------creating floors end

       


        //--------adding rooms to floors
        string[] roomsArr = rooms.Split('-');

        for (int i = 0; i < roomsArr.Length; i++)
        {
            string[] roomsArr2 = roomsArr[i].Split('*');

            int floorNo = int.Parse(roomsArr2[0]);
            //Debug.Log("belong to room: " + roomsArr2[0]);

            string[] roomsArr3 = roomsArr2[1].Split('%');

            var corners = new string[roomsArr3.Length][];
            for (int k = 0; k < roomsArr3.Length; k++)
            {
                corners[k] = roomsArr3[k].Split(',');
            }

            float[][] cornersFloat = new float[roomsArr3.Length][];
            for (int l = 0; l < roomsArr3.Length; l++)
            {
                cornersFloat[l] = new float[2];
                for (int m = 0; m < 2; m++)
                {
                    //x2[i][j] = (int)x1[i][j];
                    cornersFloat[l][m] = float.Parse(corners[l][m]);
                    //Debug.Log("corner values: " + cornersFloat[l][m]);
                }
            }

            floorList[floorNo].addRoom(cornersFloat);
        }
        //--------adding rooms to floors end



    }
}
