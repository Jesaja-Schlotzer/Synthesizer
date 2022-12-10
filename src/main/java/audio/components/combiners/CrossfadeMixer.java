package audio.components.combiners;

import audio.components.lfos.SineLFO;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SawtoothOscillator;
import audio.components.oscillators.SineOscillator;
import audio.components.oscillators.TriangleOscillator;
import audio.enums.SampleRate;
import audio.modules.io.InputPort;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;
import io.AudioPlayer;


/**
 * This mixer crossfades the two incoming audio signals to one output signal.
 */
public class CrossfadeMixer {

    @InputPort
    private final Port inputPort1;

    @InputPort
    private final Port inputPort2;


    @InputPort
    private final Port crossfadePort;


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
     * @param inputPort1 The first <code>InputPort</code>
     * @param inputPort2 The second <code>InputPort</code>
     * @param crossfadePort The <code>InputPort</code> that controls the crossfade.
     *                      At 0 the second <code>InputPort</code> is completely silent,
     *                      at 1 the first <code>InputPort</code> is completely silent.
     *                      The value is clipped between 0 and 1.
     */
    public CrossfadeMixer(Port inputPort1, Port inputPort2, Port crossfadePort) {
        this.inputPort1 = inputPort1;
        this.inputPort2 = inputPort2;
        this.crossfadePort = () -> Math.min(Math.max(crossfadePort.out(), 0), 1);
    }


    /**
     * Mixes the incoming signals by first summing them up and then dividing the sum by the number of <code>InputPort</code>s.
     * @return Returns the mixed audio signal
     */
    private double mix() {
        double crossfade = crossfadePort.out();
        return ((1-crossfade) * inputPort1.out()) + (crossfade * inputPort2.out());
    }


    public static void main(String[] args) throws InterruptedException {
        Oscillator sineOsc = new TriangleOscillator(440, 2, SampleRate._44100);

        Oscillator sawOsc = new SawtoothOscillator(880, 2, SampleRate._44100);

        SineLFO crossfadeOsc = new SineLFO(0.125, SampleRate._44100);

        CrossfadeMixer mixer = new CrossfadeMixer(sineOsc.getOutputPort(), sawOsc.getOutputPort(), crossfadeOsc.getOutputPort());

        AudioPlayer player = new AudioPlayer(mixer.getOutputPort());

        player.init();

        player.start();

        Thread.sleep(2000);

        player.stop();
    }
}
