package audio.components.oscillators;

public class SawtoothOscillator extends Oscillator{

    private double period;

    public SawtoothOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        this.period = sampleRate / frequency;
        this.phase = ((phase + 90)/ 360) * period;
    }


    @Override
    public double next() {
        double div = (t + phase) / period;
        double val = 2 * (div - Math.floor(0.5 + div));
        t++;
        return val * amplitude;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SquareOscillator osc) {
            return osc.frequency == this.frequency &&
                    osc.amplitude == this.amplitude &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Object clone() {
        return new SawtoothOscillator(frequency, amplitude, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Sawtooth, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
