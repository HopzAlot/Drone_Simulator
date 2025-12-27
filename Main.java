package dronesimulator;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numDrones = 0;
        double deltaTime = 0.01;
        int maxSteps = 1000;
        double envW = 100, envH = 100, envD = 100;

        try {
            File configFile = new File("config.txt");
            Scanner reader = new Scanner(configFile);
            
            
            while (reader.hasNext()) {
                String key = reader.next(); 

                if (key.equals("numDrones:")) {
                    numDrones = reader.nextInt();
               } else if (key.equals("deltaTime:")) {
                    deltaTime = reader.nextDouble();
                } else if (key.equals("maxSteps:")) {
                    maxSteps = reader.nextInt();
                } else if (key.equals("envWidth:")) {
                    envW = reader.nextDouble();
                } else if (key.equals("envHeight:")) {
                    envH = reader.nextDouble();
                } else if (key.equals("envDepth:")) {
                    envD = reader.nextDouble();
                }
            }
            reader.close();
            System.out.println("Configuration loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("config.txt not found.");
        }

        
        Environment env = new Environment(envW, envH, envD); 

       
        CommunicationModule communication = new CommunicationModule();
        Logger logger = new Logger();

        
        ArrayList<Drone> drones = new ArrayList<>();
        
        for (int i = 0; i < numDrones; i++) {
            Vector3 startPos = new Vector3(10 + i * 5, 10, 10);
            Vector3 target = new Vector3(90 - i * 2, 50 + i * 3, 25 + i);
            Drone d = new Drone(startPos, target);
            drones.add(d);
        }
        Simulator simulator = new Simulator(drones, deltaTime, maxSteps, communication, env, logger);
        SimulationGUI gui = new SimulationGUI(drones, simulator);
        simulator.setGUI(gui);
        System.out.println("Simulation ready!!! Δt: " + deltaTime);
    }
}