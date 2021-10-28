package audio.components.oscillators;

public abstract class Oscillator {

    private double frequency;
    private double amplitude;
    private double phase;

    public Oscillator(double frequency, double amplitude, double phase, double sampleRate) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phase = phase;
    }



    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }


    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }


    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }
}
