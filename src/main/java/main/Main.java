package main;

import audio.Synthesizer;
import audio.components.*;
import audio.components.combiners.Mixer;
import audio.components.modulators.envelopes.ADSREnvelope;
import audio.components.oscillators.*;
import audio.components.oscillators.modulated.ModulatedOscillator;
import audio.components.oscillators.modulated.ModulatedSineOscillator;
import audio.components.oscillators.modulated.ModulatedSquareOscillator;
import audio.interfaces.ModulationInterface;
import audio.interfaces.Modulator;
import io.AudioPlayer;
import ui.MatlabChart;

public class Main {

    public static void main(String[] args) {

        ADSREnvelope asdrEnv = new ADSREnvelope(7000, 8500, 0.3, 10000);

        ModulatedOscillator osc = new ModulatedSquareOscillator(
                ModulationInterface.CONSTANT(261.63),
                asdrEnv.asInterface(64),
                0,
                44100);

        osc.reset();

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



        ModulatedOscillator osc1 = new ModulatedSineOscillator(
                ModulationInterface.CONSTANT(261.63),
                asdrEnv.asInterface(64),
                0,
                44100);

        Oscillator osc2 = new SineOscillator(440, 32, 0, 44100);

        Generator gen = osc1;//new Mixer(osc1, osc2);

        for (int i = 0; i < 441; i++) {
            //System.out.println(osc1.next());
        }

        gen.reset();

        new Thread(() -> {
            try {
                asdrEnv.press();
                Thread.sleep(2000);
                asdrEnv.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        AudioPlayer ap = new AudioPlayer(new Synthesizer(gen));

        ap.init();
        ap.start();
        ap.play(3);
        ap.stop();


    }


    }
