package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.modulators.envelopes.Envelope;
import audio.components.oscillators.*;
import audio.enums.SampleRate;
import audio.enums.WaveForm;
import audio.modules.controls.ControlKnob;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;
import io.AudioPlayer;
import midi.MidiHandler;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A simple synthesizer, controllable via a Midi Keyboard.<br><br>
 */
public class MidiKeyboardModule {

    private static final double[] frequencyArray =
            {16.35, 17.32, 18.35,
             19.45, 20.60, 21.83,
             23.12, 24.50, 25.96,
             27.50, 29.14, 30.87};


    private final LinkedList<Oscillator> oscillators = new LinkedList<>();
    private final Map<Oscillator, Envelope> oscEnvMap = new HashMap<>();

    private final Map<Integer, Oscillator> handleMap = new HashMap<>();

    public final ControlKnob amplitudeKnob;

    public final ControlKnob attackKnob;
    public final ControlKnob decayKnob;
    public final ControlKnob sustainKnob;
    public final ControlKnob releaseKnob;

    @OutputPort
    private final  Port mainOutputPort;

    /**
     * @return the <code>Port</code> that supplies the output audio of the synth
     */
    public Port getMainOutputPort() {
        return mainOutputPort;
    }


    MidiDevice midiDevice;


    /**
     * Constructs a midi controlled synth.
     *
     * @param midiDeviceName The name of the midi device to be used
     * @param waveForm The waveform of the synth to be used
     * @param voices The amount of voices of the synth or how many notes can be played simultaneously
     */
    public MidiKeyboardModule(String midiDeviceName, WaveForm waveForm, int voices) {
        amplitudeKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        Mixer mixer = new Mixer();

        voices = Math.max(1, voices);

        for (int i = 0; i < voices; i++) {
            ADSREnvelope adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);
            adsrEnvelope.setMainInputPort(amplitudeKnob::getValue);

            Oscillator osc = null;

            switch (waveForm) {
                case SINE -> osc = new SineOscillator(Port.NULL, adsrEnvelope.getMainOutputPort(), SampleRate._44100);
                case SQUARE -> osc = new SquareOscillator(Port.NULL, adsrEnvelope.getMainOutputPort(), SampleRate._44100);
                case SAWTOOTH -> osc = new SawtoothOscillator(Port.NULL, adsrEnvelope.getMainOutputPort(), SampleRate._44100);
                case TRIANGLE -> osc = new TriangleOscillator(Port.NULL, adsrEnvelope.getMainOutputPort(), SampleRate._44100);
            }

            mixer.addInputPort(osc.getOutputPort());
            oscillators.add(osc);
            oscEnvMap.put(osc, adsrEnvelope);
        }


        midiDevice = MidiHandler.openMidiDevice(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {
                byte[] data = message.getMessage();
                if(data.length > 0){
                    switch ((data[0] & 0xFF) & 0xF0) {

                        case ShortMessage.NOTE_ON -> {
                            System.out.println("tap\n" + System.currentTimeMillis());
                            Oscillator osc = oscillators.pop();
                            osc.setFrequencyInputPort(() -> Math.pow(2, data[1] / 12.0) * frequencyArray[data[1] % 12]);
                            oscEnvMap.get(osc).setTriggerInputPort(() -> 1);
                            handleMap.put((int) data[1], osc);
                            oscillators.addLast(osc);
                        }

                        case ShortMessage.NOTE_OFF -> {
                            Oscillator handle = handleMap.get((int) data[1]);
                            if(handle != null) {
                                oscEnvMap.get(handle).setTriggerInputPort(() -> 0);
                                handleMap.remove((int) data[1]);
                            }
                        }

                    }
                }
            }

            @Override
            public void close() {}

        }, midiDeviceName);


        this.mainOutputPort = mixer.getMainOutputPort();

    }

    /**
     * Closes the connection to the midi device
     */
    public void close() {
        midiDevice.close();
    }




    /**
     * An example of how the <code>MidiKeyboardModule</code> could be used.
     */
    public static void main(String[] args) {
        // Tip:
        // If you don't know how your midi device is called just run this method and look at the console.
        // The "openMidiDevice" method of the "MidiHandler" class prints out all the found midi device names.
        // Just look for the name that sounds like the one of your device.
        MidiKeyboardModule module = new MidiKeyboardModule("Arturia MiniLab mkII", WaveForm.SQUARE, 8);

        // Setting the amplitude or loudness of the synth
        module.amplitudeKnob.setValue(32);

        // Setting the attributes of the envelope
        module.attackKnob.setValue(5000);
        module.decayKnob.setValue(0);
        module.sustainKnob.setValue(0.8);
        module.releaseKnob.setValue(20000);

        // Creating an AudioPlayer with the synth's audio output
        AudioPlayer audioPlayer = new AudioPlayer(module.getMainOutputPort());

        // Initializing and starting the AudioPlayer
        audioPlayer.init();
        audioPlayer.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stopping the AudioPlayer
        audioPlayer.stop();

        // Closing the connection to the midi device
        module.close();

        System.exit(0);
    }


}
