using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class AgentController : MonoBehaviour
{
    public NavMeshAgent agent;
    bool started = false;
    // Start is called before the first frame update
    void Start()
    {
        
        // agent.SetDestination(GameObject.FindWithTag("door").transform.position);
       //Color color = new Color32(0, 0, 0, 0);

        //Color color = GameObject.FindWithTag("door").GetComponent<Renderer>().material.color;
        //gameObject.GetComponent<Renderer>().material.color = color;
        // agent.destination = new Vector3(1,1,1);
    }

    public void goTo(int x)
    {



        var goArray = FindObjectsOfType<GameObject>();
       
            for (int j = 0; j < goArray.Length; j++)
            {
            if (goArray[j].GetComponent<number>() != null)
                if (goArray[j].GetComponent<number>().objectNumber == x)
                {
                    agent.SetDestination( goArray[j].transform.position);
                    Color color = goArray[j].GetComponent<Renderer>().material.color;
                    gameObject.GetComponent<Renderer>().material.color = color;
                }
            }
        

        started = true;
    }

    public void goTo(GameObject x)
    {
       // Debug.Log("going to: "+x.transform.position.x +" "+x.transform.position.y);
        //var goArray = FindObjectsOfType<GameObject>();
        agent.SetDestination( x.transform.position);
        Color color = x.GetComponent<Renderer>().material.color;
        gameObject.GetComponent<Renderer>().material.color = color;
        started = true;
    }

    // Update is called once per frame
    void Update()
    {
        if(started)
      if ( Vector3.Distance( agent.destination, agent.transform.position)-0.36 <= agent.stoppingDistance)
         {
            // Debug.Log("diatnce: "+Vector3.Distance( agent.destination, agent.transform.position));
            //  if (!agent.hasPath || agent.velocity.sqrMagnitude == 0f)
            //  {
                 Destroy(gameObject);
            //  }
         }

    }

    private void OnCollisionEnter(Collision collision)
    {
        if(collision.gameObject.tag == "door")
        {


            Destroy(this.gameObject);
            Debug.Log("here");
        }
        Debug.Log("here1");
    }
}
