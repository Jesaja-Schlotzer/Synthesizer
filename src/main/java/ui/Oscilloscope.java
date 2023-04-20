package ui;

import javax.swing.*;
import java.awt.*;

public class Oscilloscope extends JFrame {

    private static final int WIDTH = 800; // Breite des Oszilloskops
    private static final int HEIGHT = 600; // Höhe des Oszilloskops

    private final int BUFFER_SIZE = 1024; // Größe des Datenpuffers
    private double[] dataBuffer = new double[BUFFER_SIZE]; // Datenpuffer für das Signal

    private int dataPointer = 0; // Zeiger auf das aktuelle Daten-Array

    private JPanel displayPanel; // Panel für die Anzeige des Signals

    public Oscilloscope() {
        // Erstelle ein neues Fenster für das Oszilloskop
        super("Oszilloskop");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstelle ein neues JPanel für die Anzeige des Signals
        displayPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Zeichne das Signal im JPanel
                for (int i = 0; i < dataPointer - 1; i++) {
                    g.drawLine(i, (int) (getHeight() / 2 - dataBuffer[i] * getHeight() / 2), i + 1, (int) (getHeight() / 2 - dataBuffer[i + 1] * getHeight() / 2));
                }
            }
        };
        getContentPane().add(displayPanel, BorderLayout.CENTER);

        // Starte den Update-Thread für die Signalanzeige
        new Thread(() -> {
            while (true) {
                // Aktualisiere das Anzeige-Panel alle 20 Millisekunden
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                displayPanel.repaint();
            }
        }).start();
    }

    // Füge ein neues Signal zum Datenpuffer hinzu
    public void addData(double value) {
        //if (data
    }
}
