package dronesimulator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class DroneVisualizer extends JPanel {
    private ArrayList<Drone> drones;
    private final int SCALE = 6; 
    private final int OFFSET_X = 50;
    private final int OFFSET_Y = 50;

    public DroneVisualizer(ArrayList<Drone> drones) {
        this.drones = drones;
        this.setBackground(new Color(30, 30, 30)); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(OFFSET_X, OFFSET_Y, 100 * SCALE, 100 * SCALE);

        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(1));
        for(int i = 0; i <= 100; i += 10) {
            
            g2.drawLine(i * SCALE + OFFSET_X, OFFSET_Y, i * SCALE + OFFSET_X, 100 * SCALE + OFFSET_Y);
           
            g2.drawLine(OFFSET_X, i * SCALE + OFFSET_Y, 100 * SCALE + OFFSET_X, i * SCALE + OFFSET_Y);
        }

        
        for (Drone d : drones) {
            Vector3 p = d.getPosition();
            Vector3 target = d.getController().getTarget(); 

            int tx = (int) (target.x * SCALE) + OFFSET_X;
            int ty = (int) (target.y * SCALE) + OFFSET_Y;
            
            g2.setColor(Color.RED);
            g2.drawLine(tx-4, ty-4, tx+4, ty+4);
            g2.drawLine(tx+4, ty-4, tx-4, ty+4);

            int x = (int) (p.x * SCALE) + OFFSET_X;
            int y = (int) (p.y * SCALE) + OFFSET_Y;
            int size = Math.max(6, (int) (8 + p.z / 2)); 
            g2.setColor(new Color(255, 255, 255, 50));
            g2.drawLine(x, y, tx, ty);
            g2.setColor(new Color(0, 180, 255));
            g2.fillOval(x - size/2, y - size/2, size, size);
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString("Alt: " + (int)p.z, x + 8, y);
        }
    }
}