package audio;

import audio.components.Generator;

public class Synthesizer extends Generator {


    private Generator generator;

    public Synthesizer(Generator generator) { // TODO später nicht Osc sondern was allgemeineres
        this.generator = generator;
    }



    @Override
    public double next() {
        return generator.next();
    }



    @Override
    public String toString() {
        return null;
    }

    @Override
    public Generator clone() {
        return new Synthesizer(generator.clone());
    }
}
