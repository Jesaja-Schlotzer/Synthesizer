package audio.components.modulators;

import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;

public class KPWasDasWirdUndWoDasHinkommt implements Modulator {
    @Override
    public double modulate() {
        return 0;
    }

    /*  TODO Modulatoren die nur z.B den Wert der reinkommt * 0.75 nehmen
        also sehr elementare Aufgaben erledigen.
        Diese könnten nat. auch einfach mit einer direkten Modulator implem. gemacht werden,
        new "Beschränker"(0.75) ist aber halt sprechender als (in) -> in*0.75
     */



}
