using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using System;

public class Room : MonoBehaviour
{
    public double offset;
    public bool show= true;
    List<GameObject> roomParts;
    public NavMeshSurface s;
  
    void Awake()
    {
        roomParts = new List<GameObject>();
        //int x = roomParts.Count;
        hideRoom();
    }

    // Update is called once per frame
    void Update()
    {
        if (show)
            ShowRoom();
        else
            hideRoom();
    }

    void hideRoom()
    {
         for(int i = 0; i < roomParts.Count; i++)
         {
             roomParts[i].GetComponent<MeshRenderer>().enabled = false;
        }
    }

    void ShowRoom()
    {
        for (int i = 0; i < roomParts.Count; i++)
        {
            roomParts[i].GetComponent<MeshRenderer>().enabled = true;
        }
    }

    public float Distance(float x1, float y1, float x2, float y2)//simple distance between 2 points
    {
        return (float)Math.Sqrt((Math.Pow(x1 - x2, 2) + Math.Pow(y1 - y2, 2)));
    }

  
    public Vector2 moveCloser(float x1, float y1, float x2, float y2, float distanceCloser)//moves close to x2,y2
    {
        float x = x2 - x1;
        float z = y2 - y1;
        float dis = Distance(x2, y2, x1, y1);

        float xn = x / dis;
        float zn = z / dis;

        float thirdx = x1 + (xn * distanceCloser);
        float thirdy = y1 + (zn * distanceCloser);

        return new Vector2(thirdx, thirdy);
    }


