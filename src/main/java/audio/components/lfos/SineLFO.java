package audio.components.lfos;

import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;
import audio.components.oscillators.SquareOscillator;
import audio.enums.SampleRate;
import audio.modules.io.Port;
import io.AudioPlayer;

public class SineLFO extends LFO{

    public SineLFO(double frequency, SampleRate sampleRate) {
        super(new SineOscillator(frequency, 0.5, sampleRate));
    }

    public SineLFO(Port frequencyInputPort, SampleRate sampleRate) {
        super(new SineOscillator(frequencyInputPort, () -> 0.5, sampleRate));
    }


    public static void main(String[] args) throws InterruptedException {
        SquareOscillator osc = new SquareOscillator(440, 2, SampleRate._44100);

        SineLFO lfo = new SineLFO(2, SampleRate._44100);

        osc.setPulseWidthInputPort(() -> lfo.getOutputPort().out() * 0.2 + 0.3);


        AudioPlayer player = new AudioPlayer(osc.getOutputPort());

        player.init();

        player.start();

        Thread.sleep(2000);

        player.stop();

    }
}
