package audio.modules.controls;

import audio.modules.io.Port;

/**
 * Mutes the incoming input if needed
 */

public class Mute extends ControlComponent{

    private boolean muted = true;


    public Mute(Port inputPort) {
        setInputPort(inputPort);
    }

    @Override
    protected double get() {
        return (muted ? 0 : inputPort.out());
    }


    public void mute() {
        muted = true;
    }

    public void unmute() {
        muted = false;
    }
}
