package audio.modules.controls;

public class Dial<T extends Enum<T>> {

    Enum<T>[] options;
    int selectedIndex;

    public Dial(Enum<T>... options) {
        this.options = options;
    }

    public void select(int index) {
        if(index < options.length && index >= 0) {
            selectedIndex = index;
        }
    }

    public Enum<T> getSelected() {
        return options[selectedIndex];
    }
}
