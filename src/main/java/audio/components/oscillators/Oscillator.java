package audio.components.oscillators;

import audio.components.Generator;
import audio.enums.SampleRate;
import audio.modules.io.InputPort;
import audio.modules.io.Port;

/**
 * An abstract oscillator class, providing <code>InputPort</code>s to control the current frequency and amplitude
 * and an <code>OutputPort</code> to supply the generated audio signal.
 */
public abstract class Oscillator extends Generator {

    @InputPort
    protected Port frequencyInputPort = Port.NULL;

    /**
     * Sets the <code>frequencyInputPort</code> which controls the frequency of the <code>Oscillator</code>.
     * @param frequencyInputPort the <code>Port</code> to be set
     */
    public void setFrequencyInputPort(Port frequencyInputPort) {
        if(frequencyInputPort != null) {
            this.frequencyInputPort = () -> Math.max(frequencyInputPort.out(), 0);
        }
    }


    @InputPort
    protected Port amplitudeInputPort = Port.NULL;

    /**
     * Sets the <code>amplitudeInputPort</code> which controls the amplitude of the <code>Oscillator</code>.
     * @param amplitudeInputPort the <code>Port</code> to be set
     */
    public void setAmplitudeInputPort(Port amplitudeInputPort) {
        if(amplitudeInputPort != null) {
            this.amplitudeInputPort = () -> Math.max(amplitudeInputPort.out(), 0);
        }
    }



    protected double sampleRate;
    protected double t = 0;

    /**
     * Constructs an <code>Oscillator</code> and sets the <code>InputPort</code>s and the sample rate of the <code>Oscillator</code>.
     * @param frequencyInputPort the <code>frequencyInputPort</code> of the <code>Oscillator</code>
     * @param amplitudeInputPort the <code>amplitudeInputPort</code> of the <code>Oscillator</code>
     * @param sampleRate the <code>sampleRate</code> of the <code>Oscillator</code>
     */
    public Oscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate) {
        setFrequencyInputPort(frequencyInputPort);
        setAmplitudeInputPort(amplitudeInputPort);
        this.sampleRate = sampleRate.get();
    }

    /**
     * Constructs an <code>Oscillator</code> and sets a constant frequency and amplitude and the sample rate of the <code>Oscillator</code>.
     * @param frequency the frequency of the <code>Oscillator</code>
     * @param amplitude the amplitude of the <code>Oscillator</code>
     * @param sampleRate the <code>sampleRate</code> of the <code>Oscillator</code>
     */
    public Oscillator(double frequency, double amplitude, SampleRate sampleRate) {
        frequencyInputPort = (frequency >= 0 ? () -> frequency : Port.NULL);
        amplitudeInputPort = (amplitude >= 0 ? () -> amplitude : Port.NULL);
        this.sampleRate = sampleRate.get();
    }
}
