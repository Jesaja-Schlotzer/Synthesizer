package audio.components.filters;

import audio.enums.SampleRate;

/**
 * Not properly working at the moment.
 */
public class RCLowPassFilter extends Filter{

    private final double[] coeff = new double[2];
    private final double[] out = new double[2];


    public RCLowPassFilter(double cutOffFrequency, SampleRate sampleRate) {
        double RC = 1.0 / (2 * Math.PI * (cutOffFrequency == 0 ? 0.0000001 : cutOffFrequency));

        coeff[0] = sampleRate.get() / (sampleRate.get() + RC);
        coeff[1] = RC / (sampleRate.get() + RC);
    }

    private RCLowPassFilter(double[] coeff) {
        this.coeff[0] = coeff[0];
        this.coeff[1] = coeff[1];
    }



    @Override
    protected double filter() {
        out[1] = out[0];
        out[0] = coeff[0] * inputPort.out() + coeff[1] * out[1];
        return out[0];
    }



    public void adjustFilter(double cutOffFrequency, SampleRate sampleRate) {
        double RC = 1.0 / (2 * Math.PI * (cutOffFrequency == 0 ? Double.MIN_VALUE : cutOffFrequency));

        coeff[0] = sampleRate.get() / (sampleRate.get() + RC);
        coeff[1] = RC / (sampleRate.get() + RC);

        out[0] = 0;
    }



    @Override
    public Filter clone() {
        return new RCLowPassFilter(this.coeff);
    }
}
