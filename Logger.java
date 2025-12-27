package dronesimulator;

import java.io.*;
import java.util.*;
public class Logger {
    private FileOutputStream posStream;
    private int totalMessages = 0;
    private int successfulMessages = 0;

    public void init() {
        try {
            
            posStream = new FileOutputStream("positions.csv");
  
            posStream.write("time,droneID,x,y,z,vx,vy,vz,Tz\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(double time, int id, Drone d) {
        try {
            Vector3 p = d.getPosition(); 
            Vector3 v = d.getVelocity(); 
            
            double tz = d.getController().getBodyThrustZ(d);
            
            String line = time + "," + id + "," +
                          p.x + "," + p.y + "," + p.z + "," +
                          v.x + "," + v.y + "," + v.z + "," + tz + "\n";
            posStream.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logComm(boolean success) {
        totalMessages++;
        if (success) successfulMessages++;
    }


    public double calculateAvgSpacing(ArrayList<Drone> drones) {
        double totalDist = 0;
        int pairCount = 0;
        int n = drones.size();
        
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    totalDist += drones.get(i).getPosition().subtract(drones.get(j).getPosition()).magnitude();
                    pairCount++;
                }
            }
        }
        return (pairCount > 0) ? totalDist / pairCount : 0;
    }

    public int calculateCollisions(ArrayList<Drone> drones) {
        int collisions = 0;
        double dMin = 1.0; 
        
        
        for (int i = 0; i < drones.size(); i++) {
            for (int j = i + 1; j < drones.size(); j++) {
                double dist = drones.get(i).getPosition().subtract(drones.get(j).getPosition()).magnitude();
                if (dist < dMin) collisions++;
            }
        }
        return collisions;
    }


    public void writeMetrics(ArrayList<Drone> drones) {
        try {
            FileOutputStream fos = new FileOutputStream("metrics.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            
            double avgSpacing = calculateAvgSpacing(drones);
            int collisions = calculateCollisions(drones);
        
            double commRate = (totalMessages > 0) ? (double) successfulMessages / totalMessages : 0;

            dos.writeUTF("Performance Metrics Summary\n");
            dos.writeUTF(String.format("Average Spacing: %.4f m\n", avgSpacing));
            dos.writeUTF("Collision Count: " + collisions + "\n");
            dos.writeUTF(String.format("Communication Success Rate: %.2f%%\n", commRate * 100));
            
            dos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (posStream != null) posStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}