    public void buildStairs(Vector2[] cornerss, int floorNum, bool dontBuildFloors)
    {

        //building first floor
        Vector3[] vertices = new Vector3[4];
        //first
        float firstx = (cornerss[0].x + cornerss[1].x) / 2;
        float firsty = (cornerss[0].y + cornerss[1].y) / 2;
      //  Debug.Log("first: " + firstx + " " + firsty);
        //second
        float secondx = cornerss[1].x;
        float secondy = cornerss[1].y;
      //  Debug.Log("second: " + secondx + " " + secondy);
        //third
        Vector2 n = moveCloser(cornerss[1].x, cornerss[1].y, cornerss[2].x, cornerss[2].y, 0.1f);//moves closer to second x,y set
        float thirdx = n.x;
        float thirdy = n.y;
//        Debug.Log("third: " + thirdx + " " + thirdy);
        //fourth
        float fourthx = 0.0f;
        float fourthy = 0.0f;

        if (firstx != secondx)
            fourthx = firstx;
        else
            fourthx = thirdx;


        if (firsty != secondy)
            fourthy = firsty;
        else
            fourthy = thirdy;

      // Debug.Log("forth: " + fourthx + " " + fourthy);

        Vector3[] verticess = new Vector3[4];
        verticess[0] = new Vector3(firstx, (floorNum * 3), firsty);
        verticess[1] = new Vector3(secondx, (floorNum * 3), secondy);
        verticess[2] = new Vector3(thirdx, (floorNum * 3), thirdy);
        verticess[3] = new Vector3(fourthx, (floorNum * 3), fourthy);

        buildstair(verticess);

        verticess = new Vector3[4];
        verticess[3] = new Vector3(firstx, (floorNum * 3), firsty);
        verticess[2] = new Vector3(secondx, (floorNum * 3), secondy);
        verticess[1] = new Vector3(thirdx, (floorNum * 3), thirdy);
        verticess[0] = new Vector3(fourthx, (floorNum * 3), fourthy);

        buildstair(verticess);
        //building first ramp

        //first1
        float firstx1 = fourthx;
        float firsty1 = fourthy;

        //second1

        float secondx1 = thirdx;
        float secondy1 = thirdy;

        //third1
        Vector2 n1 = moveCloser(cornerss[2].x, cornerss[2].y, cornerss[1].x, cornerss[1].y, 1.6f);//moves closer to second x,y set
        float thirdx1 = n1.x;
        float thirdy1 = n1.y;

        //fourth1
        float fourthx1 = 0.0f;
        float fourthy1 = 0.0f;

        if (firstx1 != secondx1)
            fourthx1 = firstx1;
        else
            fourthx1 = thirdx1;


        if (firsty1 != secondy1)
            fourthy1 = firsty1;
        else
            fourthy1 = thirdy1;

        verticess = new Vector3[4];
        verticess[0] = new Vector3(firstx1, (floorNum * 3), firsty1);
        verticess[1] = new Vector3(secondx1, (floorNum * 3), secondy1);
        verticess[2] = new Vector3(thirdx1, (floorNum * 3)+1.5f, thirdy1);
        verticess[3] = new Vector3(fourthx1, (floorNum * 3)+1.5f, fourthy1);

        buildstair(verticess);

        verticess = new Vector3[4];
        verticess[3] = new Vector3(firstx1, (floorNum * 3), firsty1);
        verticess[2] = new Vector3(secondx1, (floorNum * 3), secondy1);
        verticess[1] = new Vector3(thirdx1, (floorNum * 3)+1.5f, thirdy1);
        verticess[0] = new Vector3(fourthx1, (floorNum * 3)+1.5f, fourthy1);

        buildstair(verticess);

        //building inbetween floor
        //first2
        float firstx2 = thirdx1;
        float firsty2 = thirdy1;

        //second2
        float secondx2 = cornerss[2].x;
        float secondy2 = cornerss[2].y;

        //third2
        float thirdx2 = cornerss[3].x;
        float thirdy2 = cornerss[3].y;

        //fourth2

        float fourthx2 = 0.0f;
        float fourthy2 = 0.0f;

        if (firstx2 != secondx2)
            fourthx2 = firstx2;
        else
            fourthx2 = thirdx2;


        if (firsty2 != secondy2)
            fourthy2 = firsty2;
        else
            fourthy2 = thirdy2;

        verticess = new Vector3[4];
        verticess[0] = new Vector3(firstx2, (floorNum * 3) + 1.5f, firsty2);
        verticess[1] = new Vector3(secondx2, (floorNum * 3) + 1.5f, secondy2);
        verticess[2] = new Vector3(thirdx2, (floorNum * 3) + 1.5f, thirdy2);
        verticess[3] = new Vector3(fourthx2, (floorNum * 3) + 1.5f, fourthy2);

        buildstair(verticess);

        verticess = new Vector3[4];
        verticess[3] = new Vector3(firstx2, (floorNum * 3) + 1.5f, firsty2);
        verticess[2] = new Vector3(secondx2, (floorNum * 3) + 1.5f, secondy2);
        verticess[1] = new Vector3(thirdx2, (floorNum * 3) + 1.5f, thirdy2);
        verticess[0] = new Vector3(fourthx2, (floorNum * 3) + 1.5f, fourthy2);

        buildstair(verticess);

        //building second ramp
        //first
        float firstx3 = fourthx2;
        float firsty3 = fourthy2;

        //second
        float secondx3 = fourthx1;
        float secondy3 = fourthy1;

        //third
        float thirdx3 = firstx1;
        float thirdy3 = firsty1;

        //fourth
        float fourthx3 = 0.0f;
        float fourthy3 = 0.0f;

        if (firstx3 != secondx3)
            fourthx3 = firstx3;
        else
            fourthx3 = thirdx3;


        if (firsty3 != secondy3)
            fourthy3 = firsty3;
        else
            fourthy3 = thirdy3;

        verticess = new Vector3[4];
        verticess[0] = new Vector3(firstx3, (floorNum * 3) + 1.5f, firsty3);
        verticess[1] = new Vector3(secondx3, (floorNum * 3) + 1.5f, secondy3);
        verticess[2] = new Vector3(thirdx3, (floorNum * 3) + 3f, thirdy3);
        verticess[3] = new Vector3(fourthx3, (floorNum * 3) + 3f, fourthy3);

        buildstair(verticess);

        verticess = new Vector3[4];
        verticess[3] = new Vector3(firstx3, (floorNum * 3) + 1.5f, firsty3);
        verticess[2] = new Vector3(secondx3, (floorNum * 3) + 1.5f, secondy3);
        verticess[1] = new Vector3(thirdx3, (floorNum * 3) + 3f, thirdy3);
        verticess[0] = new Vector3(fourthx3, (floorNum * 3) + 3f, fourthy3);

        buildstair(verticess);

        //last flat piece

        //first
        float firstx4 = thirdx3;
        float firsty4 = thirdy3;

        //second
        float secondx4 = fourthx3;
        float secondy4 = fourthy3;

        //third
        float thirdx4 = cornerss[0].x;
        float thirdy4 = cornerss[0].y;

        //fourth

        float fourthx4 = firstx;
        float fourthy4 = firsty;

        verticess = new Vector3[4];
        verticess[0] = new Vector3(firstx4, (floorNum * 3) + 3f, firsty4);
        verticess[1] = new Vector3(secondx4, (floorNum * 3) + 3f, secondy4);
        verticess[2] = new Vector3(thirdx4, (floorNum * 3) + 3f, thirdy4);
        verticess[3] = new Vector3(fourthx4, (floorNum * 3) + 3f, fourthy4);

        buildstair(verticess);

        verticess = new Vector3[4];
        verticess[3] = new Vector3(firstx4, (floorNum * 3) + 3f, firsty4);
        verticess[2] = new Vector3(secondx4, (floorNum * 3) + 3f, secondy4);
        verticess[1] = new Vector3(thirdx4, (floorNum * 3) + 3f, thirdy4);
        verticess[0] = new Vector3(fourthx4, (floorNum * 3) + 3f, fourthy4);

        buildstair(verticess);




    }


