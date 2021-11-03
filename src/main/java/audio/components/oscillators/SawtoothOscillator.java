package audio.components.oscillators;

import audio.components.Generator;
import audio.enums.SampleRate;
import audio.modules.io.Port;

public class SawtoothOscillator extends Oscillator {


    public SawtoothOscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }


    public SawtoothOscillator(double frequency, double amplitude, SampleRate sampleRate) {
        super(frequency, amplitude, sampleRate);
    }



    @Override
    protected double next() {
        double div = t / (sampleRate / frequencyInputPort.out());
        t++;
        return 2 * (div - Math.floor(0.5 + div)) * amplitudeInputPort.out();
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
    public String toString() {
        return "Oscillator[Type=Sawtooth, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +"]";
    }
}
