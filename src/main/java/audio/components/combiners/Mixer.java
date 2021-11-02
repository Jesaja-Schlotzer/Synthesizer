package audio.components.combiners;

import audio.modules.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Mixer{

    @InputPort
    private ArrayList<Port> inputPorts;


    @OutputPort
    private final Port mainOutputPort = this::mix;

    public Port getMainOutputPort() {
        return mainOutputPort;
    }


    public Mixer(Port... inputPorts) {
        this.inputPorts = (ArrayList<Port>) Arrays.stream(inputPorts).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public void addInputPort(Port inputPort) {
        if(inputPort != null) {
            inputPorts.add(inputPort);
        }
    }


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
