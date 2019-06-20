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

    public void addRoom(float[][] corners)
    {
        GameObject r = Instantiate(Resources.Load("Room", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
        rooms.Add(r);
       

        List<Vector2> vectorList = new List<Vector2>();
        List<int> intList = new List<int>();
        int newDoor = 1;
       
        for(int i = 0; i < corners.Length; i++)
        {
            //check if anys doors fall between any of the corners and then put them into and array and label door indexes.
            vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
            intList.Add(0);
            newDoor = 1;
            for(int j = 0; j < doors.Length; j++)
            {
                if(Math.Abs(doors[j][0] - floorNumber) < Mathf.Epsilon)
                {
                    /*todo:you need to restart everytime you find a 
                    new door other wize the doors will not be inserted in the correct order breaking the floor and doors*/
               
                    if(i + 1 >= corners.Length)
                    {
                        //todo: remmeber to use Mathf.Approximately(v1, v2) when comparing floats, Mathf.Epsilon
                        float v1 = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) + Distance(corners[0][0], corners[0][1], doors[j][2], doors[j][3]);
                        float v2 = Distance(corners[i][0], corners[i][1], corners[0][0], corners[0][1]);
                        if (Mathf.Approximately(v1, v2))
                        {
                               // Debug.Log("---------------------------------------------- " + doors[j][2]+"  "+ doors[j][3]);

                             float x = corners[i][0] - doors[j][2];
                             float z = corners[i][1] - doors[j][3];
                             float dis = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]);

                             float xn = x/dis;
                             float zn = z/dis;

                             float doorx1 = doors[j][2] + (xn*(doors[j][1]/2));
                             float doorz1 = doors[j][3] + (zn*(doors[j][1]/2));

                             //-------------------------------------------------------

                             float x1 = corners[0][0] - doors[j][2];
                             float z1 = corners[0][1] - doors[j][3];
                             float dis1 = Distance(corners[0][0], corners[0][1], doors[j][2], doors[j][3]);

                             float xn1 = x1/dis1;
                             float zn1 = z1/dis1;

                             float doorx2 = doors[j][2] + (xn1*(doors[j][1]/2));
                             float doorz2 = doors[j][3] + (zn1*(doors[j][1]/2));

                            // Debug.Log("new door positions: "+doorx1+","+doorz1+"   "+doorx2+","+doorz2); 
                             vectorList.Add(new Vector2(doorx1, doorz1));
                             vectorList.Add(new Vector2(doorx2, doorz2));
                         
                             intList.Add(newDoor);
                            intList.Add(newDoor);
                            newDoor++;
                        }
                    }
                    else
                    {
                    
                        float v1 = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) + Distance(corners[i + 1][0], corners[i + 1][1], doors[j][2], doors[j][3]);
                        float v2 = Distance(corners[i][0], corners[i][1], corners[i + 1][0], corners[i + 1][1]);
                        if (Mathf.Approximately(v1,v2))
                        {
                     
                           // Debug.Log("+--------------------------------------------+ " + doors[j][2] + "  " + doors[j][3]);
                            float x = corners[i][0] - doors[j][2];
                            float z = corners[i][1] - doors[j][3];
                            float dis = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]);

                            float xn = x/dis;
                            float zn = z/dis;

                            float doorx1 = doors[j][2] + (xn*(doors[j][1]/2));
                            float doorz1 = doors[j][3] + (zn*(doors[j][1]/2));

                            //-------------------------------------------------------

                            float x1 = corners[i+1][0] - doors[j][2];
                            float z1 = corners[i+1][1] - doors[j][3];
                            float dis1 = Distance(corners[i+1][0], corners[i+1][1], doors[j][2], doors[j][3]);

                            float xn1 = x1/dis1;
                            float zn1 = z1/dis1;

                            float doorx2 = doors[j][2] + (xn1*(doors[j][1]/2));
                            float doorz2 = doors[j][3] + (zn1*(doors[j][1]/2));

                           // Debug.Log("new door positions: "+doorx1+", "+doorz1+"   "+doorx2+", "+doorz2); 

                            //vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
                            vectorList.Add(new Vector2(doorx1, doorz1));
                            vectorList.Add(new Vector2(doorx2, doorz2));

                            //boolList.Add(false);
                            intList.Add(newDoor);
                            intList.Add(newDoor++);

                        }
                    }
                }
            }
        }

        Vector2[] verticesArray = vectorList.ToArray();
        int[] intListarr = intList.ToArray();



        //------------------------------------end of checking for doors
        r.GetComponent<Room>().build(verticesArray, intListarr, floorNumber);
    }
}