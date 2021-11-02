package audio.components.oscillators;

import audio.components.Generator;
import audio.modules.io.Port;

public class SawtoothOscillator extends Oscillator {


    public SawtoothOscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }

    public SawtoothOscillator(double frequency, double amplitude, double sampleRate) {
        super(frequency, amplitude, sampleRate);
    }



    @Override
    protected double next() {
        double div = t / (sampleRate / frequencyInputPort.out());
        double val = 2 * (div - Math.floor(0.5 + div));
        t++;
        return val * amplitudeInputPort.out();
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SawtoothOscillator osc) {
            return osc.frequencyInputPort == this.frequencyInputPort &&
                   osc.amplitudeInputPort == this.amplitudeInputPort &&
                   osc.sampleRate == this.sampleRate;
        }
        return false;
    }


    @Override
    public Generator clone(){
        return new SawtoothOscillator(frequencyInputPort, amplitudeInputPort, sampleRate);
    }


    @Override
    public String toString() {
        return "Oscillator[Type=Sawtooth, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +"]";
    }
}
