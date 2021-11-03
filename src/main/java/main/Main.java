package main;

import audio.components.oscillators.Oscillator;
import audio.components.oscillators.SineOscillator;
import audio.enums.SampleRate;
import audio.enums.WaveForm;
import audio.modules.BasicOscillatorModule;
import io.AudioPlayer;
import midi.PCKeyboard;

import javax.sound.sampled.*;

public class Main {

    public static void main(String[] args) { // TODO schauen wie sich 16 bit depth machen lassen

        /*  Modules  */

        BasicOscillatorModule basicOscillatorModule = new BasicOscillatorModule();

        PCKeyboard keyboard = new PCKeyboard();


        basicOscillatorModule.setWaveForm(WaveForm.SINE);
        basicOscillatorModule.setFrequencyInput(keyboard.getMainOutputPort());


        AudioPlayer audioPlayer = new AudioPlayer(keyboard.getMainOutputPort());

        audioPlayer.init();
        audioPlayer.start();
        //Thread.sleep(20000);
        //audioPlayer.stop();



        Oscillator timeTestOsc = new SineOscillator(() -> 440, () -> 100, SampleRate._44100);

        long time = System.nanoTime();

        for (int i = 0; i < 44100; i++) {
            timeTestOsc.getMainOutputPort().out();
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
