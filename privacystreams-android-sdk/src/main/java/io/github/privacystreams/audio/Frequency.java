package io.github.privacystreams.audio;

import java.util.List;

import io.github.privacystreams.core.UQI;

public class Frequency extends AudioProcessor <List<Double>>{
    Frequency(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected List<Double> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getFrequency(uqi);
    }
}
