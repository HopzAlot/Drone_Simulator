package dronesimulator;

import java.util.ArrayList;
public class Simulator {

    private ArrayList<Drone> drones;
    private double dt;
    private int steps;
    private CommunicationModule comm;
    private Environment env;
    private Logger logger;
    private SimulationGUI gui;

    public Simulator(
            ArrayList<Drone> drones,
            double dt,
            int steps,
            CommunicationModule comm,
            Environment env,
            Logger logger) {

        this.drones = drones;
        this.dt = dt;
        this.steps = steps;
        this.comm = comm;
        this.env = env;
        this.logger = logger;
    }

    public void setGUI(SimulationGUI gui) {
        this.gui = gui;
    }


    public void run() {
       
        logger.init();

        for (int s = 0; s < steps; s++) {
            double currentTime = s * dt;
            for (int i = 0; i < drones.size(); i++) {
                Drone d = drones.get(i);
                d.step(drones, dt, env); 
                logger.log(currentTime, i, d); 
            }
            for (Drone d : drones) {
                
                ArrayList<SharedData> receivedData = comm.exchange(d, drones);
                boolean success = (receivedData != null && !receivedData.isEmpty());
                logger.logComm(success); 
            }

            if (gui != null && s % 10 == 0) {
                double avgSpacing = logger.calculateAvgSpacing(drones);
                int collisions = logger.calculateCollisions(drones);   
                gui.updateDisplay(s, avgSpacing, collisions);
            }

            try {
                Thread.sleep(20); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.writeMetrics(drones);
        logger.close();
        
        System.out.println("Simulation completed successfully.");
    }
}