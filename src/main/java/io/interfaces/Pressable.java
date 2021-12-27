package io.interfaces;

/**
 * A class implements the <code>Pressable</code> interface to indicate that it has a "press" action.
 * An example would be an <code>Envelope</code> which can be pressed and released.
 */
public interface Pressable {
    void press();
    void release();
}
