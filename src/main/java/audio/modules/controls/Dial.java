package audio.modules.controls;

import audio.enums.WaveForm;

public class Dial<T> {

    T[] options;
    T selected;

    @SafeVarargs
    public Dial(T... options) {
        this.options = options;
        this.selected = this.options[0];
    }

    public void select(int index) {
        if(index < options.length && index >= 0) {
            selected = options[index];
        }
    }

    public void select(T selection) {
        for (T t : options) {
            if(t.equals(selection))
                selected = selection;
        }
    }


    public T getSelected() {
        return selected;
    }
}
