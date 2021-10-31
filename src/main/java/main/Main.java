package main;

import audio.Synthesizer;
import audio.components.*;
import audio.components.modulators.Modificator;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.modulators.lfos.LFO;
import audio.components.oscillators.*;
import audio.components.oscillators.modulated.ModulatedOscillator;
import audio.components.oscillators.modulated.ModulatedSineOscillator;
import audio.components.oscillators.modulated.ModulatedSquareOscillator;
import audio.interfaces.ModulationInterface;
import io.AudioPlayer;
import io.Microphone;
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

        ADSREnvelope asdrEnv = new ADSREnvelope(3000, 6000, 0.4, 8000);
        ADSREnvelope asdrEnv2 = new ADSREnvelope(20000, 5000, 0.7, 20000);

        LFO lfo = new LFO(new SineOscillator(3, 1, 0, 44100));


        ModulatedOscillator osc1 = new ModulatedSineOscillator(
                //asdrEnv.asInterface(261.63),
                ModulationInterface.CONSTANT(261.63),
                asdrEnv2.asInterface(64),
                0,
                44100);


        Generator gen = osc1;

        gen.reset();

        new Thread(() -> {
            try {
                asdrEnv.press();
                asdrEnv2.press();
                Thread.sleep(10);
                asdrEnv.release();
                asdrEnv2.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        AudioPlayer ap = new AudioPlayer(new Synthesizer(gen));

        ap.init();
        ap.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ap.stop();


        /*  PC Keyboard  */

        PCKeyboard keyboard = new PCKeyboard(
                new ModulatedSineOscillator(()->0, ()->64, 0, 44100),
                new ADSREnvelope(5000, 6000, 0.5, 10000)
                );


        AudioPlayer ap2 = new AudioPlayer(keyboard.getOutputGenerator());

        ap2.init();
        ap2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //ap2.stop();

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
