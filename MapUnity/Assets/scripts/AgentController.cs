using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class AgentController : MonoBehaviour
{
    public NavMeshAgent agent;
    // Start is called before the first frame update
    void Start()
    {
        
         //agent.SetDestination(GameObject.FindWithTag("door").transform.position);
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
                }
            }
        


    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