    public void build(Vector2[] vertices2D, int[] boolList , int floorNum, bool dontBuildFloors)
    {
        if(!dontBuildFloors)
            buildFloorTop(vertices2D, boolList, floorNum);

        buildWalls(vertices2D, boolList, floorNum);

       //hideRoom();
    }

    void buildWalls(Vector2[] vertices2D, int[] boolList , int floorNum)
    {
        for(int i = 0; i < vertices2D.Length; i++)
        {
            if(i + 1 >= vertices2D.Length)
            {
                //if(boolList[i] != boolList[0] || boolList[0] == 0 || boolList[i] == 0)
                //{
                    Vector3[] vertices = new Vector3[4];
                    vertices[0] = new Vector3(vertices2D[i].x, (floorNum*3), vertices2D[i].y);
                    vertices[1] = new Vector3(vertices2D[i].x, (floorNum*3)+3, vertices2D[i].y);
                    vertices[2] = new Vector3(vertices2D[0].x, (floorNum*3)+3, vertices2D[0].y);
                    vertices[3] = new Vector3(vertices2D[0].x, (floorNum*3), vertices2D[0].y);

                    buildWallFront(vertices);

                    Vector3[] vertices1 = new Vector3[4];
                    vertices1[3] = new Vector3(vertices2D[i].x, (floorNum*3), vertices2D[i].y);
                    vertices1[2] = new Vector3(vertices2D[i].x, (floorNum*3)+3, vertices2D[i].y);
                    vertices1[1] = new Vector3(vertices2D[0].x, (floorNum*3)+3, vertices2D[0].y);
                    vertices1[0] = new Vector3(vertices2D[0].x, (floorNum*3), vertices2D[0].y);

                    buildWallFront(vertices1);
                //}
                //Debug.Log(i + ": " + vertices2D[i].x + " " + vertices2D[i].y);
            }
            else
            {
                if(boolList[i] != boolList[i+1] || boolList[i+1] == 0 || boolList[i] == 0 )
                {
                    Vector3[] vertices = new Vector3[4];
                    vertices[0] = new Vector3(vertices2D[i].x, (floorNum*3), vertices2D[i].y);
                    vertices[1] = new Vector3(vertices2D[i].x, (floorNum*3)+3, vertices2D[i].y);
                    vertices[2] = new Vector3(vertices2D[i+1].x, (floorNum*3)+3, vertices2D[i+1].y);
                    vertices[3] = new Vector3(vertices2D[i+1].x, (floorNum*3), vertices2D[i+1].y);

                    buildWallFront(vertices);

                    Vector3[] vertices1 = new Vector3[4];
                    vertices1[2] = new Vector3(vertices2D[i].x, (floorNum*3), vertices2D[i].y);
                    vertices1[1] = new Vector3(vertices2D[i].x, (floorNum*3)+3, vertices2D[i].y);
                    vertices1[0] = new Vector3(vertices2D[i+1].x, (floorNum*3)+3, vertices2D[i+1].y);
                    vertices1[3] = new Vector3(vertices2D[i+1].x, (floorNum*3), vertices2D[i+1].y);

                    buildWallFront(vertices1);

                }
            }
            //Debug.Log(i + ": " + vertices2D[i].x + " " + vertices2D[i].y);
        }
    }

