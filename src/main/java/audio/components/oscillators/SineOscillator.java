package audio.components.oscillators;

import audio.components.Generator;
import audio.modules.io.Port;

public class SineOscillator extends Oscillator {


    public SineOscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }

    public SineOscillator(double frequency, double amplitude, double sampleRate) {
        super(frequency, amplitude, sampleRate);
    }



    protected double next() {
        double value = Math.sin(t);
        t += (2 * Math.PI * frequencyInputPort.out()) / sampleRate;
        return value * amplitudeInputPort.out();
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SineOscillator osc) {
            return osc.frequencyInputPort == this.frequencyInputPort &&
                    osc.amplitudeInputPort == this.amplitudeInputPort &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }


    @Override
    public Generator clone() {
        return new SineOscillator(frequencyInputPort, amplitudeInputPort, sampleRate);
    }


    @Override
    public String toString() {
        return "Oscillator[Type=Sine, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +"]";
    }
}
