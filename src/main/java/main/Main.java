package main;

import audio.Synthesizer;
import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;
import audio.enums.WaveForm;
import audio.modules.BasicADSRModule;
import audio.modules.BasicOscillatorModule;
import io.AudioPlayer;
import midi.PCKeyboard;

import javax.sound.sampled.*;

public class Main {

    public static void main(String[] args) {

/*
        ADSREnvelope asdrEnv = new ADSREnvelope(3000, 6000, 0.8, 8000);
        ADSREnvelope asdrEnv2 = new ADSREnvelope(4000, 5000, 0.7, 10000);

        ModulatedOscillator osc = new ModulatedSquareOscillator(
                asdrEnv2.asInterface(261.63), //ModulationInterface.CONSTANT(261.63),
                asdrEnv.asInterface(1),
                0,
                44100);



        // Create some sample data

        int steps = 44100;
        double width = 300;

        double[] x = new double[steps+1];
        double[] y1 = new double[steps+1];
        double[] y2 = new double[steps+1];
        int c = 0;

        asdrEnv.press();

        for(double i = 0; i < width; i += (width/steps)){
            x[c] = i;
            y1[c] = osc.next();
            osc.setT(i);
            if(i > 220) {
                asdrEnv.release();
            }
            c++;

            if(c % 100 == 0) {
                System.out.println(osc.getAmplitude());
            }
        }

        MatlabChart fig = new MatlabChart();
        fig.plot(x, y1, "-r", 1.5f, "C4");
        //fig.plot(x, y2, ":k", 3.0f, "BAC");
        fig.RenderPlot();
        fig.title("");
        fig.xlim(-10, width);
        fig.ylim(-1.5, 1.5);
        fig.xlabel("time");
        fig.ylabel("amp");
        fig.grid("on","on");
        fig.legend("northeast");
        fig.font("Helvetica",15);
        fig.saveas("MyPlot.png",640,480);

*/


        SineOscillator osc1 = new SineOscillator(() -> 440, () -> 100,  44100);
        SineOscillator osc2 = new SineOscillator(() -> 220, () -> 100, 44100);

        //TODO Generator gen = new Mixer(osc1, osc2);



        /*AudioPlayer ap = new AudioPlayer(new Synthesizer(null));

        ap.init();
        ap.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ap.stop();
        System.out.println("Audiowiedergabe gestoppt");
*/

        /*  Modules  */

        BasicOscillatorModule basicOscillatorModule = new BasicOscillatorModule();

        PCKeyboard keyboard = new PCKeyboard();


        basicOscillatorModule.setWaveForm(WaveForm.SINE);
        basicOscillatorModule.setFrequencyInput(keyboard.getMainOutput());


        AudioPlayer audioPlayer = new AudioPlayer(keyboard.getMainOutput());

        audioPlayer.init();
        audioPlayer.start();
        //Thread.sleep(20000);
        //audioPlayer.stop();



        Oscillator timeTestOsc = new SineOscillator(() -> 440, () -> 100, 44100);

        long time = System.nanoTime();

        for (int i = 0; i < 44100; i++) {
            timeTestOsc.getMainOutput().out();
        }

        System.out.println(System.nanoTime() - time);


        // TODO AudioPlayer mal einfach nur eine Zahl füttern oder andere potenziell lustige Dinge

/*                      Microphone
        try {
            Microphone mic = new Microphone(TEST_FORMAT);
                    new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        16000,
                        16,
                        1,
                        2,
                        16000,
                        false));

            //Modificator modificator = new Modificator(mic, lfo);

            AudioPlayer audioPlayer = new AudioPlayer(new Synthesizer(mic));
            audioPlayer.init();

            mic.connectMicrophone();
            mic.startListening();

            audioPlayer.start();

            Thread.sleep(50000);

            audioPlayer.stop();

            mic.stopListening();
            mic.disconnectMicrophone();

        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
*/

    }


    public static final AudioFormat TEST_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            22050,
            8,
            1,
            1,
            22050,
            false);

}
