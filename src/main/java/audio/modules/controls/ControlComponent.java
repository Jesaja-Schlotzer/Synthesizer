package audio.modules.controls;

import audio.modules.io.InputPort;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;

/**
 * ControlComponent classes can be used to adjust an input signal.
 * A signal is received on the inputPort and after the adjustments
 * where made the adjusted signal is sent to the outputPort.
 */

public abstract class ControlComponent{

    @InputPort
    protected Port inputPort = Port.NULL;

    public void setInputPort(Port inputPort) {
        if (inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    @OutputPort
    protected final Port outputPort = this::get;

    public Port getOutputPort() {
        return outputPort;
    }


    protected abstract double get();
}
