package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.oscillators.*;
import audio.enums.WaveForm;
import audio.modules.io.*;
import audio.modules.controls.*;

import java.util.HashMap;
import java.util.Map;

public class BasicOscillatorModule extends Module {

    public final ModulatedControlKnob frequencyControlKnob;
    public final ModulatedControlKnob amplitudeControlKnob;
    public final ModulatedControlKnob pulseWidthControlKnob;

    public final Dial<WaveForm> waveFormDial;


    private Map<WaveForm, Oscillator> oscillatorMap = new HashMap<>();



    @OutputPort
    private final Port mainOutput;

    public Port getMainOutput() {
        return mainOutput;
    }


    public BasicOscillatorModule()  {
        frequencyControlKnob = new ModulatedControlKnob(() -> 440);
        amplitudeControlKnob = new ModulatedControlKnob(() -> 100);
        pulseWidthControlKnob = new ModulatedControlKnob(() -> 0.5);
        waveFormDial = new Dial<>(WaveForm.SINE, WaveForm.SQUARE, WaveForm.SAWTOOTH, WaveForm.TRIANGLE);

        SineOscillator sineOscillator = new SineOscillator(frequencyControlKnob::getKnobRotation, amplitudeControlKnob::getKnobRotation,  44100);
        SquareOscillator squareOscillator = new SquareOscillator(frequencyControlKnob::getKnobRotation, amplitudeControlKnob::getKnobRotation,  44100, pulseWidthControlKnob::getKnobRotation);
        SawtoothOscillator sawtoothOscillator = new SawtoothOscillator(frequencyControlKnob::getKnobRotation, amplitudeControlKnob::getKnobRotation,  44100);
        TriangleOscillator triangleOscillator = new TriangleOscillator(frequencyControlKnob::getKnobRotation, amplitudeControlKnob::getKnobRotation,  44100);

        oscillatorMap.put(WaveForm.SINE, sineOscillator);
        oscillatorMap.put(WaveForm.SQUARE, squareOscillator);
        oscillatorMap.put(WaveForm.SAWTOOTH, sawtoothOscillator);
        oscillatorMap.put(WaveForm.TRIANGLE, triangleOscillator);

        Mixer mixer = new Mixer(sineOscillator.getMainOutput(), squareOscillator.getMainOutput(), sawtoothOscillator.getMainOutput(), triangleOscillator.getMainOutput());

        mainOutput = mixer.getMainOutputPort();
    }




    public void setFrequencyInput(Port frequencyInput) {
        frequencyControlKnob.setInput(frequencyInput);
    }


    public void setAmplitudeInput(Port amplitudeInput) {
        amplitudeControlKnob.setInput(amplitudeInput);
    }


    public void setPulseWidthInput(Port pulseWidthInput) {
        pulseWidthControlKnob.setInput(pulseWidthInput);
    }



    public void setWaveForm(WaveForm waveForm) {
        waveFormDial.select(waveForm);
    }

}
