package audio.modules.controls;

import audio.modules.io.Port;

import java.util.function.DoublePredicate;

/**
 * The <code>ModulatableControlKnob</code> is similar to the <code>ControlKnob</code>,
 * the difference is that the <code>ModulatableControlKnob</code> modulates an incoming signal by multiplying it with the current knob rotation value.
 */
public class ModulatableControlKnob extends ControlComponent{

    public static final DoublePredicate ZERO_TO_ONE = n -> n <= 1 && n >= 0;
    public static final DoublePredicate NEGATIV_ONE_TO_ONE =   n -> n <= 1 && n >= -1;
    public static final DoublePredicate ZERO_TO_TWO = n -> n <= 2 && n >= 0;


    private double knobRotation = 1;
    private final DoublePredicate knobRotationConstraints;

    /**
     * Constructs a <code>ModulatableControlKnob</code> with a given <code>InputPort</code> and <code>knobRotationConstraints</code>.
     * @param inputPort the input signal that will be modulated
     * @param knobRotationConstraints a <code>DoublePredicate</code> that represents the constraints of the knob rotation value
     */
    public ModulatableControlKnob(Port inputPort, DoublePredicate knobRotationConstraints) {
        setInputPort(inputPort);
        this.knobRotationConstraints = knobRotationConstraints;

    }


    @Override
    protected double get() {
        return knobRotation * inputPort.out();
    }

    /**
     * Sets the knob rotation value if the parameter <code>knobRotation</code> matches the <code>valueConstraints</code>.
     * @param knobRotation The knob rotation to be set
     */
    public void setKnobRotation(double knobRotation) {
        if(knobRotationConstraints.test(knobRotation)) {
            this.knobRotation = knobRotation;
        }
    }
}
