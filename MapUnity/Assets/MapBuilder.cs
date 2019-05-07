﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class MapBuilder : MonoBehaviour
{
    public NavMeshSurface s;
    //public GameObject square;
    // Start is called before the first frame update
    //int i, j;
   

    void Awake()
    {
       // surface = GetComponent<NavMeshAgent> (); 
        buildFloorTop();
        //buildFloorBottom();


        s.BuildNavMesh();

   
        
    }

    void buildFloorTop()
    {
        Vector2[] vertices2D = new Vector2[] {
            new Vector2(0,0),
            new Vector2(0,50),
            new Vector2(25,50),
        };
 
        // Use the triangulator to get indices for creating triangles
        Triangulator tr = new Triangulator(vertices2D);
        int[] indices = tr.Triangulate();
 
        // Create the Vector3 vertices
        Vector3[] vertices = new Vector3[vertices2D.Length];
        for (int i=0; i<vertices.Length; i++) {
            vertices[i] = new Vector3(vertices2D[i].x, 0 , vertices2D[i].y);
        }
 
        // Create the mesh
        Mesh msh = new Mesh();
        msh.vertices = vertices;
        msh.triangles = indices;
        // msh.RecalculateNormals();
        // msh.RecalculateBounds();
 
        // Set up game object with mesh;
        gameObject.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = gameObject.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;
    }

     void buildFloorBottom()
    {
        Vector2[] vertices2D = new Vector2[] {
            new Vector2(0,0),
            new Vector2(0,50),
            new Vector2(25,50),
        };
 
        // Use the triangulator to get indices for creating triangles
        Triangulator tr = new Triangulator(vertices2D);
        int[] indices = tr.Triangulate();
 
        // Create the Vector3 vertices
        Vector3[] vertices = new Vector3[vertices2D.Length];
        for (int i=0; i<vertices.Length; i++) {
            vertices[i] = new Vector3(vertices2D[i].x, 0 , vertices2D[i].y);
        }
 
        // Create the mesh
        Mesh msh = new Mesh();
        msh.vertices = vertices;
        msh.triangles = indices;
        // msh.RecalculateNormals();
        // msh.RecalculateBounds();
 
        // Set up game object with mesh;
        gameObject.AddComponent(typeof(MeshRenderer));
        MeshFilter filter = gameObject.AddComponent(typeof(MeshFilter)) as MeshFilter;
        filter.mesh = msh;
    }

    // Update is called once per frame
    void Update()
    {
        // if(i < 100)
        //     Instantiate(square, new Vector3(i++,1,j), new Quaternion(0, 0 , 0, 1));
        // else
        // {
        //     j++;
        //     i = 0;
        // }
    }
}