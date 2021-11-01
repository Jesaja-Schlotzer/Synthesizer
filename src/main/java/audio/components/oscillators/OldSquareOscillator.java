package audio.components.oscillators;

import audio.components.Generator;

public class OldSquareOscillator extends OldOscillator {

    private double step;
    private double threshold = 0.5;

    public OldSquareOscillator(double frequency, double amplitude, double phase, double sampleRate) {
        super(frequency, amplitude, phase, sampleRate);

        step = (2 * Math.PI * frequency) / sampleRate;
    }

    public OldSquareOscillator(double frequency, double amplitude, double phase, double sampleRate, double threshold) {
        super(frequency, amplitude, phase, sampleRate);

        this.threshold = threshold;

        step = (2 * Math.PI * frequency) / sampleRate;
    }



    @Override
    public double next() {
        double value = Math.sin(t + phase);
        t += step;
        if(value < threshold) {
            return -amplitude;
        }else {
            return amplitude;
        }
    }


    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OldSquareOscillator osc) {
            return osc.frequency == this.frequency &&
                    osc.amplitude == this.amplitude &&
                    osc.phase == this.phase &&
                    osc.sampleRate == this.sampleRate;
        }
        return false;
    }

    @Override
    public Generator clone() {
        return new OldSquareOscillator(frequency, amplitude, phase, sampleRate, threshold);
    }

    @Override
    public String toString() {
        return "Oscillator[Type=Square, Frequency="+ frequency
                +", Amplitude="+ amplitude +", Phase="+ phase
                +", SampleRate="+ sampleRate +", Threshold="+threshold+"]";
    }
}
