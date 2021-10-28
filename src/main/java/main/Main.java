package main;

import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;
import io.AudioPlayer;
import ui.MatlabChart;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Create some sample data
        double[] x = new double[100]; x[0] = 1;
        double[] y1 = new double[100]; y1[0] = 200;
        double[] y2 = new double[100]; y2[0] = 300;
        for(int i = 1; i < x.length; i++){
            x[i] = i+1;
            y1[i] = y1[i-1] + Math.random()*10 - 4;
            y2[i] = y2[i-1] + Math.random()*10 - 6;
        }

        MatlabChart fig = new MatlabChart();
        fig.plot(x, y1, "-r", 2.0f, "AAPL");
        fig.plot(x, y2, ":k", 3.0f, "BAC");
        fig.RenderPlot();
        fig.title("Stock 1 vs. Stock 2");
        fig.xlim(10, 100);
        fig.ylim(200, 300);
        fig.xlabel("Days");
        fig.ylabel("Price");
        fig.grid("on","on");
        fig.legend("northeast");
        fig.font("Helvetica",15);
        fig.saveas("MyPlot.png",640,480);


        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.test();

        audioPlayer.play();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        audioPlayer.stop();



        AudioSystem.getAudioFileReaders();


        int BUFFER_SIZE = 44100*10;
        Oscillator oscillator = new SineOscillator(220, 128, 0, 44100);

        SourceDataLine line = null;
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(oscillator.getGeneratorInputStream());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioFormat audioFormat = ais.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        line.start();
        byte[] samples = new byte[BUFFER_SIZE];
        int count = 0;
        while (true) {
            try {
                if ((count = ais.read(samples, 0, BUFFER_SIZE)) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            line.write(samples, 0, count);

            line.drain();
            line.close();
        }
    }



}
