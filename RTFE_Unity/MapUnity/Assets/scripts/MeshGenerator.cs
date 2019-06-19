using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(MeshFilter))]
public class MeshGenerator : MonoBehaviour
{
	Mesh mesh;

	Vector3[] vertices; 
	int[] triangles;

	public int xSize = 20;
	public int zSize = 20; 
    // Start is called before the first frame update
    void Start()
    {
        mesh = new Mesh(); 
        GetComponent<MeshFilter>().mesh = mesh; 

        StartCoroutine(CreateShape());
    }

    void Update(){
    	UpdateMesh();
    }

    IEnumerator  CreateShape(){
    	vertices = new Vector3[((xSize + 1) * (zSize + 1)) * 2];

    	for (int y = 0, i = 0; y < 2; y++){
	    	for (int z = 0; z <= zSize; z++){
	    		for  (int x = 0; x <= xSize; x++){
	    			vertices[i] = new Vector3(x, y, z);
	    			i++;
	    		}
	    	}
	    }

    	triangles = new int[(xSize * zSize * 6) + 6];
    	int tris = 0;
    	for (int z = 0, vert = 0; z < zSize; z++){
    		for (int x = 0; x < xSize; x++){
		    	triangles[tris] = vert + 0;
		    	triangles[tris + 1] = vert + xSize + 1;
		    	triangles[tris + 2] = vert + 1;
		    	triangles[tris + 3] = vert + 1;
		    	triangles[tris + 4] = vert + xSize + 1;
		    	triangles[tris + 5] = vert + xSize + 2;

		    	vert++;
		    	tris += 6;

		    	yield return new WaitForSeconds(.01f);
   			}
   			vert++;
    	}

    	int xValue = (xSize + 1) * (xSize + 1); 

    	triangles[tris + 0] = xValue;
    	triangles[tris + 1] = 1;
    	triangles[tris + 2] = 0;
    	triangles[tris + 3] = xValue;
    	triangles[tris + 4] = xValue + 1;
    	triangles[tris + 5] = 1;
    	yield return new WaitForSeconds(.01f);
    }

    void UpdateMesh(){
    	mesh.Clear();

    	mesh.vertices = vertices;
    	mesh.triangles = triangles; 

    	mesh.RecalculateNormals();
    }

    private void OnDrawGizmos(){
    	if (vertices == null) return;

    	for (int i = 0; i<vertices.Length; i++){
    		Gizmos.DrawSphere(vertices[i], .1f);
    	}
    }
}
