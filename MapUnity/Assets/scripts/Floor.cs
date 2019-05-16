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

    public float Distance(float x1, float y1, float x2, float y2)
    {
        return (float) Math.Sqrt( (Math.Pow(x1-x2,2)+Math.Pow(y1-y2,2)) );
    }


    public void addRoom(float[][] corners)
    {
        GameObject r = Instantiate(Resources.Load("Room", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
        rooms.Add(r);
        //todo: check if anys doors fall between any of the corners and then put them into and array and label door indexes.

        List<Vector2> vectorList = new List<Vector2>();
        List<bool> boolList = new List<bool>();
       
        for(int i = 0; i < corners.Length; i++)
        {
            vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
            boolList.Add(false);
            for(int j = 0; j < doors.Length; j++)
            {
                
                if(doors[j][0] == floorNumber)
                {
               
                    if(i + 1 >= corners.Length)
                    {
                        if(Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) + Distance(corners[0][0], corners[0][1], doors[j][2], doors[j][3]) == Distance(corners[i][0], corners[i][1], corners[0][0], corners[0][1]) )
                        {
                            Debug.Log("found door between 2 corners1 on floor no: "+floorNumber);

                            float x = corners[i][0] - doors[j][2];
                            float z = corners[i][1] - doors[j][3];
                            float dis = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]);

                            float xn = x/dis;
                            float zn = z/dis;

                            float doorx1 = doors[j][2] + (xn*(doors[j][1]/2));
                            float doorz1 = doors[j][3] + (zn*(doors[j][1]/2));

                            //-------------------------------------------------------

                            float x1 = corners[0][0] - doors[j][2] ;
                            float z1 = corners[0][1] - doors[j][3];
                            float dis1 = Distance(corners[0][0], corners[0][1], doors[j][2], doors[j][3]);

                            float xn1 = x1/dis1;
                            float zn1 = z1/dis1;

                            float doorx2 = doors[j][2] + (xn1*(doors[j][1]/2));
                            float doorz2 = doors[j][3] + (zn1*(doors[j][1]/2));

                            Debug.Log("new door positions: "+doorx1+","+doorz1+"   "+doorx2+","+doorz2); 

                            //vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
                            vectorList.Add(new Vector2(doorx1, doorz1));
                            vectorList.Add(new Vector2(doorx2, doorz2));
                            
                            boolList.Add(true);
                            boolList.Add(true);


                        }
                        else
                        {
                            //vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
                            //boolList.Add(false);
                        }
                        //Debug.Log("corner 1: "+corners[i][0]+","+corners[i][0]+" 2: "+corners[0][0]+","+corners[0][1]+" 3: "+doors[j][2]+","+doors[j][3]);
                        //Debug.Log(floorNumber+"distance between corners "+Distance(corners[i][0], corners[i][1], corners[0][0], corners[0][1])+" = "+Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) +" + "+ Distance(corners[0][0], corners[0][1], doors[j][2], doors[j][3]));
                    }
                    else
                    {
                        if(Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) + Distance(corners[i+1][0], corners[i+1][1], doors[j][2], doors[j][3]) == Distance(corners[i][0], corners[i][1], corners[i+1][0], corners[i+1][1]) )
                        {
                            Debug.Log("found door between 2 corners1 on floor no: "+floorNumber);

                            float x = corners[i][0] - doors[j][2];
                            float z = corners[i][1] - doors[j][3];
                            float dis = Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]);

                            float xn = x/dis;
                            float zn = z/dis;

                            float doorx1 = doors[j][2] + (xn*(doors[j][1]/2));
                            float doorz1 = doors[j][3] + (zn*(doors[j][1]/2));

                            //-------------------------------------------------------

                            float x1 = corners[i+1][0] - doors[j][2] ;
                            float z1 = corners[i+1][1] - doors[j][3];
                            float dis1 = Distance(corners[i+1][0], corners[i+1][1], doors[j][2], doors[j][3]);

                            float xn1 = x1/dis1;
                            float zn1 = z1/dis1;

                            float doorx2 = doors[j][2] + (xn1*(doors[j][1]/2));
                            float doorz2 = doors[j][3] + (zn1*(doors[j][1]/2));

                            Debug.Log("new door positions: "+doorx1+","+doorz1+"   "+doorx2+","+doorz2); 

                            //vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
                            vectorList.Add(new Vector2(doorx1, doorz1));
                            vectorList.Add(new Vector2(doorx2, doorz2));
                            //boolList.Add(false);
                            boolList.Add(true);
                            boolList.Add(true);

                        }
                        else
                        {
                            //vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
                           // boolList.Add(false);
                        }
                        //Debug.Log("corner 1: "+corners[i][0]+","+corners[i][0]+" 2: "+corners[i+1][0]+","+corners[i+1][1]+" 3: "+doors[j][2]+","+doors[j][3]);
                        //Debug.Log(floorNumber+"distance between corners "+Distance(corners[i][0], corners[i][1], corners[i+1][0], corners[i+1][1]) +" = "+Distance(corners[i][0], corners[i][1], doors[j][2], doors[j][3]) + " + "+Distance(corners[i+1][0], corners[i+1][1], doors[j][2], doors[j][3]));
                    }
                }
            }
         
        }

        Vector2[] verticesArray = vectorList.ToArray();
        bool[] boolArray = boolList.ToArray();



        //------------------------------------end of checking for doors
        r.GetComponent<Room>().build(verticesArray, boolArray, floorNumber);
    }
}