package audio.modules.controls;

import audio.modules.io.Port;

public class ModulatedControlKnob extends ControlComponent{

    private double knobRotation = 1;


    public ModulatedControlKnob(Port inputPort) {
        setInputPort(inputPort);
    }


    @Override
    protected double get() {
        return knobRotation * inputPort.out();
    }


    public void setKnobRotation(double knobRotation) {
        this.knobRotation = Math.max(Math.min(knobRotation, 1), 0);
    }
}
