package midi;

import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.TriangleOscillator;
import audio.enums.SampleRate;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;
import io.AudioPlayer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;

public class MidiKeyboard{

    private static final double[] frequencyArray =
            {16.35, 17.32, 18.35,
             19.45, 20.60, 21.83,
             23.12, 24.50, 25.96,
             27.50, 29.14, 30.87};


    private int octave;
    private int frequencyIndex;

    private int keyPressed;


    @OutputPort
    private final Port outputPort = () -> keyPressed * (Math.pow(2, octave) * frequencyArray[frequencyIndex]);


    public Port getOutputPort() {
        return outputPort;
    }


    @OutputPort
    private final Port keyPressedOutputPort = () -> keyPressed;

    public Port getKeyPressedOutputPort() {
        return keyPressedOutputPort;
    }


    MidiDevice midiDevice;

    public MidiKeyboard() {
        midiDevice = MidiHandler.openMidiDevice(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {
                byte[] data = message.getMessage();
                if( data.length > 0 ){
                    switch ((data[0] & 0xFF) & 0xF0) {
                        case ShortMessage.NOTE_ON -> {
                            System.out.println(Arrays.toString(data));
                            keyPressed = 1;
                            octave = data[1] / 12;
                            frequencyIndex = data[1] % 12;
                        }
                        case ShortMessage.NOTE_OFF -> keyPressed = 0;
                    }
                }
            }

            @Override
            public void close() {}

        }, "loopMIDI Port");
    }


    public void close() {
        midiDevice.close();
    }




    public static void main(String[] args) {
        MidiKeyboard midiKeyboard = new MidiKeyboard();

        ADSREnvelope adsrEnvelope = new ADSREnvelope(5000, 5000, 0.7, 10000);

        adsrEnvelope.setTriggerInputPort(midiKeyboard.getKeyPressedOutputPort());

        Oscillator oscillator = new TriangleOscillator(midiKeyboard.getOutputPort(), adsrEnvelope.getMainOutputPort(), SampleRate._44100);

        AudioPlayer audioPlayer = new AudioPlayer(oscillator.getMainOutputPort());

        audioPlayer.init();
        audioPlayer.start();

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        audioPlayer.stop();
        midiKeyboard.close();

    }


}
