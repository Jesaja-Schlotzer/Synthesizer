package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.oscillators.*;
import audio.enums.SampleRate;
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

public class FMSynth {

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


    private final Map<Character, FMSynth.KeyPort> keyToKeyPortMap = new HashMap<>(){{
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

    // VCA
    public final ControlKnob VCA_LEVEL;

    public final ControlKnob VCA_ENV_ATTACK;
    public final ControlKnob VCA_ENV_DECAY;
    public final ControlKnob VCA_ENV_SUSTAIN;
    public final ControlKnob VCA_ENV_RELEASE;

    // OP1
    public final ControlKnob OP1_LEVEL;
    public final ControlKnob OP1_FREQ_RATIO;


    // OP2
    public final ControlKnob OP2_LEVEL;
    public final ControlKnob OP2_FREQ_RATIO;

    // OP3
    public final ControlKnob OP3_LEVEL;
    public final ControlKnob OP3_FREQ_RATIO;



    @OutputPort
    private final Port mainOutputPort;

    /**
     * @return the <code>Port</code> that supplies the output audio of the synth
     */
    public Port getMainOutputPort() {
        return mainOutputPort;
    }



    private int octave = 3;


    /**
     * Constructs a PC-Keyboard controlled FM-Synth.
     */
    public FMSynth() {
        VCA_LEVEL = new ControlKnob(ControlKnob.NON_NEGATIVE);

        VCA_ENV_ATTACK = new ControlKnob(ControlKnob.NON_NEGATIVE);
        VCA_ENV_DECAY = new ControlKnob(ControlKnob.NON_NEGATIVE);
        VCA_ENV_SUSTAIN = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        VCA_ENV_RELEASE = new ControlKnob(ControlKnob.NON_NEGATIVE);

        OP1_LEVEL = new ControlKnob(ControlKnob.NON_NEGATIVE);
        OP1_FREQ_RATIO = new ControlKnob(ControlKnob.NON_NEGATIVE);

        OP2_LEVEL = new ControlKnob(ControlKnob.NON_NEGATIVE);
        OP2_FREQ_RATIO = new ControlKnob(ControlKnob.NON_NEGATIVE);

        OP3_LEVEL = new ControlKnob(ControlKnob.NON_NEGATIVE);
        OP3_FREQ_RATIO = new ControlKnob(ControlKnob.NON_NEGATIVE);

        Mixer mixer = new Mixer();


        for (Map.Entry<Character, FMSynth.KeyPort> e : keyToKeyPortMap.entrySet()) {
            ADSREnvelope adsrEnvelope = new ADSREnvelope(VCA_ENV_ATTACK::getValue, VCA_ENV_DECAY::getValue, VCA_ENV_SUSTAIN::getValue, VCA_ENV_RELEASE::getValue);

            Oscillator op1 = new SineOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()) * OP1_FREQ_RATIO.getValue(),   OP1_LEVEL::getValue, SampleRate._44100);
            Oscillator op2 = new SineOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()) * op1.getOutputPort().out() * OP2_FREQ_RATIO.getValue(),                            OP2_LEVEL::getValue, SampleRate._44100);
            Oscillator op3 = new SineOscillator(() -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey()) * op2.getOutputPort().out() * OP3_FREQ_RATIO.getValue(),                            OP3_LEVEL::getValue, SampleRate._44100);

            e.setValue(new FMSynth.KeyPort());

            adsrEnvelope.setMainInputPort(op2.getOutputPort());
            adsrEnvelope.setTriggerInputPort(e.getValue().keyPressedOutputPort);

            mixer.addInputPort(adsrEnvelope.getMainOutputPort());
        }

        mainOutputPort = mixer.getMainOutputPort();

        JFrame frame = new JFrame();
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
                    octave = e.getKeyCode() - 48;
                } else {
                    if (keyToFreqMap.containsKey(e.getKeyChar())) {
                        keyToKeyPortMap.get(e.getKeyChar()).keyPressed = 1;
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                if (keyToFreqMap.containsKey(e.getKeyChar())) {
                    keyToKeyPortMap.get(e.getKeyChar()).keyPressed = 0;
                }
            }
        });

    }


    /**
     * Helper-class to keep track of keypress events.
     */
    private static class KeyPort {
        private int keyPressed;

        @OutputPort
        private final Port keyPressedOutputPort = () -> keyPressed;
    }


    /**
     * An example of how the <code>PCKeyboardModule</code> could be used.
     */
    public static void main(String[] args) {

        FMSynth module = new FMSynth();

        // Setting the amplitude or loudness of the synth
        module.VCA_LEVEL.setValue(127);

        // Setting the attributes of the envelope
        module.VCA_ENV_ATTACK.setValue(20);
        module.VCA_ENV_DECAY.setValue(1000);
        module.VCA_ENV_SUSTAIN.setValue(0.7);
        module.VCA_ENV_RELEASE.setValue(120000); // Bell: 120000

        // Setting the attributes of the operators
        module.OP1_LEVEL.setValue(1);
        module.OP1_FREQ_RATIO.setValue(1);

        module.OP2_LEVEL.setValue(127);
        module.OP2_FREQ_RATIO.setValue(0.7142857142857143); // Bell: 0.7142857142857143

        module.OP3_LEVEL.setValue(127);
        module.OP3_FREQ_RATIO.setValue(11);

        // Creating an AudioPlayer with the synth's audio output
        AudioPlayer audioPlayer = new AudioPlayer(module.getMainOutputPort());

        // Initializing and starting the AudioPlayer
        audioPlayer.init();
        audioPlayer.start();

        try {
            Thread.sleep(130000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stopping the AudioPlayer
        audioPlayer.stop();

        System.exit(0);
    }

}