using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FlyCamera : MonoBehaviour
{

    public Transform cam;
    public float X = 0;
    public float Z = 0;


    private void FixedUpdate()
    {
        Move();
    }
    //void fixe()
    //{
    //    Move();
    //}


   
    private void Move()
    { //returns the basic values, if it's 0 than it's not active.
        Vector3 p_Velocity = new Vector3(GetComponent<Rigidbody>().position.x , GetComponent<Rigidbody>().position.y, GetComponent<Rigidbody>().position.z);
        bool moved = false;
        if (Input.GetKey(KeyCode.W))
        {
            moved = true;
            p_Velocity += new Vector3(0, 0, 0.5f);
        }
        if (Input.GetKey(KeyCode.S))
        {
            moved = true;
            p_Velocity += new Vector3(0, 0, -0.5f);
        }
        if (Input.GetKey(KeyCode.A))
        {
            moved = true;
            p_Velocity += new Vector3(-0.5f, 0, 0);
        }
        if (Input.GetKey(KeyCode.D))
        {
            moved = true;
            p_Velocity += new Vector3(0.5f, 0, 0);
        }

        if (moved)
        {
            
            

        

            //Vector3 camF = cam.forward;
            //Vector3 camR = cam.right;
            //camF.y = 0;
            //camR.y = 0;
            //camF = camF.normalized;
            //camR = camR.normalized;


            GetComponent<Rigidbody>().position = (p_Velocity);
            //GetComponent<Rigidbody>().position += (camF * p_Velocity.x + camR * p_Velocity.z) * Time.deltaTime;
        }
        Vector3 target = new Vector3(X, 0, Z+2f);
        Transform myTransform = GetComponent<Rigidbody>().transform;
        myTransform.rotation = Quaternion.Slerp(myTransform.rotation, Quaternion.LookRotation(target - myTransform.position), 1f * Time.deltaTime);
    }
    
}
