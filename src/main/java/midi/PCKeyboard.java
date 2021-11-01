package midi;

import audio.modules.io.Port;


import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class PCKeyboard{

    private static final Map<Character, Double> keyToFreqMap = new HashMap<>(){{
        put('y', 16.35); // C
        put('s', 17.32); // C#
        put('x', 18.35); // D
        put('d', 19.45); // D#
        put('c', 20.60); // E
        put('v', 21.83); // F
        put('g', 23.12); // F#
        put('b', 24.50); // G
        put('h', 25.96); // G#
        put('n', 27.50); // A
        put('j', 29.14); // A#
        put('m', 30.87); // B
    }};


    public Port mainOutput;
    public Port keyPressedOutput;

    private int keyPressed;

    private int octave = 3;
    private double frequency;



    public PCKeyboard() {
        mainOutput = () -> Math.pow(2, octave) * frequency;
        keyPressedOutput = () -> keyPressed;

        JFrame frame = new JFrame();
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
                    octave = e.getKeyCode() - 48;
                } else {
                    if (keyToFreqMap.containsKey(e.getKeyChar())) {
                        frequency = keyToFreqMap.get(e.getKeyChar());
                        keyPressed = 1;
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                if (keyToFreqMap.containsKey(e.getKeyChar())) {
                    keyPressed = 0;
                }
            }

        });
    }

}
