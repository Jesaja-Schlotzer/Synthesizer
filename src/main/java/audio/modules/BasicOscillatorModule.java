package audio.modules;

import audio.components.Generator;
import audio.components.oscillators.*;
import audio.enums.WaveForm;
import audio.modules.controls.ControlKnob;
import audio.modules.controls.Dial;
import io.AudioPlayer;

import java.util.HashMap;
import java.util.Map;

public class BasicOscillatorModule extends Module {

    private final ControlKnob frequencyControlKnob;
    private final ControlKnob amplitudeControlKnob;
    private final ControlKnob pulseWidthControlKnob;

    private final Dial<WaveForm> waveFormDial;


    Map<WaveForm, Oscillator> oscillatorMap = new HashMap<>();


    public BasicOscillatorModule()  {
        frequencyControlKnob = new ControlKnob();
        amplitudeControlKnob = new ControlKnob();
        pulseWidthControlKnob = new ControlKnob();

        waveFormDial = new Dial<WaveForm>(WaveForm.SINE, WaveForm.SQUARE, WaveForm.SAWTOOTH, WaveForm.TRIANGLE);

        SineOscillator sineOscillator =     new SineOscillator(    frequencyControlKnob::getValue, amplitudeControlKnob::getValue, 0, 44100);
        SquareOscillator squareOscillator =   new SquareOscillator(  frequencyControlKnob::getValue, amplitudeControlKnob::getValue, 0, 44100, pulseWidthControlKnob::getValue);
        SawtoothOscillator sawtoothOscillator = new SawtoothOscillator(frequencyControlKnob::getValue, amplitudeControlKnob::getValue, 0, 44100);
        TriangleOscillator triangleOscillator = new TriangleOscillator(frequencyControlKnob::getValue, amplitudeControlKnob::getValue, 0, 44100);

        oscillatorMap.put(WaveForm.SINE, sineOscillator);
        oscillatorMap.put(WaveForm.SQUARE, squareOscillator);
        oscillatorMap.put(WaveForm.SAWTOOTH, sawtoothOscillator);
        oscillatorMap.put(WaveForm.TRIANGLE, triangleOscillator);

        setOscillatorFrequency(261.63);
        setOscillatorAmplitude(100);
    }



    public double out() {
        return oscillatorMap.get(waveFormDial.getSelected()).next();
    }




    /*  CONTROLS  */

    public void setOscillatorFrequency(double value) {
        frequencyControlKnob.setValue(value);
    }

    public void setOscillatorAmplitude(double value) {
        amplitudeControlKnob.setValue(value);
    }

    public void setOscillatorPulseWidth(double value) {
        pulseWidthControlKnob.setValue(value);
    }


    public void setWaveForm() {// TODO Was ist phase kommt die nu raus oder nich?

    }


    public static void main(String[] args) throws InterruptedException {
        BasicOscillatorModule bom = new BasicOscillatorModule();

        AudioPlayer audioPlayer = new AudioPlayer(new Generator() {
            @Override
            public double next() {
                return bom.out();
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public Generator clone() {
                return null;
            }
        });

        audioPlayer.init();
        audioPlayer.start();
        Thread.sleep(2000);
        audioPlayer.stop();
    }

}
