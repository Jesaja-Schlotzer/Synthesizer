package audio.components.combiners;

import audio.modules.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A mixer mixes multiple incoming audio signals to one output signal.
 */
public class Mixer{

    @InputPort
    private final ArrayList<Port> inputPorts;


    @OutputPort
    private final Port mainOutputPort = this::mix;

    /**
     * Returns the output port where the mixed audio signal will be sent.
     * @return the main output port
     */
    public Port getMainOutputPort() {
        return mainOutputPort;
    }

    /**
     * Constructs a mixer and adds the given, non-null <code>InputPort</code>s to the mixer.
     * @param inputPorts The <code>InputPort</code>s to add to the mixer
     */
    public Mixer(Port... inputPorts) {
        this.inputPorts = (ArrayList<Port>) Arrays.stream(inputPorts).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Adds another non-null <code>InputPort</code> to the mixer.
     * @param inputPort The <code>InputPort</code> to add to the mixer
     */
    public void addInputPort(Port inputPort) {
        if(inputPort != null) {
            inputPorts.add(inputPort);
        }
    }

    /**
     * Mixes the incoming signals by first summing them up and then dividing the sum by the number of <code>InputPort</code>s.
     * @return Returns the mixed audio signal
     */
    private double mix() {
        double sum = 0;

        for (Port port : inputPorts) {
            sum += port.out();
        }

        if(sum == 0){
            return 0;
        }

        return sum / inputPorts.size();
    }
}
