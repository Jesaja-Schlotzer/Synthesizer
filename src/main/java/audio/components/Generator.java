package audio.components;

import audio.modules.io.OutputPort;
import audio.modules.io.Port;

/**
 * A <code>Generator</code> generates an audio signal and sends it to its <code>OutputPort</code>.
 * An example of a <code>Generator</code> is an <code>Oscillator</code>.
 */
public abstract class Generator {

    @OutputPort
    private final Port outputPort = this::next;

    /**
     * @return the <code>OutputPort</code> that the generated data is sent to
     */
    public Port getOutputPort() {
        return outputPort;
    }


    /**
     * The <code>next</code>-method returns the next audio sample that the generator has generated.
     * @return Returns the next audio sample.
     */
    protected abstract double next();


    public abstract String toString();
}
