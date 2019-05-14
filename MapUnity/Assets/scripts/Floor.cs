using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Floor : MonoBehaviour
{
    public List<GameObject> rooms;
    public float[][] doors;
    public int floorNumber;
    public GameObject[] people;

    private void Awake()
    {
        rooms = new List<GameObject>();
    }

    public void addAllDoors(float[][] d)
    {
        doors = d;
    }


    public void addRoom(float[][] corners)
    {
        GameObject x = Instantiate(Resources.Load("Room", typeof(GameObject)) as GameObject, new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1)) as GameObject;
        rooms.Add(x);

        //todo: check if anys doors fall between any of the corners and then put them into and array and label door indexes.
        x.GetComponent<Room>().build(corners, floorNumber);
    }
}