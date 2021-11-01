package audio.modules.controls;

import java.util.function.DoublePredicate;

public class ControlKnob {

    public static final DoublePredicate ZERO_TO_ONE = n -> n <= 1 && n >= 0;
    public static final DoublePredicate SIGNED_BYTE = n -> n <= 127 && n >= -128;
    public static final DoublePredicate UNSIGNED_BYTE = n -> n <= 256 && n >= 0;
    public static final DoublePredicate NON_NEGATIVE = n -> n >= 0;



    DoublePredicate valueLimitations;
    private double value;

    public ControlKnob(DoublePredicate valueLimitations) {
        this.valueLimitations = valueLimitations;
    }


    public void setValue(double value) {
        if(valueLimitations.test(value)) {
            this.value = value;
        }
    }


    public double getValue() {
        return value;
    }
}
