package audio.interfaces;

public interface Modulator {
    double modulate(double in);

    ModulationInterface asInterface(double in);
}
