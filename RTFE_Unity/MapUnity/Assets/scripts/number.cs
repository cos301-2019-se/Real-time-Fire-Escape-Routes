using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[SerializeField]
public class number : MonoBehaviour
{
    public bool exists = true;
    public double objectNumber;
    public int[][] array;
    public string type = "";
    public float x, y ,z;
    public int showBelow = -1;

    private void Update()
    {

        if (showBelow != -1)
        {
            double currentHeight = gameObject.transform.position.y;
            if (currentHeight < showBelow * 3)
            {
                gameObject.GetComponent<MeshRenderer>().enabled = true;
            }
            else
            {
                gameObject.GetComponent<MeshRenderer>().enabled = false;
            }
        }
    }


}


