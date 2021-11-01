package audio.components;


public abstract class Generator {

    public abstract double next();


    public abstract String toString();

    public abstract Generator clone();

}
