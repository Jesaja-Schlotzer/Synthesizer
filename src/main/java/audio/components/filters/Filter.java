package audio.components.filters;

import audio.modules.io.*;

/**
 * An abstract audio filter class, providing an input and an output <code>Port</code>.
 * The unfiltered input signal is supplied by the <code>InputPort</code> and the filtered output signal is send to the <code>OutputPort</code>.
 */
public abstract class Filter {

    @InputPort
    protected Port inputPort = Port.NULL;

    /**
     * @param inputPort The source of the unfiltered signal
     */
    public void setInputPort(Port inputPort) {
        if(inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    @OutputPort
    protected final Port outputPort = this::filter;

    /**
     * @return the <code>OutputPort</code> that supplies the filtered signal
     */
    public Port getOutputPort() {
        return outputPort;
    }

    /**
     * @return the filtered signal
     */
    protected abstract double filter();


    public abstract Filter clone();
}
