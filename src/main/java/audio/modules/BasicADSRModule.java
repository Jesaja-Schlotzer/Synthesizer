package audio.modules;

import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.TriangleOscillator;
import audio.enums.SampleRate;
import audio.modules.controls.ControlKnob;
import audio.modules.io.*;
import io.AudioPlayer;

/**
 * An implementation of an ADSREnvelope module.
 * It takes in an input signal, adjusts its amplitude according to the <code>Envelope</code> and sends the adjusted signal to the output port.
 */
public class BasicADSRModule {

    public final ControlKnob attackKnob;
    public final ControlKnob decayKnob;
    public final ControlKnob sustainKnob;
    public final ControlKnob releaseKnob;

    private final ADSREnvelope adsrEnvelope;

    /**
     * @param mainInputPort the signal that should be modulated by the envelope
     */
    public void setMainInputPort(Port mainInputPort) {
        if (mainInputPort != null) {
            adsrEnvelope.setMainInputPort(mainInputPort);
        }
    }


    @OutputPort
    private final Port outputPort;

    /**
     * @return the <code>outputPort</code> that supplies the modulated signal
     */
    public Port getOutputPort() {
        return outputPort;
    }

    /**
     * Constructs an <code>BasicADSRModule</code> object.
     */
    public BasicADSRModule() {
        attackKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        decayKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);
        sustainKnob = new ControlKnob(ControlKnob.ZERO_TO_ONE);
        releaseKnob = new ControlKnob(ControlKnob.NON_NEGATIVE);

        adsrEnvelope = new ADSREnvelope(attackKnob::getValue, decayKnob::getValue, sustainKnob::getValue, releaseKnob::getValue);

        outputPort = adsrEnvelope.getOutputPort();
    }

    /**
     * @param triggerInputPort The <code>inputPort</code> that supplies a trigger signal for the <code>Envelope</code>
     */
    public void setTriggerInputPort(Port triggerInputPort) {
        if (triggerInputPort != null) {
            adsrEnvelope.setTriggerInputPort(triggerInputPort);
        }
    }


    /**
     * An example of how the <code>BasicADSRModule</code> could be used.
     */
    public static void main(String[] args) {

        Oscillator oscillator = new TriangleOscillator(440, 32, SampleRate._44100);
        BasicADSRModule basicADSRModule = new BasicADSRModule();

        // Setting the input port of the envelope
        basicADSRModule.setMainInputPort(oscillator.getOutputPort());

        // Setting the attributes of the envelope
        basicADSRModule.attackKnob.setValue(88200);
        basicADSRModule.decayKnob.setValue(22050);
        basicADSRModule.sustainKnob.setValue(0.8);
        basicADSRModule.releaseKnob.setValue(44100);

        // Creating an AudioPlayer and setting its input to the output of the envelope
        AudioPlayer audioPlayer = new AudioPlayer(basicADSRModule.getOutputPort());

        // Initializing and starting the AudioPlayer
        audioPlayer.init();
        audioPlayer.start();

        try {
            // Triggering the envelope
            basicADSRModule.adsrEnvelope.press();
            Thread.sleep(3000);
            // Releasing the envelope
            basicADSRModule.adsrEnvelope.release();

            Thread.sleep(2500);

            // Setting the attributes of the envelope
            basicADSRModule.attackKnob.setValue(11200);
            basicADSRModule.decayKnob.setValue(5050);
            basicADSRModule.sustainKnob.setValue(0.8);
            basicADSRModule.releaseKnob.setValue(11000);

            // Triggering the envelope
            basicADSRModule.adsrEnvelope.press();
            Thread.sleep(1500);
            // Releasing the envelope
            basicADSRModule.adsrEnvelope.release();

            Thread.sleep(1500);

            // Setting the attributes of the envelope
            basicADSRModule.attackKnob.setValue(200);
            basicADSRModule.decayKnob.setValue(150);
            basicADSRModule.sustainKnob.setValue(0.8);
            basicADSRModule.releaseKnob.setValue(5000);

            // Triggering the envelope
            basicADSRModule.adsrEnvelope.press();
            Thread.sleep(700);
            // Releasing the envelope
            basicADSRModule.adsrEnvelope.release();

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stopping the AudioPlayer
        audioPlayer.stop();

        System.exit(0);
    }
}
