package audio.components;

import io.GeneratorInputStream;

public abstract class Generator {

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
    public abstract Object clone();

}
