using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class Room : MonoBehaviour
{
    public NavMeshSurface s;
  
    void Awake()
    {

    }

    public void build(Vector2[] vertices2D, int[] boolList , int floorNum)
    {
        buildFloorTop(vertices2D, boolList, floorNum);
        buildWalls(vertices2D, boolList, floorNum);
    }

    void buildWalls(Vector2[] vertices2D, int[] boolList , int floorNum)
    {
        for(int i = 0; i < vertices2D.Length; i++)
        {
            if(i + 1 >= vertices2D.Length)
            {
                if(boolList[i] != boolList[0] || (boolList[0] == 0 || boolList[i] == 0) )
                {
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
                }
            }
            else
            {
                if(boolList[i] != boolList[i+1] || (boolList[i+1] == 0 || boolList[i] == 0) )
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
    }


    // Update is called once per frame
    void Update()
    {
 
    }
}
