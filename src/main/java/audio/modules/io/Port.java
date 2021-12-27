package audio.modules.io;

/**
 * <code>Ports</code> can occur as <code>InputPorts</code> and <code>OutputPorts</code> in any device.
 * The <code>InputPort</code> only gets a setter,
 * while the <code>OutputPort</code> can only be returned via a getter.
 * This is because an output is declared when a device is created
 * and should not be changed afterwards.
 * An <code>InputPort</code> however, has to be set after the device has been created,
 * to send a signal to the device at all.
 */
public interface Port {

    Port NULL = () -> 0;
    Port ONE = () -> 1;

    /**
     * @return the signal that has been sent to the port.
     */
    double out();

}
