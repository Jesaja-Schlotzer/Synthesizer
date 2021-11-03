package audio.enums;

public enum SampleRate {
    _8000(8000),
    _11025(11025),
    _16000(16000),
    _22050(22050),
    _44100(44100),
    _48000(48000),
    _88200(88200),
    _96000(96000),
    _192000(192000);


    private final double sampleRate;

    SampleRate(double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public double get() {
        return sampleRate;
    }
}
