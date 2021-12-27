package audio.modules.controls;

/**
 * A dial that can switch between different options.
 * @param <T> the type of the options
 */
public class Dial<T> {

    private final T[] options;
    private T selected;

    /**
     * Constructs a <code>Dial</code> and sets the list of options.
     * @param options the option list
     */
    @SafeVarargs
    public Dial(T... options) {
        this.options = options;
        if(this.options.length > 0) {
            this.selected = this.options[0];
        }else {
            this.selected = null;
        }
    }

    /**
     * Tries to select the option with the index-parameter.
     * @param index the index of the selecting option
     * @return whether the index is in the array bounds or not
     */
    public boolean select(int index) {
        if(index < options.length && index >= 0) {
            selected = options[index];
            return true;
        }else {
            return false;
        }
    }

    /**
     * Tries to select the option with the selection-parameter.
     * @param selection the option to select
     * @return whether the selection occurs in the array or not
     */
    public boolean select(T selection) {
        for (T t : options) {
            if (t.equals(selection)) {
                selected = selection;
                return true;
            }
        }
        return false;
    }


    /**
     * @return the selected option
     */
    public T getSelected() {
        return selected;
    }
}
