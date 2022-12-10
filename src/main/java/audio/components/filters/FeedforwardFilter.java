package audio.components.filters;

import audio.components.combiners.CrossfadeMixer;
import audio.components.lfos.SineLFO;
import audio.components.oscillators.SawtoothOscillator;
import audio.components.oscillators.SineOscillator;
import audio.enums.SampleRate;
import io.AudioPlayer;

public class FeedforwardFilter extends Filter {

    private final double a0;
    private final double a1;

    private double lastSample = 0;


    public FeedforwardFilter(double a0, double a1) {
        this.a0 = a0;
        this.a1 = a1;
    }


    @Override
    protected double filter() {
        double sample = inputPort.out();
        double filteredSample = (a0 * sample) + (a1 * lastSample);
        this.lastSample = sample;
        return filteredSample;
    }

    @Override
    public Filter clone() {
        return new FeedforwardFilter(a0, a1);
    }


    public static void main(String[] args) throws InterruptedException {
        SawtoothOscillator osc = new SawtoothOscillator(1440, 4, SampleRate._44100);

        SawtoothOscillator osc2 = new SawtoothOscillator(1440, 4, SampleRate._44100);

        FeedforwardFilter filter = new FeedforwardFilter(0.5, 0.5);

        filter.setInputPort(osc.getOutputPort());


        SineLFO sineLfo = new SineLFO(0.5, SampleRate._44100);

        CrossfadeMixer mixer = new CrossfadeMixer(osc2.getOutputPort(), filter.getOutputPort(), sineLfo.getOutputPort());


        AudioPlayer player = new AudioPlayer(mixer.getOutputPort());

        player.init();
        player.start();

        Thread.sleep(2000);

        player.stop();
    }
}
