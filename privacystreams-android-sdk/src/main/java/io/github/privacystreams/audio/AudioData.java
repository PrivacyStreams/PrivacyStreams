package io.github.privacystreams.audio;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.StatisticUtils;
import io.github.privacystreams.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An abstraction of audio data.
 */

public class AudioData {
    private final int type;
    private static final int TYPE_TEMP_RECORD = 0;
    private static final int TYPE_LOCAL_FILE = 1;
    private static final int TYPE_REMOTE_FILE = 1;
    private static List<Short> dataInShorts;

    private File audioFile;
    private List<Integer> amplitudeSamples;

    private final static double AMPLITUDE_BASE = 1.0;

    private AudioData(int type) {
        this.type = type;
    }


    static AudioData newTempRecord(List<Short> bytebuffer) {
        AudioData audioData = new AudioData(TYPE_TEMP_RECORD);
        dataInShorts = bytebuffer;
        return audioData;
    }

    static AudioData newLocalAudio(File localAudioFile) {
        AudioData audioData = new AudioData(TYPE_LOCAL_FILE);
        audioData.audioFile = localAudioFile;
        return audioData;
    }

    List<Integer> getAmplitudeSamples() {
        if (this.amplitudeSamples != null) return this.amplitudeSamples;
        // TODO get amplitude samples from local file.
        return new ArrayList<>();
    }

    List<Double[]> getMFCC(UQI uqi) {
        MFCCCalculator newMFCC = new MFCCCalculator(dataInShorts);
        newMFCC.Process();
        return newMFCC.result;
    }

    List<Double[]> getMFCC(UQI uqi, int framSize){
        MFCCCalculator newMFCC = new MFCCCalculator(dataInShorts, framSize);
        newMFCC.Process();
        return newMFCC.result;
    }

    List<Double> getZCR(UQI uqi){
        ZCRCalculator newZCR = new ZCRCalculator(dataInShorts);
        newZCR.Process();
        return newZCR.result;
    }

    List<Double> getZCR(UQI uqi, int frameSize){
        ZCRCalculator newZCR = new ZCRCalculator(dataInShorts, frameSize);
        newZCR.Process();
        return newZCR.result;
    }

    List<Double> getFrequency(UQI uqi){
        Pitch_YIN newFre = new Pitch_YIN(dataInShorts);
        newFre.Process();
        return newFre.result;
    }

    List<Double> getFrequency(UQI uqi, int framSize){
        Pitch_YIN newFre = new Pitch_YIN(dataInShorts, framSize);
        newFre.Process();
        return newFre.result;
    }


    String getFilepath(UQI uqi) {
        if (this.audioFile != null) return this.audioFile.getAbsolutePath();
        return null;
    }

    Integer getMaxAmplitude(UQI uqi) {
        LoudnessCalculator newLoud = new LoudnessCalculator(dataInShorts);
        newLoud.Process();
        return newLoud.maximumAmplitude;
    }

    Double getLoudness(UQI uqi) {
        LoudnessCalculator newLoud = new LoudnessCalculator(dataInShorts);
        newLoud.Process();
        return newLoud.averageLoudness;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.type == TYPE_TEMP_RECORD) {
            StorageUtils.safeDelete(this.audioFile);
        }
    }
    static Double convertAmplitudeToLoudness(UQI uqi, Number amplitude) {
        if (amplitude == null) return null;
        double loudness = 20 * Math.log10(amplitude.doubleValue() / AMPLITUDE_BASE);
        return loudness;
    }


    public String toString() {
        return String.format(Locale.getDefault(), "<Audio@%d%d>", this.type, this.hashCode());
    }

    boolean hasHumanVoice(UQI uqi) {
        // TODO implement this
        return false;
    }
}
