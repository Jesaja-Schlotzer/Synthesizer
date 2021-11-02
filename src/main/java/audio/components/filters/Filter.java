package audio.components.filters;

import audio.modules.io.*;

public abstract class Filter {

    @InputPort
    protected Port inputPort = Port.NULL;

    public void setInputPort(Port inputPort) {
        if(inputPort != null) {
            this.inputPort = inputPort;
        }
    }


    @OutputPort
    protected final Port outputPort = this::filter;

    public Port getOutputPort() {
        return outputPort;
    }


    protected abstract double filter();


    public abstract Filter clone();
}
