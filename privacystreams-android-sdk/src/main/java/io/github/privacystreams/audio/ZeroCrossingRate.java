package io.github.privacystreams.audio;

import java.util.List;

import io.github.privacystreams.core.UQI;

public class ZeroCrossingRate extends AudioProcessor<List<Double>>  {
    ZeroCrossingRate(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected List<Double> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getZCR(uqi);
    }

    protected List<Double> processAudio(UQI uqi, AudioData audioData, int frameSize) {
        //return audioData.getLoudness(uqi);
        return audioData.getZCR(uqi, frameSize);
    }
}
