package audio.modules.controls;

import audio.modules.io.*;

/**
 * The {@code ModulatedControlKnob}
 */

public class ModulatedControlKnob{

    @InputPort
    private Port input;

    public void setInput(Port input) {
        this.input = input;
    }

    @OutputPort
    private Port output;

    public Port getOutput() {
        return output;
    }


    private double knobRotation; // Between 0 and 1


    public ModulatedControlKnob(Port input) {
        this.input = input;
        this.output = () -> input.out() * knobRotation;

        knobRotation = 1;
    }



    public void setKnobRotation(double knobRotation) {
        this.knobRotation = Math.max(Math.min(knobRotation, 1), 0);
    }

    public double getKnobRotation() {
        return knobRotation;
    }
}
