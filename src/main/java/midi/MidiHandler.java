package midi;

import javax.sound.midi.*;
import java.util.List;


public class MidiHandler{

    public static MidiDevice openMidiDevice(Receiver receiver, String searchForDeviceName) {
        MidiDevice device;

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : infos) {
            try {

                if(info.toString().equals(searchForDeviceName)) {
                    device = MidiSystem.getMidiDevice(info);

                    device.open();

                    List<Transmitter> transmitters = device.getTransmitters();

                    for (Transmitter transmitter : transmitters) {
                        transmitter.setReceiver(receiver);
                    }

                    Transmitter trans = device.getTransmitter();
                    trans.setReceiver(receiver);

                    return device;
                }

            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}