package audio.components.modulators.envelopes;

import audio.modules.io.*;

public class ADSREnvelope extends Envelope{

    @InputPort
    private Port attackInputPort = Port.NULL;

    public void setAttackInputPort(Port attackInputPort) {
        if(attackInputPort != null) {
            this.attackInputPort = () -> Math.max(attackInputPort.out(), 0);
        }
    }


    @InputPort
    private Port decayInputPort = Port.ONE;

    public void setDecayInputPort(Port decayInputPort) {
        if(decayInputPort != null) {
            this.decayInputPort = () -> Math.max(decayInputPort.out(), 1);
        }
    }


    @InputPort
    private Port sustainInputPort = Port.ONE;

    public void setSustainInputPort(Port sustainInputPort) {
        if(sustainInputPort != null) {
            this.sustainInputPort = () -> Math.min(Math.max(sustainInputPort.out(), 0), 1);
        }
    }


    @InputPort
    private Port releaseInputPort = Port.ONE;

    public void setReleaseInputPort(Port releaseInputPort) {
        if(releaseInputPort != null) {
            this.releaseInputPort = () -> Math.max(releaseInputPort.out(), 1);
        }
    }


    private double lastTriggerSignal;


    public ADSREnvelope(Port attackInputPort, Port decayInputPort, Port sustainInputPort, Port releaseInputPort) {
        setAttackInputPort(attackInputPort);
        setDecayInputPort(decayInputPort);
        setSustainInputPort(sustainInputPort);
        setReleaseInputPort(releaseInputPort);

        stage = STAGES.NONE;
    }


    public ADSREnvelope(double attack, double decay, double sustain, double release) {
        attackInputPort =  (attack  >= 0 ? () -> attack  : Port.NULL);
        decayInputPort =   (decay   >= 1 ? () -> decay   : Port.ONE);
        sustainInputPort = (sustain >= 0 && sustain <= 1 ? () -> sustain : Port.ONE);
        releaseInputPort = (release >= 1 ? () -> release : Port.ONE);

        stage = STAGES.NONE;
    }


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
                    stage = STAGES.NONE;
                }
                return value * mainInputPort.out();

            default: // case NONE
                return 0;
        }
    }



    @Override
    public void press() {
        stage = STAGES.ATTACK;
    }


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
