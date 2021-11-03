package audio.modules;

import audio.components.combiners.Mixer;
import audio.components.oscillators.*;
import audio.enums.SampleRate;
import audio.enums.WaveForm;
import audio.modules.controls.Dial;
import audio.modules.controls.ModulatedControlKnob;
import audio.modules.controls.Mute;
import audio.modules.io.OutputPort;
import audio.modules.io.Port;

import java.util.HashMap;
import java.util.Map;


public class BasicOscillatorModule{

    public final ModulatedControlKnob frequencyControlKnob;
    public final ModulatedControlKnob amplitudeControlKnob;
    public final ModulatedControlKnob pulseWidthControlKnob;

    public final Dial<WaveForm> waveFormDial;

    private final Map<WaveForm, Mute> oscillatorMap = new HashMap<>();

    @OutputPort
    private final Port mainOutputPort;

    public Port getMainOutputPort() {
        return mainOutputPort;
    }


    public BasicOscillatorModule()  {
        frequencyControlKnob = new ModulatedControlKnob(() -> 440);
        amplitudeControlKnob = new ModulatedControlKnob(() -> 100);
        pulseWidthControlKnob = new ModulatedControlKnob(() -> 0.5);

        waveFormDial = new Dial<>(WaveForm.SINE, WaveForm.SQUARE, WaveForm.SAWTOOTH, WaveForm.TRIANGLE);

        SineOscillator sineOscillator = new SineOscillator(frequencyControlKnob.getOutputPort(), amplitudeControlKnob.getOutputPort(), SampleRate._44100);
        SquareOscillator squareOscillator = new SquareOscillator(frequencyControlKnob.getOutputPort(), amplitudeControlKnob.getOutputPort(),  SampleRate._44100, pulseWidthControlKnob.getOutputPort());
        SawtoothOscillator sawtoothOscillator = new SawtoothOscillator(frequencyControlKnob.getOutputPort(), amplitudeControlKnob.getOutputPort(),  SampleRate._44100);
        TriangleOscillator triangleOscillator = new TriangleOscillator(frequencyControlKnob.getOutputPort(), amplitudeControlKnob.getOutputPort(),  SampleRate._44100);

        Mute sineMute = new Mute(sineOscillator.getMainOutputPort());
        Mute squareMute = new Mute(squareOscillator.getMainOutputPort());
        Mute sawtoothMute = new Mute(sawtoothOscillator.getMainOutputPort());
        Mute triangleMute = new Mute(triangleOscillator.getMainOutputPort());

        oscillatorMap.put(WaveForm.SINE, sineMute);
        oscillatorMap.put(WaveForm.SQUARE, squareMute);
        oscillatorMap.put(WaveForm.SAWTOOTH, sawtoothMute);
        oscillatorMap.put(WaveForm.TRIANGLE, triangleMute);

        oscillatorMap.get(waveFormDial.getSelected()).unmute();

        Mixer mixer = new Mixer(sineMute.getOutputPort(), squareMute.getOutputPort(), sawtoothMute.getOutputPort(), triangleMute.getOutputPort());

        mainOutputPort = mixer.getMainOutputPort();
    }



    public void setFrequencyInput(Port frequencyInput) {
        frequencyControlKnob.setInputPort(frequencyInput);
    }

    public void setAmplitudeInput(Port amplitudeInput) {
        amplitudeControlKnob.setInputPort(amplitudeInput);
    }

    public void setPulseWidthInput(Port pulseWidthInput) {
        pulseWidthControlKnob.setInputPort(pulseWidthInput);
    }


    public void setWaveForm(WaveForm waveForm) {
        oscillatorMap.get(waveFormDial.getSelected()).mute();
        waveFormDial.select(waveForm);
        oscillatorMap.get(waveFormDial.getSelected()).unmute();
    }

}
