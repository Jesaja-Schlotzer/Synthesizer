package audio.components.modulators.envelopes;

import io.interfaces.Pressable;

public abstract class Envelope implements Pressable {

    enum STAGE {
        NONE, ATTACK, DECAY, SUSTAIN, RELEASE, HOLD, DELAY
    }

    private double value;
    private STAGE state;

    public abstract String toString();
    public abstract Envelope clone();
}
