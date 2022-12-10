package audio.components.combiners;

import audio.components.Generator;
import audio.components.oscillators.SawtoothOscillator;
import audio.enums.SampleRate;
import audio.modules.io.*;
import io.AudioPlayer;

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
    private final Port outputPort = this::mix;

    /**
     * Returns the output port where the mixed audio signal will be sent.
     * @return the main output port
     */
    public Port getOutputPort() {
        return outputPort;
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


    public static void main(String[] args) throws InterruptedException {
        SawtoothOscillator[] oscList = new SawtoothOscillator[4];

        for (int i = 0; i < oscList.length; i++) {
            oscList[i] = new SawtoothOscillator(440+Math.random()*10, 2, SampleRate._44100);

            for (int j = 0; j < (int) (Math.random()*10000); j++) {
                oscList[i].getOutputPort().out();
            }
        }

        Mixer mixer = new Mixer(Arrays.stream(oscList).map(Generator::getOutputPort).toList().toArray(Port[]::new));


        AudioPlayer player = new AudioPlayer(mixer.getOutputPort());

        player.init();

        player.start();

        Thread.sleep(2000);

        player.stop();
    }
}
