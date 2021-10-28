package main;

import ui.MatlabChart;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

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


        try {
            generateTone();
            loopSound(true);
            play();
            Thread.sleep(1000);
            stop();
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    static Clip clip;


    private static void play() {
        clip.start();
    }


    private static void stop() {
        clip.stop();
    }



    public static void loopSound(boolean commence) {
        if ( commence ) {
            clip.setFramePosition(0);
            clip.loop( Clip.LOOP_CONTINUOUSLY );
        } else {
            clip.stop();
        }
    }

    public static void generateTone() throws LineUnavailableException {

        if ( clip!=null ) {
            clip.stop();
            clip.close();
        } else {
            clip = AudioSystem.getClip();
        }
        boolean addHarmonic = false;

        int intFPW = 440;

        float sampleRate = 44100;

        // oddly, the sound does not loop well for less than
        // around 5 or so, wavelengths
        int wavelengths = 20;
        byte[] buf = new byte[2*intFPW*wavelengths];
        AudioFormat af = new AudioFormat(
                sampleRate,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );

        int maxVol = 127;
        for(int i=0; i<intFPW*wavelengths; i++){
            double angle = ((float)(i*2)/((float)intFPW))*(Math.PI);
            buf[i*2]= getByteValue(angle);
            if(addHarmonic) {
                buf[(i*2)+1]=getByteValue(2*angle);
            } else {
                buf[(i*2)+1] = buf[i*2];
            }
        }

        try {
            byte[] b = buf;
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(b),
                    af,
                    buf.length/2 );

            clip.open( ais );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static byte getByteValue(double angle) {
        int maxVol = 127;
        return (byte) Math.round(Math.sin(angle) * maxVol);
    }

}