    void buildWallFront(Vector3[] vertices3D)
    {
        int[] tri = new int[6];

        tri[0] = 0;
        tri[1] = 1;
        tri[2] = 2;
        
        tri[3] = 2;
        tri[4] = 3;
        tri[5] = 0;
        
        Mesh msh = new Mesh();
        msh.vertices = vertices3D;
        msh.triangles = tri;
        msh.RecalculateNormals();
        msh.RecalculateBounds();

        GameObject objToSpawn = new GameObject("Wall");
        objToSpawn.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = objToSpawn.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;


        Material myMaterial = Resources.Load("materials/glass") as Material; 
        objToSpawn.GetComponent<Renderer>().material = myMaterial; 
        roomParts.Add(objToSpawn);
    }

    void buildstair(Vector3[] vertices3D)
    {
        int[] tri = new int[6];

        tri[0] = 0;
        tri[1] = 1;
        tri[2] = 2;

        tri[3] = 2;
        tri[4] = 3;
        tri[5] = 0;

        Mesh msh = new Mesh();
        msh.vertices = vertices3D;
        msh.triangles = tri;
        msh.RecalculateNormals();
        msh.RecalculateBounds();

        GameObject objToSpawn = new GameObject("Wall");
        objToSpawn.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = objToSpawn.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;


        Material myMaterial = Resources.Load("materials/routeGreen") as Material;
        objToSpawn.GetComponent<Renderer>().material = myMaterial;
        roomParts.Add(objToSpawn);
    }



    void buildFloorTop(Vector2[] vertices2D, int[] boolList , int floorNum)
    {
        // List<Vector2> vectorList = new List<Vector2>();
        // for(int i = 0; i < corners.Length; i++)
        // {
        //     vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
        // }

        // Vector2[] vertices2D = vectorList.ToArray();

        //Vector2[] vertices2D = new Vector2[] {
        //    new Vector2(0,0),
        //    new Vector2(0,50),
        //    new Vector2(25,50),
        //    new Vector2(70,0)
        //};

        // Use the triangulator to get indices for creating triangles
        Triangulator tr = new Triangulator(vertices2D);
        int[] indices = tr.Triangulate();

        // Create the Vector3 vertices
        Vector3[] vertices = new Vector3[vertices2D.Length];
        for (int i=0; i<vertices.Length; i++) {
            vertices[i] = new Vector3(vertices2D[i].x, floorNum*3, vertices2D[i].y);
        }

        // Create the mesh
        Mesh msh = new Mesh();
        msh.vertices = vertices;
        msh.triangles = indices;
        msh.RecalculateNormals();
        msh.RecalculateBounds();

        // Set up game object with mesh;
        gameObject.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = gameObject.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;
        Material myMaterial = Resources.Load("materials/routeGreen") as Material; 
        GetComponent<Renderer>().material = myMaterial;
        roomParts.Add(gameObject);
    }

    public void buildFloorTopFire(Vector2[] vertices2D, int[] boolList, int floorNum)
    {
        Debug.Log("building fire room");
        // List<Vector2> vectorList = new List<Vector2>();
        // for(int i = 0; i < corners.Length; i++)
        // {
        //     vectorList.Add(new Vector2(corners[i][0], corners[i][1]));
        // }

        // Vector2[] vertices2D = vectorList.ToArray();

        //Vector2[] vertices2D = new Vector2[] {
        //    new Vector2(0,0),
        //    new Vector2(0,50),
        //    new Vector2(25,50),
        //    new Vector2(70,0)
        //};

        // Use the triangulator to get indices for creating triangles
        Triangulator tr = new Triangulator(vertices2D);
        int[] indices = tr.Triangulate();

        // Create the Vector3 vertices
        Vector3[] vertices = new Vector3[vertices2D.Length];
        for (int i = 0; i < vertices.Length; i++)
        {
            vertices[i] = new Vector3(vertices2D[i].x, floorNum * 3+0.2f, vertices2D[i].y);
        }

        // Create the mesh
        Mesh msh = new Mesh();
        msh.vertices = vertices;
        msh.triangles = indices;
        msh.RecalculateNormals();
        msh.RecalculateBounds();

        // Set up game object with mesh;
        gameObject.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = gameObject.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;
        Material myMaterial = Resources.Load("materials/fireFloor") as Material;
        GetComponent<Renderer>().material = myMaterial;
        roomParts.Add(gameObject);
    }



}
