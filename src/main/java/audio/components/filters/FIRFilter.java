package audio.components.filters;

import audio.components.combiners.CrossfadeMixer;
import audio.components.lfos.SineLFO;
import audio.components.oscillators.SawtoothOscillator;
import audio.enums.SampleRate;
import io.AudioPlayer;

public class FIRFilter extends Filter{

    private final double[] coefficients; // Koeffizienten des Filters
    private final double[] buffer; // Pufferspeicher für vergangene Signalwerte

    public FIRFilter(double... coefficients) {
        this.coefficients = coefficients;
        this.buffer = new double[coefficients.length];
    }

    @Override
    protected double filter() {
        double output = 0;

        // Speichere das aktuelle Signal im Pufferspeicher
        for (int i = buffer.length - 1; i > 0; i--) {
            buffer[i] = buffer[i-1];
        }
        buffer[0] = inputPort.out();

        // Führe eine Faltung der aktuellen Signalwerte mit den Koeffizienten durch
        for (int i = 0; i < coefficients.length; i++) {
            output += coefficients[i] * buffer[i];
        }

        return output;
    }

    @Override
    public Filter clone() {
        return new FIRFilter(this.coefficients);
    }



    public static void main(String[] args) throws InterruptedException {
        SawtoothOscillator osc = new SawtoothOscillator(1440, 4, SampleRate._44100);

        SawtoothOscillator osc2 = new SawtoothOscillator(1440, 4, SampleRate._44100);


        // Definiere die Koeffizienten für ein Tiefpass-FIR-Filter mit einer Grenzfrequenz von 100 Hz
        double[] coefficients = {0.0031, 0.0156, 0.0347, 0.0521, 0.0681, 0.0811, 0.0907, 0.0964, 0.098, 0.0964, 0.0907, 0.0811, 0.0681, 0.0521, 0.0347, 0.0156, 0.0031};

        FIRFilter filter = new FIRFilter(coefficients);

        filter.setInputPort(osc.getOutputPort());


        SineLFO sineLfo = new SineLFO(0.5, SampleRate._44100);

        CrossfadeMixer mixer = new CrossfadeMixer(osc2.getOutputPort(), filter.getOutputPort(), sineLfo.getOutputPort());


        AudioPlayer player = new AudioPlayer(filter.getOutputPort());

        player.init();
        player.start();

        Thread.sleep(2000);

        player.stop();
    }
}