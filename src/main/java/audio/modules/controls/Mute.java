package audio.modules.controls;

import audio.modules.io.Port;

/**
 * The <code>Mute</code> can be used to mute the incoming signal when needed.
 */
public class Mute extends ControlComponent{

    private boolean muted = true;

    /**
     * Constructs a <code>Mute</code> object and sets the incoming signal <code>Port</code>.
     * @param inputPort The incoming signal which can be muted
     */
    public Mute(Port inputPort) {
        setInputPort(inputPort);
    }

    /**
     * @return if <code>muted</code> is true 0 is returned, if <code>muted</code> is false the incoming signal ist returned
     */
    @Override
    protected double get() {
        return (muted ? 0 : inputPort.out());
    }

    /**
     * Mutes the signal
     */
    public void mute() {
        muted = true;
    }

    /**
     * Unmutes the signal
     */
    public void unmute() {
        muted = false;
    }
}
