package audio.components.combiners;

import audio.components.Generator;
import audio.modules.Module;
import audio.modules.io.InputPort;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Mixer extends Module{

    @InputPort
    private ArrayList<Port> inputPorts;


    @OutputPort
    private Port mainOutput;

    public Port getMainOutput() {
        return mainOutput;
    }


    public Mixer(Port... ports) {
        this.inputPorts = (ArrayList<Port>) Arrays.stream(ports).collect(Collectors.toList());

        mainOutput = this::mix;
    }


    public void addPort(Port port) {
        inputPorts.add(port);
    }


    public double mix() {
        double sum = 0;

        for (Port port : inputPorts) {
            sum += port.out();
        }

        return sum / inputPorts.size();
    }
}
