package audio.components;

import io.GeneratorInputStream;

public abstract class Generator /*TODO extends (Interface) "keine ahnung"     (um später Filter nicht mit Generator zu implementen für logik dies das)*/{

    protected double t = 0;

    public void reset() {
        t = 0;
    }

    public abstract double next();


    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }


    public GeneratorInputStream getGeneratorInputStream() {
        return new GeneratorInputStream(this);
    }


    public abstract String toString();
    public abstract Generator clone();

}
