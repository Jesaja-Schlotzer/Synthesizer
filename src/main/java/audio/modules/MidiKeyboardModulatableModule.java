package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.modulators.envelopes.Envelope;
import audio.components.oscillators.*;
import audio.enums.SampleRate;
import audio.enums.WaveForm;
import audio.modules.controls.ControlKnob;
import audio.modules.controls.ModulatableControlKnob;
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
public class MidiKeyboardModulatableModule {

    private static final double[] frequencyArray =
            {16.35, 17.32, 18.35,
             19.45, 20.60, 21.83,
             23.12, 24.50, 25.96,
             27.50, 29.14, 30.87};


    /* This is only the mapping for my keyboard, must be adjusted manually at the moment */
    private final Map<Byte, Byte> midiKnobMap = new HashMap<>(){{
        put((byte) 112, (byte) 0); // 1 (Funktioniert vielleicht nicht)
        put((byte) 74,  (byte) 0); // 2
        put((byte) 71,  (byte) 0); // 3
        put((byte) 76,  (byte) 0); // 4
        put((byte) 77,  (byte) 0); // 5
        put((byte) 93,  (byte) 0); // 6
        put((byte) 73,  (byte) 0); // 7
        put((byte) 75,  (byte) 0); // 8
        put((byte) 114, (byte) 0); // 9
        put((byte) 18,  (byte) 0); // 10
        put((byte) 19,  (byte) 0); // 11
        put((byte) 16,  (byte) 0); // 12
        put((byte) 17,  (byte) 0); // 13
        put((byte) 91,  (byte) 0); // 14
        put((byte) 79,  (byte) 0); // 15
        put((byte) 72,  (byte) 0); // 16
    }};


    private final LinkedList<Oscillator> oscillators = new LinkedList<>();
    private final Map<Oscillator, Envelope> oscEnvMap = new HashMap<>();

    private final Map<Integer, Oscillator> handleMap = new HashMap<>();

    public final ModulatableControlKnob amplitudeKnob;

    public final ModulatableControlKnob attackKnob;
    public final ModulatableControlKnob decayKnob;
    public final ModulatableControlKnob sustainKnob;
    public final ModulatableControlKnob releaseKnob;

    @OutputPort
    private final Port outputPort;

    /**
     * @return the <code>Port</code> that supplies the output audio of the synth
     */
    public Port getOutputPort() {
        return outputPort;
    }


    MidiDevice midiDevice;


    /**
     * Constructs a midi controlled synth.
     *
     * @param midiDeviceName The name of the midi device to be used
     * @param waveForm The waveform of the synth to be used
     * @param voices The amount of voices of the synth or how many notes can be played simultaneously
     */
    public MidiKeyboardModulatableModule(String midiDeviceName, WaveForm waveForm, int voices) {
        amplitudeKnob = new ModulatableControlKnob(() -> midiKnobMap.get((byte) 18), ControlKnob.NON_NEGATIVE);

        attackKnob = new ModulatableControlKnob(() -> midiKnobMap.get((byte) 74), ControlKnob.NON_NEGATIVE);
        decayKnob = new ModulatableControlKnob(() -> midiKnobMap.get((byte) 71), ControlKnob.NON_NEGATIVE);
        sustainKnob = new ModulatableControlKnob(() -> midiKnobMap.get((byte) 76), ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ModulatableControlKnob(() -> midiKnobMap.get((byte) 77), ControlKnob.NON_NEGATIVE);

        Mixer mixer = new Mixer();

        voices = Math.max(1, voices);

        for (int i = 0; i < voices; i++) {
            ADSREnvelope adsrEnvelope = new ADSREnvelope(attackKnob.getOutputPort(), decayKnob.getOutputPort(), sustainKnob.getOutputPort(), releaseKnob.getOutputPort());
            adsrEnvelope.setMainInputPort(amplitudeKnob.getOutputPort());

            Oscillator osc = null;

            switch (waveForm) {
                case SINE -> osc = new SineOscillator(Port.NULL, adsrEnvelope.getOutputPort(), SampleRate._44100);
                case SQUARE -> osc = new SquareOscillator(Port.NULL, adsrEnvelope.getOutputPort(), SampleRate._44100);
                case SAWTOOTH -> osc = new SawtoothOscillator(Port.NULL, adsrEnvelope.getOutputPort(), SampleRate._44100);
                case TRIANGLE -> osc = new TriangleOscillator(Port.NULL, adsrEnvelope.getOutputPort(), SampleRate._44100);
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

                        case ShortMessage.CONTROL_CHANGE -> {
                            midiKnobMap.put(data[1], data[2]);
                            System.out.println(data[1] + " " + data[2]);
                        }
                    }
                }
            }

            @Override
            public void close() {}

        }, midiDeviceName);


        this.outputPort = mixer.getOutputPort();

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
        MidiKeyboardModulatableModule module = new MidiKeyboardModulatableModule("Arturia MiniLab mkII", WaveForm.TRIANGLE, 8);

        // Setting the amplitude or loudness of the synth
        module.amplitudeKnob.setKnobRotation(1);

        // Setting the attributes of the envelope
        module.attackKnob.setKnobRotation(1700);
        module.decayKnob.setKnobRotation(700);
        module.sustainKnob.setKnobRotation(0.007874);
        module.releaseKnob.setKnobRotation(1700);

        // Creating an AudioPlayer with the synth's audio output
        AudioPlayer audioPlayer = new AudioPlayer(module.getOutputPort());

        // Initializing and starting the AudioPlayer
        audioPlayer.init();
        audioPlayer.start();

        try {
            Thread.sleep(300000);
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
