package audio.components.oscillators;

import audio.enums.SampleRate;
import audio.modules.io.Port;

/**
 * An <code>Oscillator</code> implementation that generates a sawtooth wave.
 */
public class SawtoothOscillator extends Oscillator {

    /**
     * Constructs an <code>SawtoothOscillator</code> and sets the <code>InputPort</code>s and the sample rate of the <code>Oscillator</code>.
     * @param frequencyInputPort the <code>frequencyInputPort</code> of the <code>Oscillator</code>
     * @param amplitudeInputPort the <code>amplitudeInputPort</code> of the <code>Oscillator</code>
     * @param sampleRate the <code>sampleRate</code> of the <code>Oscillator</code>
     */
    public SawtoothOscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }

    /**
     * Constructs an <code>SawtoothOscillator</code> and sets a constant frequency and amplitude and the sample rate of the <code>Oscillator</code>.
     * @param frequency the frequency of the <code>Oscillator</code>
     * @param amplitude the amplitude of the <code>Oscillator</code>
     * @param sampleRate the <code>sampleRate</code> of the <code>Oscillator</code>
     */
    public SawtoothOscillator(double frequency, double amplitude, SampleRate sampleRate) {
        super(frequency, amplitude, sampleRate);
    }


    /**
     * @return the next audio sample generated by the <code>Oscillator</code>
     */
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
