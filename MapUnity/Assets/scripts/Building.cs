using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Globalization;

public class buildingString
{
    public string doors;
    public string msg;
    public string rooms;
    public int numberFloors;
    public string people;
    public string status;
}

public class Building : MonoBehaviour
{
    private string doors;
    private string msg;
    private string rooms;
    private int numberFloors;
    private string people;
    private string status;
    //above will be used to convert from json
    List<Floor> floorList = new List<Floor>();

    void Start()
    {
        //createArrays();

    }

    public void addStrings(buildingString s)
    {
        doors = s.doors;
        msg = s.msg;
        rooms = s.rooms;
        numberFloors = s.numberFloors;
        people = s.people;
        status = s.status;
        //Debug.Log("adding data to building");
    }


    public void createArrays()
    {
        // numberFloors = 3;
        // rooms = "0 * 0,0 % 0,10 % 10,10 % 10,0 - 1 * 5,5 % 5,10 % 10,10 % 10,5 - 2 * 5,5 % 5,10 % 10,10 % 10,5 - 0 * 2,2 % 2,4 % 4,4- 0 * 0,0 % 0,20 % 20,20 % 20,0 - 1 * 0,0 % 0,18 % 18,18 % 18,0 - 2 * 0,0 % 0,16 % 16,16 % 16,0 - 0 * 0,0 % 0,5 % 5,5 % 5,0 - 1 * 0,0 % 0,5 % 5,5 % 5,0 - 2 * 0,0 % 0,5 % 5,5 % 5,0";
        //rooms ="0 * 0.0,0.0 % 0.0,4.5 % 4.0,4.5 % 4.0,0.0 - 0 * 4.0,0.0 % 4.0,4.5 % 8.0,4.5 % 8.0,0.0 - 0 * 8.0,0.0 % 8.0,4.5 % 12.0,4.5 % 12.0,0.0 - 0 * 0.0,5.8 % 4.0,5.8 % 4.0,10.0 - 0 * 4.0,5.8 % 8.0,5.8 % 8.0,10.0 % 4.0,10.0 - 0 * 8.0,5.8 % 12.0,5.8 % 12.0,10.0 % 8.0,10.0 - 0 * 12.0,5.8 % 16.0,5.8 % 16.0,10.0 % 12.0,10.0 - 0 * 16.0,5.8 % 20.0,5.8 % 20.0,10.0 % 16.0,10.0 - 0 * 20.0,5.8 % 24.0,5.8 % 24.0,10.0 % 20.0,10.0 - 0 * 13.3,0.0 % 17.0,0.0 % 17.0,1.7 % 13.3,1.7 - 0 * 13.3,1.7 % 13.3,4.5 % 17.0,4.5 % 17.0,1.7 - 0 * 17.0,1.7 % 17.0,4.5 % 24.0,4.5 % 24.0,1.7 ";

        //rooms = "0 * 0.0,0.0 % 0.0,10.5 % 4.0,10.5 % 4.0,0.0";
        //doors = "0 * 1 * 0,2 - 0 * 1 * 0,5 - 0 * 1 * 0.5,10.5- 0 * 1 * 3,10.5- 0 * 1 * 1,0- 0 * 1 * 3,0-0 * 1 * 4,2 - 0 * 1 * 4,5";
        // doors = "0 * 1 * 0,2 - 1 * 1  * 5,7 - 2 * 1 * 5,8.3 - 0 * 1 * 3,3";//floor*type*x,y
        // people = "0 * 0 * 7,7 - 1 * 1  * 7,7 - 2 * 2 * 7,7";



        float test = float.Parse("0.9", System.Globalization.CultureInfo.InvariantCulture);

        //--------finding doors
        Debug.Log("doors string: " + doors);//--------------splitting doors
        doors = doors.Replace(" ", string.Empty);
        string[] doorsA = doors.Split('-');

        float[][] doorsArr = new float[doorsA.Length][];
        for (int i = 0; i < doorsA.Length; i++)
        {
            doorsArr[i] = new float[4];

            string[] doorsA1 = doorsA[i].Split('*');

            doorsArr[i][0] = float.Parse(doorsA1[0], System.Globalization.CultureInfo.InvariantCulture);//floor
            Debug.Log("-"+doorsA1[1]+"-");
            doorsArr[i][1] = float.Parse(doorsA1[1], System.Globalization.CultureInfo.InvariantCulture);//type
            string[] xy = doorsA1[2].Split(',');
            doorsArr[i][2] = float.Parse(xy[0], System.Globalization.CultureInfo.InvariantCulture);//x
            doorsArr[i][3] = float.Parse(xy[1], System.Globalization.CultureInfo.InvariantCulture);//z

           // Debug.Log("door: " + doorsArr[i][0] + " " + doorsArr[i][1] + " " + doorsArr[i][2] + doorsArr[i][3] + " ");//--------------splitting doors
        }
        //--------finding doors end


        //--------creating floors
        rooms = rooms.Replace(" ", string.Empty);
        for (int i = 0; i < numberFloors; i++)
        {
            Floor floorPtr = gameObject.AddComponent(typeof(Floor)) as Floor;
            floorPtr.addAllDoors(doorsArr);//adding all doors into each floor
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
                    cornersFloat[l][m] = float.Parse(corners[l][m], System.Globalization.CultureInfo.InvariantCulture);
                    //Debug.Log("corner values: " + cornersFloat[l][m]);
                }
            }

            floorList[floorNo].addRoom(cornersFloat);
        }
        //--------adding rooms to floors end

        //--------placing people

        people = people.Replace(" ", string.Empty);
        string[] peopleA = people.Split('-');

        float[][] peopleArr = new float[peopleA.Length][];
        for (int i = 0; i < peopleA.Length; i++)
        {
            peopleArr[i] = new float[4];

            string[] peopleA1 = peopleA[i].Split('*');

            peopleArr[i][0] = float.Parse(peopleA1[0], System.Globalization.CultureInfo.InvariantCulture);//floor
            peopleArr[i][1] = float.Parse(peopleA1[1], System.Globalization.CultureInfo.InvariantCulture);//type
            string[] xy = peopleA1[2].Split(',');
            peopleArr[i][2] = float.Parse(xy[0], System.Globalization.CultureInfo.InvariantCulture);//x
            peopleArr[i][3] = float.Parse(xy[1], System.Globalization.CultureInfo.InvariantCulture);//z

            //Debug.Log("door: " + peopleArr[i][0] + " " + peopleArr[i][1] + " " + peopleArr[i][2] + peopleArr[i][3] + " ");//--------------splitting doors
        }

        for(int i = 0; i < peopleArr.Length; i++)
        {
          //  Instantiate(Resources.Load("Capsule"), new Vector3(peopleArr[i][2],peopleArr[i][1]*3,peopleArr[i][3]));
         // Debug.Log(peopleArr[i][2]+", "+peopleArr[i][0]+", "+peopleArr[i][3]);
          GameObject r = Instantiate(Resources.Load("Capsule", typeof(GameObject)) as GameObject, new Vector3(peopleArr[i][3], (peopleArr[i][0]*3)+1.2f,peopleArr[i][2]), new Quaternion(0, 0, 0, 1)) as GameObject;
          r.GetComponent<number>().objectNumber = peopleArr[i][1];
        }
        //--------placing people end

    }
}
