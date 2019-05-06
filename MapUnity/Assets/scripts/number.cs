using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[SerializeField]
public class number : MonoBehaviour
{
    public int objectNumber;
    public int[][] array;

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.tag == "Player")
        {


            Destroy(this.gameObject);
            Debug.Log("here");
        }
        Debug.Log("here1");
    }
}


