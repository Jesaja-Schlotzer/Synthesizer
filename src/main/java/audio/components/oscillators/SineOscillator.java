package audio.components.oscillators;

public class SineOscillator extends Oscillator{

    protected double step;

    public SineOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        step = (2 * Math.PI * frequency) / sampleRate;
    }


    public void init() {
        i = 0;
    }

    public double next() {
        double value = Math.sin(i + phase);
        i += step;
        return value * amplitude;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SineOscillator) {
            SineOscillator osc = (SineOscillator) obj;
            if(osc.frequency == this.frequency &&
               osc.amplitude == this.amplitude &&
               osc.phase == this.phase &&
               osc.sampleRate == this.sampleRate) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new SineOscillator(frequency, amplitude, phase, sampleRate);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Sine, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +"]";
    }
}
