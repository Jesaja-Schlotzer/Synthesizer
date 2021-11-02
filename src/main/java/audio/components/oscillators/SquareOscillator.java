package audio.components.oscillators;

import audio.components.Generator;
import audio.modules.io.InputPort;
import audio.modules.io.Port;

public class SquareOscillator extends Oscillator {

    @InputPort
    private Port pulseWidthInputPort = () -> 0.5;

    public void setPulseWidthInputPort(Port pulseWidthInputPort) {
        this.pulseWidthInputPort = pulseWidthInputPort;
    }


    public SquareOscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }

    public SquareOscillator(double frequency, double amplitude, double sampleRate) {
        super(frequency, amplitude, sampleRate);
    }

    public SquareOscillator(Port frequencyInputPort, Port amplitudeInputPort, double sampleRate, Port pulseWidthInputPort) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);

        this.pulseWidthInputPort = pulseWidthInputPort;
    }

    public SquareOscillator(double frequency, double amplitude, double sampleRate, double pulseWidth) {
        super(frequency, amplitude, sampleRate);

        this.pulseWidthInputPort = () -> pulseWidth;
    }



    @Override
    protected double next() {
        if(Math.sin(t) < pulseWidthInputPort.out()) {
            t += (2 * Math.PI * frequencyInputPort.out()) / sampleRate;
            return -amplitudeInputPort.out();
        }else {
            t += (2 * Math.PI * frequencyInputPort.out()) / sampleRate;
            return amplitudeInputPort.out();
        }
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SquareOscillator osc) {
            return osc.frequencyInputPort == this.frequencyInputPort &&
                    osc.amplitudeInputPort == this.amplitudeInputPort &&
                    osc.sampleRate == this.sampleRate &&
                    osc.pulseWidthInputPort == this.pulseWidthInputPort;
        }
        return false;
    }


    @Override
    public Generator clone() {
        return new SquareOscillator(frequencyInputPort, amplitudeInputPort, sampleRate, pulseWidthInputPort);
    }


    @Override
    public String toString() {
        return "Oscillator[Type=Square, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +", Threshold="+pulseWidthInputPort.out()+"]";
    }
}
