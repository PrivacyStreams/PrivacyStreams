package io.github.privacystreams.audio;

import java.util.List;

import io.github.privacystreams.core.UQI;

public class ZeroCrossingRateWithFrameSize extends AudioProcessor<List<Double>>{
    int frameSize;
    ZeroCrossingRateWithFrameSize(String audioDataField, int samplesPerFrame) {
        super(audioDataField);
        frameSize = samplesPerFrame;
    }

    @Override
    protected List<Double> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getZeroCrossingRate(uqi, frameSize);
    }
}
