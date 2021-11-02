package audio.components.oscillators;

import audio.components.Generator;
import audio.modules.io.Port;

public class TriangleOscillator extends Oscillator {


    public TriangleOscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }

    public TriangleOscillator(double frequency, double amplitude, double sampleRate) {
        super(frequency, amplitude, sampleRate);
    }



    @Override
    protected double next() {
        double div = t / (sampleRate / frequencyInputPort.out());
        double value = 2 * (div - Math.floor(0.5 + div));
        value = (Math.abs(value) - 0.5) * 2;
        t++;
        return value * amplitudeInputPort.out();
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TriangleOscillator osc) {
            return osc.frequencyInputPort == this.frequencyInputPort &&
                    osc.amplitudeInputPort == this.amplitudeInputPort &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }


    @Override
    public Generator clone() {
        return new TriangleOscillator(frequencyInputPort, amplitudeInputPort, sampleRate);
    }


    @Override
    public String toString() {
        return "Oscillator[Type=Triangle, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +"]";
    }
}
