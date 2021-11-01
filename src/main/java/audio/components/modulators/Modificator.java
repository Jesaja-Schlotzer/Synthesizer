package audio.components.modulators;

import audio.components.Generator;
import audio.interfaces.Modulator;

public class Modificator extends Generator {
    // TODO maybe ist das ehr ein Filter also viellciht ehr Filter statt Modificator mal schauen
    private final Generator generator;
    private final Modulator modulator;

    public Modificator(Generator generator, Modulator modulator) {
        this.generator = generator;
        this.modulator = modulator;
    }

    @Override
    public double next() {
        return modulator.modulate() * generator.next();
    }

    @Override
    public String toString() {
        return "Modificator[Generator="+ generator.toString() +", Modulator="+ modulator.toString() +"]";
    }

    @Override
    public Generator clone() {
        return new Modificator(generator.clone(), modulator);
    }
}
