package dronesimulator;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SimulationGUI extends JFrame {
    private DroneVisualizer visualizer;
    private JLabel spacingLabel;
    private JLabel collisionLabel;
    private JLabel stepLabel;
    private JTextArea droneStatsArea;
    private Simulator simulator;
    private ArrayList<Drone> drones;

    public SimulationGUI(ArrayList<Drone> drones, Simulator sim) {
        this.simulator = sim;
        this.drones = drones;
        
        setTitle("Autonomous Drone Fleet Simulator");
        setSize(1200, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        visualizer = new DroneVisualizer(drones);
        add(visualizer, BorderLayout.CENTER);
        JPanel dashboard = new JPanel();
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));
        dashboard.setPreferredSize(new Dimension(350, 800));
        dashboard.setBorder(BorderFactory.createTitledBorder("System Performance"));
        dashboard.setBackground(new Color(240, 240, 240));

        stepLabel = new JLabel("Time Step: 0");
        spacingLabel = new JLabel("Avg Spacing: 0.00 m");
        collisionLabel = new JLabel("Collisions: 0");

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        stepLabel.setFont(labelFont);
        spacingLabel.setFont(labelFont);
        collisionLabel.setFont(labelFont);

        dashboard.add(Box.createVerticalStrut(10));
        dashboard.add(stepLabel);
        dashboard.add(spacingLabel);
        dashboard.add(collisionLabel);
        dashboard.add(Box.createVerticalStrut(20));
        JLabel statsTitle = new JLabel("Individual Drone States (P, V, |ω|):");
        statsTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        dashboard.add(statsTitle);

        droneStatsArea = new JTextArea();
        droneStatsArea.setEditable(false);
        droneStatsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(droneStatsArea);
        dashboard.add(scrollPane);

        add(dashboard, BorderLayout.EAST);
        JPanel controls = new JPanel();
        JButton startBtn = new JButton("Run Simulation");
        startBtn.addActionListener(e -> {
            startBtn.setEnabled(false);
            new Thread(() -> {
                simulator.run(); 
            }).start();
        });
        controls.add(startBtn);
        add(controls, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void updateDisplay(int currentStep, double avgSpacing, int collisions) {
        SwingUtilities.invokeLater(() -> {
            stepLabel.setText("Time Step: " + currentStep);
            spacingLabel.setText(String.format("Avg Spacing: %.2f m", avgSpacing));
            collisionLabel.setText("Collisions: " + collisions);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < drones.size(); i++) {
                Drone d = drones.get(i);
                Vector3 p = d.getPosition();
                Vector3 v = d.getVelocity();
                double wMag = d.getAngularVelocity().magnitude(); 
                
                sb.append(String.format("Drone %d:\n", i));
                sb.append(String.format(" P: [%.1f, %.1f, %.1f]\n", p.x, p.y, p.z));
                sb.append(String.format(" V: [%.1f, %.1f, %.1f]\n", v.x, v.y, v.z));
                sb.append(String.format(" |ω|: %.2f rad/s\n", wMag));
            }
            droneStatsArea.setText(sb.toString());
            visualizer.repaint();
        });
    }
}