package io.github.privacystreams.audio;

import java.util.List;

import io.github.privacystreams.core.UQI;

public class MFCCWithParameters extends AudioProcessor<List<Double[]>>{
    int frameSize;
    int numberOfFilters;
    int numberOfCoefficients;
    MFCCWithParameters(String audioDataField, int samplesPerFrame, int MelFilters, int CepstrumCoe) {
        super(audioDataField);
        frameSize = samplesPerFrame;
        numberOfCoefficients =  CepstrumCoe;
        numberOfFilters = MelFilters;
    }

    @Override
    protected List<Double[]> processAudio(UQI uqi, AudioData audioData) {
        //return audioData.getLoudness(uqi);
        return audioData.getMFCC(uqi, frameSize, numberOfFilters, numberOfCoefficients);
    }
}
