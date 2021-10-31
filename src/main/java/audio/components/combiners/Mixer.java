package audio.components.combiners;

import audio.components.Generator;

public class Mixer extends Generator{

    private Generator[] generators;

    public Mixer(Generator... generators) {
        this.generators = generators;
    }


    @Override
    public double next() {
        double sum = 0;

        for (Generator g : generators) {
            sum += g.next();
        }

        return sum / generators.length;
    }



    @Override
    public String toString() {
        return null;
    }


    @Override
    public Generator clone() {
        Generator[] clonedGenerators = new Generator[generators.length];
        for (int i = 0; i < generators.length; i++) {
            clonedGenerators[i] = generators[i].clone();
        }
        return new Mixer(clonedGenerators);
    }
}
