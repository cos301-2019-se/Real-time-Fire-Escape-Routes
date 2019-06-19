using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[SerializeField]
public class number : MonoBehaviour
{
    public double objectNumber;
    public int[][] array;
    public string type = "";
    public float x, y ,z;

    private void OnCollisionEnter(Collision collision)
    {
        if(type == "")
        if (collision.gameObject.tag == "Player")
        {


            Destroy(this.gameObject);
            Debug.Log("here");
        }
        Debug.Log("here1");
    }
}


