package audio.modules.controls;

import audio.modules.io.OutputPort;
import audio.modules.io.Port;

import java.util.function.DoublePredicate;

/**
 * The <code>ControlKnob</code> acts like a real knob. It holds a value that can be accessed with setter and getter.
 */
public class ControlKnob {

    public static final DoublePredicate ZERO_TO_ONE =   n -> n <= 1 && n >= 0;
    public static final DoublePredicate SIGNED_BYTE =   n -> n <= 127 && n >= -128;
    public static final DoublePredicate UNSIGNED_BYTE = n -> n <= 256 && n >= 0;
    public static final DoublePredicate NON_NEGATIVE =  n -> n >= 0;


    @OutputPort
    protected final Port outputPort = this::getValue;

    /**
     * @return the <code>OutputPort</code> that supplies the knob-value
     */
    public Port getOutputPort() {
        return outputPort;
    }


    private final DoublePredicate valueConstraints;
    private double value;

    /**
     * Constructs a <code>ControlKnob</code> with given value constraints.
     * @param valueConstraints a <code>DoublePredicate</code> that represents the constraints of the knob-value
     */
    public ControlKnob(DoublePredicate valueConstraints) {
        this.valueConstraints = valueConstraints;
    }

    /**
     * Sets the knob-value if the parameter value matches the <code>valueConstraints</code>.
     * @param value The value to be set
     */
    public void setValue(double value) {
        if(valueConstraints.test(value)) {
            this.value = value;
        }
    }

    /**
     * @return the knob-value
     */
    public double getValue() {
        return value;
    }
}
