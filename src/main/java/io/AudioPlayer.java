package io;

import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AudioPlayer {

    Clip clip;

    Oscillator oscillator;

    public AudioPlayer() {
        oscillator = new SineOscillator(220, 128, 0, 44100);
    }



    public void test() {
        if ( clip!=null ) {
            clip.stop();
            clip.close();
        } else {
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }


        AudioFormat af = new AudioFormat(
                44100,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );


        AudioInputStream ais = new AudioInputStream(
                oscillator.getGeneratorInputStream(),
                af,
                44100);

        try {


            clip.open(ais);


        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void play() {
        clip.start();
    }


    public void stop() {
        clip.stop();
    }



    public void loopSound(boolean commence) {
        if ( commence ) {
            clip.setFramePosition(0);
            clip.loop( Clip.LOOP_CONTINUOUSLY );
        } else {
            clip.stop();
        }
    }

    public void generateTone() throws LineUnavailableException {

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


        SineOscillator sineOsc = new SineOscillator(440, 1, 0, 44100);

        sineOsc.init();



        int maxVol = 64;
        for(int i=0; i<intFPW*wavelengths; i++){
            /*double angle = ((float)(i*2)/((float)intFPW))*(Math.PI);
            buf[i*2]= getByteValue(angle);
            if(addHarmonic) {
                buf[(i*2)+1]=getByteValue(2*angle);
            } else {
                buf[(i*2)+1] = buf[i*2];
            }*/


            buf[i*2] = (byte) (sineOsc.next() * 128.0);
            buf[(i*2)+1] = buf[i*2];

            //System.out.println(buf[i*2]);
        }
        System.out.println(intFPW*wavelengths);

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

    private byte getByteValue(double angle) {
        int maxVol = 127;
        return (byte) Math.round(Math.sin(angle) * maxVol);
    }
}
