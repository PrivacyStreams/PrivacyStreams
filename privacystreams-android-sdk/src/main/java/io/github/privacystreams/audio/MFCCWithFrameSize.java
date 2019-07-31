package io.github.privacystreams.audio;

import java.util.List;

import io.github.privacystreams.core.UQI;

public class MFCCWithFrameSize extends AudioProcessor<List<Double[]>>{
    int frameSize;
    MFCCWithFrameSize(String audioDataField, int samplesPerFrame) {
        super(audioDataField);
        frameSize = samplesPerFrame;
    }

    @Override
    protected List<Double[]> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getMFCC(uqi, frameSize);
    }
}
