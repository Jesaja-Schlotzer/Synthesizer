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


/**
 * A simple synthesizer, controllable via the PC Keyboard.<br><br>
 *
 * Y to M triggers the notes of the major scale and S, D, G, H, J triggers C#, D#, F#, G#, A#.<br>
 * 0 to 9 can be used to change the octave of the Synth.
 */
public class PCKeyboardModulateableModule {

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


    private final Map<Character, KeyPort> keyToKeyPortMap = new HashMap<>(){{
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


    public final ControlKnob amplitudeKnob;

    public final ControlKnob attackKnob;
    public final ControlKnob decayKnob;
    public final ControlKnob sustainKnob;
    public final ControlKnob releaseKnob;



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
     * Constructs a PC-Keyboard controlled Synth.
     *
     * @param waveForm The waveform of the synth to be used
     */
    public PCKeyboardModulateableModule(WaveForm waveForm) {
        amplitudeKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        Mixer mixer = new Mixer();

        for (Map.Entry<Character, KeyPort> e : keyToKeyPortMap.entrySet()) {
            ADSREnvelope adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);

            Oscillator oscillator = null;

            Port freqInputPort = () -> Math.pow(2, octave) * keyToFreqMap.get(e.getKey());

            Oscillator freqModOsc = new SineOscillator(13, 7, SampleRate._44100);
            Oscillator amplModOsc = new TriangleOscillator(3, 8, SampleRate._44100);

            Mixer freqModMixer = new Mixer(freqInputPort, freqModOsc.getOutputPort());
            Mixer amplModMixer = new Mixer(amplitudeKnob::getValue, amplModOsc.getOutputPort());

            switch (waveForm) {
                case SINE -> oscillator = new SineOscillator(freqModMixer.getMainOutputPort(), amplModMixer.getMainOutputPort(), SampleRate._44100);
                case SQUARE ->  oscillator = new SquareOscillator(freqModMixer.getMainOutputPort(), amplModMixer.getMainOutputPort(), SampleRate._44100);
                case SAWTOOTH ->  oscillator = new SawtoothOscillator(freqModMixer.getMainOutputPort(), amplModMixer.getMainOutputPort(), SampleRate._44100);
                case TRIANGLE ->  oscillator = new TriangleOscillator(freqModMixer.getMainOutputPort(), amplModMixer.getMainOutputPort(), SampleRate._44100);
            }

            e.setValue(new KeyPort());

            adsrEnvelope.setMainInputPort(oscillator.getOutputPort());
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

        PCKeyboardModulateableModule module = new PCKeyboardModulateableModule(WaveForm.SQUARE);

        // Setting the amplitude or loudness of the synth
        module.amplitudeKnob.setValue(64);

        // Setting the attributes of the envelope
        module.attackKnob.setValue(2000);
        module.decayKnob.setValue(300);
        module.sustainKnob.setValue(0.8);
        module.releaseKnob.setValue(15000);

        // Creating an AudioPlayer with the synth's audio output
        AudioPlayer audioPlayer = new AudioPlayer(module.getMainOutputPort());

        // Initializing and starting the AudioPlayer
        audioPlayer.init();
        audioPlayer.start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stopping the AudioPlayer
        audioPlayer.stop();

        System.exit(0);
    }

}
