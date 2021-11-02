package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.oscillators.*;
import audio.enums.WaveForm;
import audio.modules.controls.ControlKnob;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;
import io.AudioPlayer;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class PCKeyboardOscillatorModule {

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


    private Map<Character, KeyPort> keyPortMap = new HashMap<>(){{
        put('y', null); // C
        put('s', null); // C#
        put('x', null); // D
        put('d', null); // D#
        put('c', null); // E
        put('v', null); // F
        put('g', null); // F#
        put('b', null); // G
        put('h', null); // G#
        put('n', null); // A
        put('j', null); // A#
        put('m', null); // B
    }};


    public final ControlKnob attackKnob;
    public final ControlKnob decayKnob;
    public final ControlKnob sustainKnob;
    public final ControlKnob releaseKnob;



    @OutputPort
    private Port mainOutput;

    public Port getMainOutput() {
        return mainOutput;
    }



    private int octave = 3;



    public PCKeyboardOscillatorModule(WaveForm oscillatorType) { // TODO TODO Input Port (von alles) in Konstruktor packen (weil wieso nicht) gerade bei Envelope ganz praktisch
        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        Mixer mixer = new Mixer();

        for (Map.Entry<Character, KeyPort> e : keyPortMap.entrySet()) {
            ADSREnvelope adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);

            Oscillator oscillator = null;

            switch (oscillatorType) {
                case SINE -> {
                    oscillator = new SineOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()), () -> 100/*adsrEnvelope::modulate*/, 44100);
                    e.setValue(new KeyPort());

                } case SQUARE -> {
                    oscillator = new SquareOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()), () -> 100/*adsrEnvelope::modulate*/, 44100, () -> 0.5);
                    e.setValue(new KeyPort());

                } case SAWTOOTH -> {
                    oscillator = new SawtoothOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()), () -> 100/*adsrEnvelope::modulate*/, 44100);
                    e.setValue(new KeyPort());

                } case TRIANGLE -> {
                    oscillator = new TriangleOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()), () -> 100/*adsrEnvelope::modulate*/, 44100);
                    e.setValue(new KeyPort());

                }
            }

            adsrEnvelope.setMainInputPort(oscillator.getMainOutput());//() -> 100);
            adsrEnvelope.setTriggerInputPort(e.getValue().keyPressedOutput);

            mixer.addInputPort(adsrEnvelope.getMainOutputPort());//oscillator::next);

        }

        mainOutput = mixer.getMainOutputPort();


        JFrame frame = new JFrame();
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
                    octave = e.getKeyCode() - 48;
                } else {
                    if (keyToFreqMap.containsKey(e.getKeyChar())) {
                        keyPortMap.get(e.getKeyChar()).keyPressed = 1;
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                if (keyToFreqMap.containsKey(e.getKeyChar())) {
                    keyPortMap.get(e.getKeyChar()).keyPressed = 0;
                }
            }
        });

    }



    private static class KeyPort {
        private int keyPressed;
        private final Port keyPressedOutput = () -> keyPressed;
    }





    public static void main(String[] args) {

        PCKeyboardOscillatorModule module = new PCKeyboardOscillatorModule(WaveForm.SAWTOOTH);

        module.attackKnob.setValue(10000);
        module.decayKnob.setValue(0);
        module.sustainKnob.setValue(0.8);
        module.releaseKnob.setValue(40000);


        AudioPlayer audioPlayer = new AudioPlayer(module.getMainOutput());

        audioPlayer.init();
        audioPlayer.start();


    }

}
