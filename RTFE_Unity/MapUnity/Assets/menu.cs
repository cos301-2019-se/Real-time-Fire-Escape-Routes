using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
public class menu : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void DeleteAllNonPemanent()
    {
        foreach (GameObject gameObj in GameObject.FindObjectsOfType<GameObject>())
        {
            // modify gameObj.transform.rotation
            if (gameObj.tag != "permanent")
            {
                Destroy(gameObj);
            }
        }
    }

    public void SceneChange()
    {
        Debug.Log("Change Scene");
        Dropdown ddown = GameObject.Find("DropdownScene").GetComponentInChildren<Dropdown>();
        Debug.Log(ddown.options[ddown.value].text);// = "ALARM TRIGGERED";
        if(ddown.options[ddown.value].text == "SIMULATION")
        {
            GameObject.Find("ObjectCounter").GetComponent<Counter>().isSimulation = true;
        }
        else
        {
            GameObject.Find("ObjectCounter").GetComponent<Counter>().isSimulation = false;
        }

        DeleteAllNonPemanent();
        GameObject.Find("ObjectCounter").GetComponent<Counter>().runOnce = false;


    }

    public void TriggerAlarm()
    {
        Debug.Log("triggering alarm");
        if(GameObject.Find("AlarmTrigger") != null)
        {
            
            if(GameObject.Find("ObjectCounter").GetComponent<Counter>().alarm == 0)
            {
                GameObject.Find("AlarmTrigger").GetComponentInChildren<Text>().text = "TRIGGER ALARM";
                GameObject.Find("ObjectCounter").GetComponent<Counter>().alarm = -1;
            }
            else
            {
                GameObject.Find("AlarmTrigger").GetComponentInChildren<Text>().text = "ALARM TRIGGERED";
                GameObject.Find("ObjectCounter").GetComponent<Counter>().alarm = 0;
                GameObject.Find("ObjectCounter").GetComponent<Counter>().once = true;
            }
        }
    }
}
