package com.github.privacystreams.audio;

import com.github.privacystreams.core.UQI;

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
    private Double averageAmplitude;
    private Double rmsAmplitude;
    private Integer maxAmplitude;

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

    private List<Integer> getAmplitudeSamples() {
        if (this.amplitudeSamples != null) return this.amplitudeSamples;
        // TODO get amplitude samples from local file.
        return null;
    }

    private void processAmplitudes() {
        if (this.getAmplitudeSamples() == null) return;

        maxAmplitude = 0;
        averageAmplitude = 0.0;
        rmsAmplitude = 0.0;

        int lengthOfAmplitudes = this.amplitudeSamples.size();
        if (lengthOfAmplitudes > 0) {
            for (Integer amplitude : this.amplitudeSamples) {
                if (amplitude > maxAmplitude) maxAmplitude = amplitude;
                averageAmplitude += amplitude.doubleValue() / lengthOfAmplitudes;
                rmsAmplitude += amplitude.doubleValue() / lengthOfAmplitudes;
            }
        }
    }

    String getFilepath(UQI uqi) {
        if (this.audioFile != null) return this.audioFile.getAbsolutePath();
        return null;
    }

    Double getRmsAmplitude(UQI uqi) {
        if (this.rmsAmplitude == null) {
            this.processAmplitudes();
        }
        return this.rmsAmplitude;
    }

    Integer getMaxAmplitude(UQI uqi) {
        if (this.maxAmplitude == null) {
            this.processAmplitudes();
        }
        return this.maxAmplitude;
    }

    Double getAverageAmplitude(UQI uqi) {
        if (this.averageAmplitude == null) {
            this.processAmplitudes();
        }
        return this.averageAmplitude;
    }

    Double getLoudness(UQI uqi) {
        if (this.getRmsAmplitude(uqi) != null) {
            return 20 * Math.log10(this.getRmsAmplitude(uqi)/AMPLITUDE_BASE);
        }
        return null;
    }

    Double getPeakLoudness(UQI uqi) {
        if (this.getMaxAmplitude(uqi) != null) {
            return 20 * Math.log10(this.getMaxAmplitude(uqi).doubleValue()/AMPLITUDE_BASE);
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.type == TYPE_TEMP_RECORD) {
            this.audioFile.deleteOnExit();
        }
    }

    public String toString() {
        return String.format(Locale.getDefault(), "<Audio@%d%d>", this.type, this.hashCode());
    }
}
