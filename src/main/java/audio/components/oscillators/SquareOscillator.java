package audio.components.oscillators;

import audio.components.Generator;
import audio.enums.SampleRate;
import audio.modules.io.InputPort;
import audio.modules.io.Port;

public class SquareOscillator extends Oscillator {

    @InputPort
    private Port pulseWidthInputPort = Port.NULL;

    public void setPulseWidthInputPort(Port pulseWidthInputPort) {
        if (pulseWidthInputPort != null) {
            this.pulseWidthInputPort = () -> Math.min(Math.max(pulseWidthInputPort.out(), -1), 1);
        }
    }



    public SquareOscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
    }


    public SquareOscillator(double frequency, double amplitude, SampleRate sampleRate) {
        super(frequency, amplitude, sampleRate);
    }


    public SquareOscillator(Port frequencyInputPort, Port amplitudeInputPort, SampleRate sampleRate, Port pulseWidthInputPort) {
        super(frequencyInputPort, amplitudeInputPort, sampleRate);
        setPulseWidthInputPort(pulseWidthInputPort);
    }


    public SquareOscillator(double frequency, double amplitude, SampleRate sampleRate, double pulseWidth) {
        super(frequency, amplitude, sampleRate);
        this.pulseWidthInputPort = (pulseWidth >= -1 && pulseWidth <= 1 ? () -> pulseWidth : Port.NULL);
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
    public String toString() {
        return "Oscillator[Type=Square, Frequency="+ frequencyInputPort.out()
                +", Amplitude="+ amplitudeInputPort.out()
                +", SampleRate="+ sampleRate +", Threshold="+pulseWidthInputPort.out()+"]";
    }
}
