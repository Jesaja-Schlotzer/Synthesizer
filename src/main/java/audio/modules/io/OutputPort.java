package audio.modules.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a <code>Port</code> as an <code>OutputPort</code>
 */
@Target(ElementType.FIELD)
public @interface OutputPort {

}
