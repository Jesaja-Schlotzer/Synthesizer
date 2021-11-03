package audio.modules.controls;


public class Dial<T> {

    T[] options;
    T selected;

    @SafeVarargs
    public Dial(T... options) {
        this.options = options;
        this.selected = this.options[0];
    }

    public boolean select(int index) {
        if(index < options.length && index >= 0) {
            selected = options[index];
            return true;
        }else {
            return false;
        }
    }

    public boolean select(T selection) {
        for (T t : options) {
            if (t.equals(selection)) {
                selected = selection;
                return true;
            }
        }
        return false;
    }


    public T getSelected() {
        return selected;
    }
}
