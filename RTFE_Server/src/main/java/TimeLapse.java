import Builder.BuildingManager;
import TimeLapse.FileGenerator;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class TimeLapse {
    public static void main(String[] args){

        String input = "";
        Scanner scanner = new Scanner(System.in);
        FileGenerator FakeSensors;
        String filename = "";
        int numPeople = 0;
        long timespan = 0;
        long updateInterval = 0;
        double Chance = 0;
        boolean one,two,three,four,five,allFields;
        one=two=three=four=five=allFields=false;
        if(args.length==5){
            allFields= true;
            filename = (args[0]);
            numPeople =  Integer.parseInt(args[1]);
            timespan = Integer.parseInt( args[2]);
            updateInterval = Integer.parseInt(args[3]);
            Chance =Double.parseDouble( args[4]);
        }

        while(!allFields){
            try {
                if (filename.compareTo("") == 0 && !one) {
                    System.out.print("Please specify a file to import: ");
                    input = scanner.nextLine();
                    filename = input;
                    one = true;
                }
                if (numPeople == 0 && !two) {
                    System.out.print("Please specify number of people to generate: ");
                    input = scanner.nextLine();
                    numPeople = Integer.parseInt(input);
                    input = "";
                    two = true;
                }
                if (timespan == 0 && !three) {
                    System.out.print("Please Please specify the timespan to be used: ");
                    input = scanner.nextLine();
                    timespan = Integer.parseInt(input);
                    input = "";
                    three = true;
                }
                if (updateInterval == 0 && !four) {
                    System.out.print("Please specify how regular updates should occur: ");
                    input = scanner.nextLine();
                    updateInterval = Integer.parseInt(input);
                    input = "";
                    four = true;
                }
                if (Chance == 0 && !five) {
                    System.out.print("Please specify how active users are (int)Chance: ");
                    input = scanner.nextLine();
                    Chance = Integer.parseInt(input);
                    input = "";
                    five = true;
                }

                if (one && two && three && four && five) {
                    allFields = true;
                }
            }catch (Exception e){
                System.out.println("Error: "+e.getMessage());
            }
        }

        try {
            System.out.println("Loading "+filename+"...");
            JSONObject buildingData = FileGenerator.readFile(filename);
            System.out.println("Loading finished");
            System.out.println("Constructing Building");
            BuildingManager JSONtoBuilding = new BuildingManager(buildingData);
            //FileGenerator(Building building,int NumberOfPeople, long timespan,long updateInterval,double movementChance)

            FakeSensors = new FileGenerator(JSONtoBuilding.construct(),numPeople,timespan,updateInterval,Chance);
            System.out.println("Starting Simulation ");
            FakeSensors.start();
            System.out.println("Simulation Finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
