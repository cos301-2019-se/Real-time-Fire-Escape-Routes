using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
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

    public void LoadNextScene()
    {
            if (SceneManager.GetActiveScene().name == "SampleScene1")
            {
                SceneManager.LoadSceneAsync("SampleScene2");
            }
            if (SceneManager.GetActiveScene().name == "SampleScene2")
            {
                SceneManager.LoadSceneAsync("SampleScene1");
            }
    }
}
