using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class Floor : MonoBehaviour
{
    public List<GameObject> rooms;
    public float[][] doors;
    public int floorNumber;
    public GameObject[] people;
    public bool showFloor = true;

    void Update()
    {
        if (showFloor)
            showRooms();
        else
            hideRooms();
    }

    void hideRooms()
    {
        for (int i = 0; i < rooms.Count; i++)
        {
            rooms[i].GetComponent<Room>().show = false;
        }
    }

    void showRooms()
    {
        for (int i = 0; i < rooms.Count; i++)
        {
            rooms[i].GetComponent<Room>().show = true;
        }
    }


    private void Awake()
    {
        rooms = new List<GameObject>();
    }

    public void addAllDoors(float[][] d)
    {
        doors = d;
    }

    public float Distance(float x1, float y1, float x2, float y2)//simple distance between 2 points
    {
        return (float) Math.Sqrt( (Math.Pow(x1-x2,2)+Math.Pow(y1-y2,2)) );
    }

    public void addStairs(float[][] corners, bool dontBuildFloors)
    {
        GameObject r = Instantiate(Resources.Load("Room", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
        rooms.Add(r);
        List<Vector2> pointList = new List<Vector2>();
        for(int i = 0; i < corners.Length;i++)
        {
            pointList.Add(new Vector2(corners[i][0], corners[i][1]));
        }
        Vector2[] verticesArray = pointList.ToArray();
        r.GetComponent<Room>().buildStairs(verticesArray, floorNumber, dontBuildFloors);


    }

    public void addRoom(float[][] corners, bool dontBuildFloors)
    {

        List<int> numberList = new List<int>();
        List<Vector2> pointList = new List<Vector2>();
        for (int i = 0; i < corners.Length; i++)
        {
            numberList.Add(0);
            pointList.Add(new Vector2(corners[i][0], corners[i][1]));
        }
        GameObject r = Instantiate(Resources.Load("Room", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
        rooms.Add(r);

        //List<int> initializers = new List<int>();
        //initializers.Add(1);
        //initializers.Add(3);
        //int index = initializers.IndexOf(3);
        //initializers.Insert(1, 2);
        //Debug.Log("check:------"+initializers[1]);

        //Debug.Log("--------" + pointList[0].x+" "+ pointList[0].y);

        int foundOne = 0;
        int foundTwo = 0;
        int newDoor = 1;

        for (int i = 0; i < doors.Length; i++)
        {
            if (Math.Abs(doors[i][0] - floorNumber) < Mathf.Epsilon)//skips doors not on the right floor
            {
                for (int j = 0; j < pointList.Count; j++)
                {
                    if (j + 1 >= pointList.Count)
                    {
                        //pointList[j].x 
                        float v1 = Distance(pointList[j].x, pointList[j].y, doors[i][2], doors[i][3]) + Distance(pointList[0].x, pointList[0].y, doors[i][2], doors[i][3]);
                        float v2 = Distance(pointList[j].x, pointList[j].y, pointList[0].x, pointList[0].y);

                        if (Mathf.Approximately(v1, v2))
                        {
                            float x = pointList[j].x - doors[i][2];
                            float z = pointList[j].y - doors[i][3];
                            float dis = Distance(pointList[j].x, pointList[j].y, doors[i][2], doors[i][3]);

                            float xn = x / dis;
                            float zn = z / dis;

                            float doorx1 = doors[i][2] + (xn * (doors[i][1] / 2));
                            float doorz1 = doors[i][3] + (zn * (doors[i][1] / 2));

                            ////-------------------------------------------------------

                            float x1 = pointList[0].x - doors[i][2];
                            float z1 = pointList[0].y - doors[i][3];
                            float dis1 = Distance(pointList[0].x, pointList[0].y, doors[i][2], doors[i][3]);

                            float xn1 = x1 / dis1;
                            float zn1 = z1 / dis1;

                            float doorx2 = doors[i][2] + (xn1 * (doors[i][1] / 2));
                            float doorz2 = doors[i][3] + (zn1 * (doors[i][1] / 2));

//                            Debug.Log("new door positions: " + doorx1 + ", " + doorz1 + "   " + doorx2 + ", " + doorz2);

                            //doorx2 = (float)Math.Round(doorx2, 2);
                            //doorz2 = (float)Math.Round(doorz2, 2);
                            //doorx1 = (float)Math.Round(doorx1, 2);
                            //doorz1 = (float)Math.Round(doorz1, 2);

                            pointList.Insert(j + 1, new Vector2(doorx2, doorz2));
                            pointList.Insert(j + 1, new Vector2(doorx1, doorz1));

                            numberList.Insert(j + 1, newDoor);
                            numberList.Insert(j + 1, newDoor);
                            newDoor++;


                            foundOne++;
                            break;
                        }
                    }
                    else
                    {
                        float v1 = Distance(pointList[j].x, pointList[j].y, doors[i][2], doors[i][3]) + Distance(pointList[j+1].x, pointList[j+1].y, doors[i][2], doors[i][3]);
                        float v2 = Distance(pointList[j].x, pointList[j].y, pointList[j+1].x, pointList[j+1].y);
                        if (Mathf.Approximately(v1, v2))
                        {
                             //Debug.Log("+--------------------------------------------+ " + doors[j][2] + "  " + doors[j][3]);
                            float x = pointList[j].x - doors[i][2];
                            float z = pointList[j].y - doors[i][3];
                            float dis = Distance(pointList[j].x, pointList[j].y, doors[i][2], doors[i][3]);

                            float xn = x / dis;
                            float zn = z / dis;

                            float doorx1 = doors[i][2] + (xn * (doors[i][1] / 2));
                            float doorz1 = doors[i][3] + (zn * (doors[i][1] / 2));

                            ////-------------------------------------------------------

                            float x1 = pointList[j+1].x - doors[i][2];
                            float z1 = pointList[j+1].y - doors[i][3];
                            float dis1 = Distance(pointList[j+1].x, pointList[j+1].y, doors[i][2], doors[i][3]);

                            float xn1 = x1 / dis1;
                            float zn1 = z1 / dis1;

                            float doorx2 = doors[i][2] + (xn1 * (doors[i][1] / 2));
                            float doorz2 = doors[i][3] + (zn1 * (doors[i][1] / 2));

                            //Debug.Log("new door positions: "+doorx1+", "+doorz1+"   "+doorx2+", "+doorz2);

                            //doorx2 = (float)Math.Round(doorx2, 2);
                            //doorz2 = (float)Math.Round(doorz2, 2);
                            //doorx1 = (float)Math.Round(doorx1, 2);
                            //doorz1 = (float)Math.Round(doorz1, 2);


                            pointList.Insert(j+1, new Vector2(doorx2, doorz2));
                            pointList.Insert(j+1, new Vector2(doorx1, doorz1));

                            numberList.Insert(j + 1, newDoor);
                            numberList.Insert(j + 1, newDoor);
                            newDoor++;
                     
                            foundOne++;
                            break;
                        }
                    }
                }
            }
        }

        Vector2[] verticesArray = pointList.ToArray();
        int[] intListarr = numberList.ToArray();



        //------------------------------------end of checking for doors
        r.GetComponent<Room>().build(verticesArray, intListarr, floorNumber, dontBuildFloors);

        //Debug.Log("-----found" + foundOne + " == " + foundTwo);
    }
}