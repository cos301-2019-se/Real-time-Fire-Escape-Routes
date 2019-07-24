package TimeLapse;

import Building.*;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @brief This class is used to set and generate timelapse files based on settings provided
 * */
public class FileGenerator {
    public Building building;
    long updateInterval;
    int numPeople;
    long timespan;
    int BotStartId = 1000;
    double ChanceToMove;

    /**
     * The constructor is used to set the various settings for the file that is being generated
     * @param building: The building to be used. It is vital as the code uses it for graph traversal
     * @param movementChance: This is used to decide how likely people are to move from one location to the next on a given update
     * @param NumberOfPeople: This parameter is used to specify how many people's data must be simulated in the timelapse file
     * @param updateInterval: This is used to determine how frequently(in milliseconds) people's decide to move from one location to the next
     * @param timespan: This is used to determine the start and end period for the timelapse file
     * */
    public FileGenerator(Building building,int NumberOfPeople, long timespan,long updateInterval,double movementChance){
        this.building = building;
        this.updateInterval= updateInterval;
        this.numPeople = NumberOfPeople;
        this.timespan = timespan;
        this.ChanceToMove = movementChance;
    }

    /**
     * The start function makes use of the simulated building
     * and all its predefined locations and calls all the
     * relevant functions to generate the timelapse file
     * */
    public void start(){
        building.clearPeople();;
        System.out.println("Starting Simulation - "+new Date(System.currentTimeMillis()));
        System.out.println("Expected Finish time: "+new Date(System.currentTimeMillis()+timespan));
        InitialBuild();
        long currentTime = System.currentTimeMillis();
        long nextUpdateTime = currentTime;
        long endTime = currentTime+timespan;

        while(currentTime<endTime){

                UpdatePeople();
                updatefile(currentTime);
                nextUpdateTime = currentTime+ updateInterval;
            currentTime+=updateInterval;
        }

    }

    /**
     * The updateFile will pull all the people's position data from
     * the building and write each persons location into
     * the text file with a given timestamp
     * @param time: the timestamp to be used when writing into the file as well as terminal prompt
     * */
    void updatefile(long time){

        System.out.println("Updating file "+new Date(time));
        Vector<Person> people = building.getPeople();
        try{
            FileWriter fw=new FileWriter("timelapse.txt",true);
            for (Person p :people) {
                String line ="timestamp:"+time+ " ID:"+p.getName()+" DeviceID:"+p.deviceID+" location:"+ Arrays.toString(p.getPosition())+" floor:"+p.floor+"\n";
                fw.write(line);
            }
            fw.close();
        }catch(Exception e){System.out.println(e);}
    }

    /**
     * The function updatePeople will based on moveChance change the position of where a person is located.
     * */
    private void UpdatePeople(){
        Random rand = new Random();
        Vector<Person> people = new Vector<>();
        people.addAll(building.getPeople());
        for (Person p:people) {
            if(rand.nextInt(100)<ChanceToMove) {//If the random value is less than the chance it is seen as a move otherwise their location stays the same
                Sensor nextLocation = nextLocation(p);
                building.updatePersonLocation(Integer.parseInt(p.getName()), nextLocation.floor, nextLocation.coordinates);
            }
        }
    }
    /**
     * This function takes a person and determines a random sensor where he should move to that is adjacent to his/her current room
     * @param p: the person of interest looking for a next location
     * */
    private Sensor nextLocation(Person p){
        Vector<Sensor> sensors = new Vector<Sensor>();
        for (Room f:building.getFloors() ) {
            sensors.addAll(f.getAllSensors());
            f.getPeopleData(building.getRoutes());
        }
        Routes r = building.getRoutes().get(0);
        Vector<Sensor> possibleChoices = new Vector<>();
        for (Sensor s:sensors) {
            for (Door d:p.availableDoors) {
                for (Path c:d.node.getPaths()) {
                    r.resetVisited();
                    Vector<Node> path =  r.ShortestPathToGoal(c.end,s);
                    path.insertElementAt(c.start,0);
                    if(path.size() <= 2){
                        //Routes.printPath(path,p);
                        possibleChoices.add(s);
                    }
                }
            }
        }

        Random rand = new Random();
        return possibleChoices.get(rand.nextInt(possibleChoices.size()));
    }

    /**
     * This function randomly places people throughout the building until satisfied
     * */
    private void InitialBuild(){
        int PeopleToPlace = numPeople;
        int numFloors = building.getNumFloors();
        Random rand = new Random();
        while(PeopleToPlace> 0){
            double [] pos = { rand.nextInt(100),rand.nextInt(100)};
            int floor = rand.nextInt(numFloors);
            Person p = new Person(String.valueOf(BotStartId),pos);
            if(building.addPerson(p,floor)){
                BotStartId++;
                System.out.println("Person:"+p.getName()+" is placed");
                PeopleToPlace--;
            }
        }
    }

    /**
     * This function reads a given building.json file and returns a JSONObject that can be utilized by the system
     * */
    public static JSONObject readFile(String fileName) throws IOException {
        File f = new File(fileName);
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            String fileContents = "";
            String line;
            FileReader fileReader = new FileReader(f);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                fileContents += line;
            }
            bufferedReader.close();
            return new JSONObject(fileContents);
        }catch (IOException e){
            throw e;
        }
        finally {
            lock.unlock();
        }
    }
}
