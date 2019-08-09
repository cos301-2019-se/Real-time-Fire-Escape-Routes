using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SceneLoader : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        SceneManager.LoadScene("SampleScene1", LoadSceneMode.Additive);
        SceneManager.LoadScene("SampleScene2", LoadSceneMode.Additive);
    }

        // Update is called once per frame
        void Update()
    {
        
    }
}
