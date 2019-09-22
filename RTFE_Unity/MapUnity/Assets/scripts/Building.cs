﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Globalization;
using System;

public class buildingString
{
    public string doors;
    public string msg;
    public string rooms;
    public int numberFloors;
    public string people;
    public string status;
    public string stairs;
    public string emergency;
}

public class Building : MonoBehaviour
{
    public double offset;
    private string doors;
    private string msg;
    private string rooms;
    private int numberFloors;
    private string people;
    private string status;
    public string stairs;
    public string emergency;
    private int showFloorsBelow;
    //above will be used to convert from json
    List<Floor> floorList = new List<Floor>();
    List<GameObject> peopleList = new List<GameObject>();

    void Start()
    {
        numberFloors = -1;
        //createArrays();

    }

    public List<GameObject> getList()
    {
        return peopleList;
    }

    private void Update()
    {
        if(numberFloors != -1)
        {
            if (Input.GetKeyDown("down"))
            {
                if(showFloorsBelow <= 0)
                {

                }
                else
                {
                    showFloorsBelow--;
                }
            }

            if (Input.GetKeyDown("up"))
            {
                if (showFloorsBelow >= numberFloors)
                {

                }
                else
                {
                    showFloorsBelow++;
                }
            }
        }

        for(int i = 0; i < floorList.Count; i++)
        {
            if(floorList[i].floorNumber < showFloorsBelow)
            {
                floorList[i].showFloor = true;
            }
            else
            {
                floorList[i].showFloor = false;
            }
        }

        for (int i = 0; i < peopleList.Count; i++)
        {
            if(peopleList[i] != null)
                peopleList[i].GetComponent<number>().showBelow = showFloorsBelow;
        }
    }

    public void addStrings(buildingString s)
    {
        doors = s.doors;
        msg = s.msg;
        rooms = s.rooms;
        numberFloors = s.numberFloors;
        people = s.people;
        status = s.status;
        stairs = s.stairs;
        //Debug.Log("adding data to building");
    }

    public float Distance(float x1, float y1, float x2, float y2)//simple distance between 2 points
    {
        return (float)Math.Sqrt((Math.Pow(x1 - x2, 2) + Math.Pow(y1 - y2, 2)));
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
        Debug.Log(stairs);
        Debug.Log(rooms);
        Debug.Log(doors);

        showFloorsBelow = numberFloors;

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
           //Debug.Log("-"+doorsA1[1]+"-");
            doorsArr[i][1] = float.Parse(doorsA1[1], System.Globalization.CultureInfo.InvariantCulture);//type
            string[] xy = doorsA1[2].Split(',');
            doorsArr[i][2] = float.Parse(xy[0], System.Globalization.CultureInfo.InvariantCulture);//x
            doorsArr[i][3] = float.Parse(xy[1], System.Globalization.CultureInfo.InvariantCulture);//z

           // Debug.Log("door: " + doorsArr[i][0] + " " + doorsArr[i][1] + " " + doorsArr[i][2] + doorsArr[i][3] + " ");//--------------splitting doors
        }
        //--------finding doors end


