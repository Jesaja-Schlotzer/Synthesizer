package audio.interfaces;

public interface ModulationInterface {
    double get();

    static ModulationInterface CONSTANT(double n) {
        return () -> n;
    }
}
