package audio.modules.controls;

import audio.modules.io.InputPort;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;

/**
 * <code>ControlComponent</code> classes can be used to adjust an input signal.
 * A signal is received on the <code>InputPort</code> and after the adjustments
 * are made the adjusted signal is sent to the <code>OutputPort</code>.
 */
public abstract class ControlComponent{

    @InputPort
    protected Port inputPort = Port.NULL;

    /**
     * @param inputPort The source of the unadjusted signal
     */
    public void setInputPort(Port inputPort) {
        if (inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    @OutputPort
    protected final Port outputPort = this::get;

    /**
     * @return the <code>OutputPort</code> that supplies the adjusted signal
     */
    public Port getOutputPort() {
        return outputPort;
    }

    /**
     * @return the adjusted signal
     */
    protected abstract double get();
}
