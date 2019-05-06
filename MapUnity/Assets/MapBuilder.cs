using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MapBuilder : MonoBehaviour
{
    public GameObject square;
    // Start is called before the first frame update
    int i, j;

    void Start()
    {
        i = 0;
        j = 0;
        // for(int i = 0; i < 1000; i ++)
        // {
        //     for(int j = 0; j < 1000; j++ )
        //     {
        //         Instantiate(square, new Vector3(i,1,j), new Quaternion(0, 0 , 0, 1));
        //     }
        // }
        
    }

    // Update is called once per frame
    void Update()
    {
        if(i < 100)
            Instantiate(square, new Vector3(i++,1,j), new Quaternion(0, 0 , 0, 1));
        else
        {
            j++;
            i = 0;
        }
    }
}
