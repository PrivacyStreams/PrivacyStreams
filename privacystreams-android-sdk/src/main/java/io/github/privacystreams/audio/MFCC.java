package io.github.privacystreams.audio;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.UQI;

public class MFCC extends AudioProcessor<List<Double[]>> {

    MFCC(String audioDataField) {
        super(audioDataField);
    }

    @Override
    protected List<Double[]> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getMFCC(uqi);
    }

}
