package midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;

public class MidiKeyboard implements Receiver {

    MidiDevice midiDevice;

    public MidiKeyboard() {
        midiDevice = MidiHandler.openMidiDevice(this, "loopMIDI Port");


    }




    public void send(MidiMessage msg, long timeStamp) {
        System.out.println(((ShortMessage) msg).getCommand() == ShortMessage.NOTE_ON);
        System.out.println(Arrays.toString(msg.getMessage()));
        System.out.println();
    }


    public void close() {
        midiDevice.close();
    }
}
