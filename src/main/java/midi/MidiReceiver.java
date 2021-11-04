package midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;

public class MidiReceiver implements Receiver {



    public MidiReceiver(){

    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        parseMessage(message);
    }


    @Override
    public void close(){
        //not implemented
    }


    private void parseMessage(MidiMessage message){
        byte[] data = message.getMessage();
        if( data.length > 0 ){
            switch ((data[0] & 0xFF) & 0xF0) {
                case ShortMessage.NOTE_ON:
                    System.out.println(Arrays.toString(message.getMessage()));
                    System.out.println(data[1] / 12 + " " + data[1] % 12);
                    //parseNoteOn(data, findChannel(message), findVoice(message), findBendMode(message));
                    break;
                case ShortMessage.NOTE_OFF:
                    System.out.println(Arrays.toString(message.getMessage()));
                    System.out.println("--");
                    System.out.println();
                    //parseNoteOff(data, findChannel(message), findVoice(message), findBendMode(message));

            }
        }
    }



    private void parseNoteOn(byte[] data, int channel, int voice, boolean bendMode){
        int length = data.length;
        int value = (length > 1)?(data[1] & 0xFF):0;
        int velocity = (length > 2)?(data[2] & 0xFF):0;

        if( channel >= 0 ){
            if( velocity == 0 ){
                this.parseNoteOff(data, channel, voice, bendMode);
            }else if(value > 0){
                //this.sequencer.getTransmitter().sendNoteOn(channel,value,velocity,voice,bendMode);
            }
        }
    }

    private void parseNoteOff(byte[] data, int channel, int voice, boolean bendMode){
        int length = data.length;
        int value = (length > 1)?(data[1] & 0xFF):0;
        int velocity = (length > 2)?(data[2] & 0xFF):0;
        if( channel >= 0 ){
            //this.sequencer.getTransmitter().sendNoteOff(channel,value,velocity,voice,bendMode);
        }
    }



    private int findChannel(MidiMessage midiMessage){
        if( midiMessage instanceof ShortMessage ){
            return ((ShortMessage)midiMessage).getChannel();
        }
        byte[] data = midiMessage.getMessage();
        if( data != null && data.length > 0){
            return ((data[0] & 0xFF) & 0x0F);
        }
        return -1;
    }

    /*
    private int findVoice(MidiMessage midiMessage){
        if( midiMessage instanceof MidiShortMessage ){
            return ((MidiShortMessage)midiMessage).getVoice();
        }
        return MidiShortMessage.DEFAULT_VOICE;
    }

    private boolean findBendMode(MidiMessage midiMessage){
        if( midiMessage instanceof MidiShortMessage ){
            return ((MidiShortMessage)midiMessage).isBendMode();
        }
        return MidiShortMessage.DEFAULT_BEND_MODE;
    }

    */
}