        //---------finding stairs and adding the new door to stair room
        if (stairs != null)
        {
            stairs = stairs.Replace(" ", string.Empty);
            string[] stairsA = stairs.Split('-');
            float[][] stairsArr = new float[stairsA.Length][];
            for (int i = 0; i < stairsA.Length; i++)
            {
                stairsArr[i] = new float[10];
                string[] stairsA1 = stairsA[i].Split('*');
                stairsArr[i][0] = float.Parse(stairsA1[0], System.Globalization.CultureInfo.InvariantCulture);//floor?
                stairsArr[i][1] = float.Parse(stairsA1[1], System.Globalization.CultureInfo.InvariantCulture);//type?check this sometime

                string[] stairsA2 = stairsA1[2].Split('%');
                string[] temp = stairsA2[0].Split(',');
                stairsArr[i][2] = float.Parse(temp[0], System.Globalization.CultureInfo.InvariantCulture);
                stairsArr[i][3] = float.Parse(temp[1], System.Globalization.CultureInfo.InvariantCulture);
                temp = stairsA2[1].Split(',');
                stairsArr[i][4] = float.Parse(temp[0], System.Globalization.CultureInfo.InvariantCulture);
                stairsArr[i][5] = float.Parse(temp[1], System.Globalization.CultureInfo.InvariantCulture);
                temp = stairsA2[2].Split(',');
                stairsArr[i][6] = float.Parse(temp[0], System.Globalization.CultureInfo.InvariantCulture);
                stairsArr[i][7] = float.Parse(temp[1], System.Globalization.CultureInfo.InvariantCulture);
                temp = stairsA2[3].Split(',');
                stairsArr[i][8] = float.Parse(temp[0], System.Globalization.CultureInfo.InvariantCulture);
                stairsArr[i][9] = float.Parse(temp[1], System.Globalization.CultureInfo.InvariantCulture);
            }

            //-------------------------------------------------

            //----------putting stairs doors into doors array
            float[][] doorsArr2 = new float[doorsA.Length + stairsA.Length][];
            for (int i = 0; i < doorsArr.Length; i++)
            {
                doorsArr2[i] = new float[4];
                for (int j = 0; j < 4; j++)
                {
                    doorsArr2[i][j] = doorsArr[i][j];
                }
            }

            for (int i = 0; i < stairsA.Length; i++)
            {
                float x1 = stairsArr[i][2];
                float y1 = stairsArr[i][3];
                float x2 = stairsArr[i][4];
                float y2 = stairsArr[i][5];

                float dist = Distance(x1, y1, x2, y2);

                float newX = (x1 + x2) / 2;
                float newY = (y1 + y2) / 2;
                // Debug.Log(stairsA.Length);
                doorsArr2[doorsA.Length + i] = new float[4];
                doorsArr2[doorsA.Length + i][0] = (float)stairsArr[i][0];
                doorsArr2[doorsA.Length + i][1] = dist - 0.2f;
                doorsArr2[doorsA.Length + i][2] = newX;//x
                doorsArr2[doorsA.Length + i][3] = newY;//should be z not y just using z cause vector 2 has xy and not xz
            }

            doorsArr = doorsArr2;

            //---------------------------------------
            //--------creating floors
            rooms = rooms.Replace(" ", string.Empty);
            for (int i = 0; i < numberFloors; i++)
            {
                Floor floorPtr = gameObject.AddComponent(typeof(Floor)) as Floor;
                floorPtr.GetComponent<Floor>().offset = offset;
                floorPtr.addAllDoors(doorsArr);//adding all doors into each floor
                floorPtr.floorNumber = i;
                floorList.Add(floorPtr);
            }


            //todo: add the rooms corners

            //--------creating floors end




            //--------adding rooms to floors
            string[] roomsArr = rooms.Split('-');

            float BiggestX = 0;
            float SmallestX = 0;
            float SmallestZ = 0;
            float BiggestZ = 0;
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
                        if(m == 0)
                        {
                            if(BiggestX < cornersFloat[l][m])
                                BiggestX = cornersFloat[l][m];
                            if (SmallestX > cornersFloat[l][m])
                                SmallestX = cornersFloat[l][m];
                        }
                        if(m == 1)
                        {
                            if (BiggestZ < cornersFloat[l][m])
                                BiggestZ = cornersFloat[l][m];
                            if (SmallestZ > cornersFloat[l][m])
                                SmallestZ = cornersFloat[l][m];
                        }
                        //Debug.Log("corner values: " + cornersFloat[l][m]);
                    }
                }

                floorList[floorNo].addRoom(cornersFloat, false);
            }
            float averageX = (BiggestX + SmallestX) / 2f;
            float averageZ = (BiggestZ + SmallestZ) / 2f;
            GameObject cam = GameObject.Find("MainCamera");
            cam.GetComponent<FlyCamera>().X = averageX;
            cam.GetComponent<FlyCamera>().Z = averageZ;

            //-----building stairs rooms

            for (int i = 0; i < stairsArr.Length; i++)
            {
                //stairsArr[i][9]
                float[][] corn = new float[4][];
                corn[0] = new float[2];
                corn[0][0] = stairsArr[i][2];
                corn[0][1] = stairsArr[i][3];

                corn[1] = new float[2];
                corn[1][0] = stairsArr[i][4];
                corn[1][1] = stairsArr[i][5];

                corn[2] = new float[2];
                corn[2][0] = stairsArr[i][6];
                corn[2][1] = stairsArr[i][7];

                corn[3] = new float[2];
                corn[3][0] = stairsArr[i][8];
                corn[3][1] = stairsArr[i][9];
                //Debug.Log("+"+stairsArr[i][9]+ " "+stairsArr[i][8]);
                if ((int)stairsArr[i][0] == 0)
                {
                    floorList[(int)stairsArr[i][0]].addRoom(corn, false);
                    floorList[(int)stairsArr[i][0]].addStairs(corn, false);
                }
                else
                {
                    floorList[(int)stairsArr[i][0]].addRoom(corn, true);
                    if ((int)stairsArr[i][0] < 2)//stops top floor stairs from building
                        floorList[(int)stairsArr[i][0]].addStairs(corn, true);
                }


            }

            //--------adding rooms to f0oors end

            //--------placing people
            /*
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
                peopleList.Add(r);
            }
            //--------placing people end
            */
        }

    }

    public void addFire(List<Vector2> fireCorners, float floorNo)
    {
        floorList[(int)floorNo].addFire(fireCorners, floorNo);
    }

    public void addPerson(List<Vector3> pl,float personNumber, GameObject g, bool emerge)
    {
       // Debug.Log(personNumber+") "+pl[0].x + ", " + pl[0].y + ", " + pl[0].z + " ");
        GameObject r = Instantiate(Resources.Load("Capsule", typeof(GameObject)) as GameObject, pl[0], new Quaternion(0, 0, 0, 1)) as GameObject;
        r.GetComponent<number>().objectNumber = personNumber;
        r.GetComponent<number>().exists = true;
        r.GetComponent<AgentController>().goTo(pl, emerge);
        r.GetComponent<AgentController>().offset = offset;
        //r.layer = "scene2";

        r.GetComponent<AgentController>().Setcolor(g);


       // r.GetComponent<AgentController>().emergency = emerg;
        peopleList.Add(r);
    }
}
