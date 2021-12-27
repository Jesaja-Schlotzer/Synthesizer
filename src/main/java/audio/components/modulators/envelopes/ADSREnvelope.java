package audio.components.modulators.envelopes;


import audio.modules.io.InputPort;
import audio.modules.io.Port;

/**
 * An ADSR implementation of the <code>Envelope</code>.
 *
 * The <code>attack-, decay-, sustain-</code> and <code>releaseInputPorts</code>
 * can be used to provide the envelope with constant or variable values.
 */
public class ADSREnvelope extends Envelope{

    @InputPort
    private Port attackInputPort = Port.NULL;

    /**
     * Sets the <code>attackInputPort</code> which controls the attack time of the envelope.
     * @param attackInputPort the <code>Port</code> to be set
     */
    public void setAttackInputPort(Port attackInputPort) {
        if(attackInputPort != null) {
            this.attackInputPort = () -> Math.max(attackInputPort.out(), 0);
        }
    }


    @InputPort
    private Port decayInputPort = Port.ONE;

    /**
     * Sets the <code>decayInputPort</code> which controls the decay time of the envelope.
     * @param decayInputPort the <code>Port</code> to be set
     */
    public void setDecayInputPort(Port decayInputPort) {
        if(decayInputPort != null) {
            this.decayInputPort = () -> Math.max(decayInputPort.out(), 1);
        }
    }


    @InputPort
    private Port sustainInputPort = Port.ONE;

    /**
     * Sets the <code>sustainInputPort</code> which controls the sustain level of the envelope.
     * @param sustainInputPort the <code>Port</code> to be set
     */
    public void setSustainInputPort(Port sustainInputPort) {
        if(sustainInputPort != null) {
            this.sustainInputPort = () -> Math.min(Math.max(sustainInputPort.out(), 0), 1);
        }
    }


    @InputPort
    private Port releaseInputPort = Port.ONE;

    /**
     * Sets the <code>releaseInputPort</code> which controls the release time of the envelope.
     * @param releaseInputPort the <code>Port</code> to be set
     */
    public void setReleaseInputPort(Port releaseInputPort) {
        if(releaseInputPort != null) {
            this.releaseInputPort = () -> Math.max(releaseInputPort.out(), 1);
        }
    }


    private double lastTriggerSignal;


    /**
     * Constructs and initializes an <code>ADSREnvelope</code> and sets the ADSR-InputPorts.
     * @param attackInputPort the <code>attackInputPort</code> of the <code>Envelope</code>
     * @param decayInputPort the <code>decayInputPort</code> of the <code>Envelope</code>
     * @param sustainInputPort the <code>sustainInputPort</code> of the <code>Envelope</code>
     * @param releaseInputPort the <code>releaseInputPort</code> of the <code>Envelope</code>
     */
    public ADSREnvelope(Port attackInputPort, Port decayInputPort, Port sustainInputPort, Port releaseInputPort) {
        setAttackInputPort(attackInputPort);
        setDecayInputPort(decayInputPort);
        setSustainInputPort(sustainInputPort);
        setReleaseInputPort(releaseInputPort);

        stage = STAGES.OFF;
    }

    /**
     * Constructs and initializes an <code>ADSREnvelope</code> and sets constant ADSR-values.
     *
     * All parameters, except the sustain level, should be in samples (for example if the attack time should be 1 second
     * you need to multiply the samplerate you are using by 1, if you want half a second you multiply your samplerate by 0.5).
     * The sustain level is a value between 0 and 1.
     *
     * @param attack the attack time of the <code>Envelope</code>
     * @param decay the decay time of the <code>Envelope</code>
     * @param sustain the sustain level of the <code>Envelope</code>
     * @param release the release time of the <code>Envelope</code>
     */
    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        attackInputPort =  (attack  >= 0 ? () -> attack  : Port.NULL);
        decayInputPort =   (decay   >= 1 ? () -> decay   : Port.ONE);
        sustainInputPort = (sustain >= 0 && sustain <= 1 ? () -> sustain : Port.ONE);
        releaseInputPort = (release >= 1 ? () -> release : Port.ONE);

        stage = STAGES.OFF;
    }

    /**
     * A copy constructor to copy an existing <code>ADSREnvelope</code>
     * @param adsrEnvelope the existing <code>ADSREnvelope</code>
     */
    private ADSREnvelope(ADSREnvelope adsrEnvelope) {
        this.attackInputPort =  adsrEnvelope.attackInputPort;
        this.decayInputPort =   adsrEnvelope.decayInputPort;
        this.sustainInputPort = adsrEnvelope.sustainInputPort;
        this.releaseInputPort = adsrEnvelope.releaseInputPort;
    }



    @Override
    protected double modulate() {
        if(lastTriggerSignal < triggerInputPort.out()) {
            lastTriggerSignal = triggerInputPort.out();
            press();
        }else if(lastTriggerSignal > triggerInputPort.out()) {
            lastTriggerSignal = triggerInputPort.out();
            release();
        }

        switch (stage) {
            case ATTACK:
                value += 1 / attackInputPort.out();
                if(value >= 1) {
                    value = 1;
                    stage = STAGES.DECAY;
                }
                return value * mainInputPort.out();

            case DECAY:
                value -= (1-sustainInputPort.out()) / decayInputPort.out();
                if(value <= sustainInputPort.out()) {
                    value = sustainInputPort.out();
                    stage = STAGES.SUSTAIN;
                }
                return value * mainInputPort.out();

            case SUSTAIN:
                return sustainInputPort.out() * mainInputPort.out();

            case RELEASE:
                value -= sustainInputPort.out() / releaseInputPort.out();
                if(value <= 0) {
                    value = 0;
                    stage = STAGES.OFF;
                }
                return value * mainInputPort.out();

            default: // case OFF
                return 0;
        }
    }


    /**
     * Triggers the envelope.
     */
    @Override
    public void press() {
        stage = STAGES.ATTACK;
    }

    /**
     * Releases the envelope
     */
    @Override
    public void release() {
        stage = STAGES.RELEASE;
    }



    @Override
    public String toString() {
        return "Envelope[Type=ADSR, Attack="+attackInputPort.out()+", Decay="+decayInputPort.out()+", Sustain="+ sustainInputPort.out()+", Release="+ releaseInputPort.out()+"]";
    }

    @Override
    public Envelope clone() {
        return new ADSREnvelope(this);
    }
}
