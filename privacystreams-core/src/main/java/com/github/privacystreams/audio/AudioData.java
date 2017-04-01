package com.github.privacystreams.audio;

import java.io.File;
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

    private File audioFile;
    private List<Integer> amplitudeSamples;
    private double averageAmplitude;
    private double rmsAmplitude;
    private double maxAmplitude;

    private final static double AMPLITUDE_BASE = 1.0;

    private AudioData(int type) {
        this.type = type;
    }

    static AudioData newTempRecord(File tempRecordFile, List<Integer> amplitudeSamples) {
        AudioData audioData = new AudioData(TYPE_TEMP_RECORD);
        audioData.audioFile = tempRecordFile;
        audioData.amplitudeSamples = amplitudeSamples;
        if (amplitudeSamples != null) audioData.processAmplitudes();
        return audioData;
    }

    static AudioData newLocalAudio(File localAudioFile) {
        AudioData audioData = new AudioData(TYPE_LOCAL_FILE);
        audioData.audioFile = localAudioFile;
        return audioData;
    }

    private void processAmplitudes() {
        maxAmplitude = 0;
        averageAmplitude = 0;
        rmsAmplitude = 0;

        int lengthOfAmplitudes = this.amplitudeSamples.size();
        if (lengthOfAmplitudes > 0) {

            for (Integer amplitude : this.amplitudeSamples) {
                if (amplitude > maxAmplitude) maxAmplitude = amplitude;
                averageAmplitude += amplitude / lengthOfAmplitudes;
                rmsAmplitude += amplitude / lengthOfAmplitudes;
            }
        }
    }

    double getLoudness() {
        return 20 * Math.log10(rmsAmplitude/AMPLITUDE_BASE);
    }

    double getPeakLoudness() {
        return 20 * Math.log10(maxAmplitude/AMPLITUDE_BASE);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.type == TYPE_TEMP_RECORD) {
            this.audioFile.deleteOnExit();
        }
    }

    public String toString() {
        if (this.type == TYPE_TEMP_RECORD) {
            return String.format(Locale.getDefault(), "<Audio@temp%d>", this.hashCode());
        }
        else if (this.type == TYPE_LOCAL_FILE) {
            return String.format(Locale.getDefault(), "<Audio@local%d>", this.hashCode());
        }
        else {
            return String.format(Locale.getDefault(), "<Audio@%d>", this.hashCode());
        }
    }
}
