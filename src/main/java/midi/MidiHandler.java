package midi;

import javax.sound.midi.*;
import java.util.List;

/**
 * Helper class to handle Midi related tasks.
 */
public class MidiHandler{

    /**
     * Searches for a midi-device with a given name and tries to open it.
     * @param receiver The <code>Receiver</code> who accepts the incoming midi-messages
     * @param searchForDeviceName The name of the midi-device
     * @return the first midi-device that matches the <code>searchForDeviceName</code>, may return null if nothing was found
     */
    public static MidiDevice openMidiDevice(Receiver receiver, String searchForDeviceName) {
        MidiDevice device;

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : infos) {
            try {
                System.out.println(info.toString());
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