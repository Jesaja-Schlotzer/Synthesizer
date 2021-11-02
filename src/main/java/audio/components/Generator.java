package audio.components;


public abstract class Generator {

    protected abstract double next();


    public abstract String toString();

    public abstract Generator clone();

}
