package midi;

import audio.components.Generator;
import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.modulators.envelopes.Envelope;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.modulated.ModulatedOscillator;
import audio.interfaces.Modulator;
import io.interfaces.Pressable;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class PCKeyboard extends Generator{

    private static final Map<Character, Key> keyToFreqMap = new HashMap<>(){{
        put('y', new Key(16.35)); // C
        put('s', new Key(17.32)); // C#
        put('x', new Key(18.35)); // D
        put('d', new Key(19.45)); // D#
        put('c', new Key(20.60)); // E
        put('v', new Key(21.83)); // F
        put('g', new Key(23.12)); // F#
        put('b', new Key(24.50)); // G
        put('h', new Key(25.96)); // G#
        put('n', new Key(27.50)); // A
        put('j', new Key(29.14)); // A#
        put('m', new Key(30.87)); // B
    }};



    private JFrame frame;

    private Mixer mixer;

    private int octave;
    private double frequency;



    public PCKeyboard(ModulatedOscillator oscillator, Envelope envelope) {

        keyToFreqMap.forEach((chr, key) -> key.setGenerator(oscillator.clone()));
        keyToFreqMap.forEach((chr, key) -> key.setEnvelope(envelope.clone()));

        Generator[] keyGenerators = new Generator[keyToFreqMap.size()];

        int i = 0;
        for (Key key : keyToFreqMap.values()) {
            keyGenerators[i++] = key.keyGenerator;
        }

        mixer = new Mixer(keyGenerators);

        frame = new JFrame();

        frame.setAutoRequestFocus(true);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
                    octave = e.getKeyCode() - 48;
                }else {
                    if(keyToFreqMap.containsKey(e.getKeyChar())) {
                        keyToFreqMap.get(e.getKeyChar()).press();
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {

            }

        });


    }


    public Generator getOutputGenerator() {
        return mixer;
    }




    @Override
    public double next() {
        return 0;
    }


    @Override
    public String toString() {
        return null; // TODO;
    }


    @Override
    public Generator clone() {
        return null; //TODO new PCKeyboard(keyToFreqMap.get('y').keyGenerator, keyToFreqMap.get('y').keyEnvelope);
    }


    private static class Key implements Pressable {

        private double baseFrequency;
        private Generator keyGenerator;
        private Envelope keyEnvelope;


        public Key(double baseFrequency) {
            this.baseFrequency = baseFrequency;
        }



        @Override
        public void press() {

        }


        @Override
        public void release() {

        }


        public void setGenerator(Generator generator) {
            this.keyGenerator = generator;
        }

        public void setEnvelope(Envelope envelope) {

        }
    }

}
