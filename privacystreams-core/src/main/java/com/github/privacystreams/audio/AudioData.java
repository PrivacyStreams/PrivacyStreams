package com.github.privacystreams.audio;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * An abstraction of audio instance.
 */

public class AudioData {
    private int type;
    private static final int TYPE_TEMP_RECORD = 0;
    private static final int TYPE_LOCAL_FILE = 1;
    private static final int TYPE_REMOTE_FILE = 1;

    private File tempRecordFile;
    private List<Integer> amplitudeSamples;
    private double averageAmplitude;
    private double rmsAmplitude;
    private double maxAmplitude;

    private final static double AMPLITUDE_BASE = 1.0;

    AudioData(File tempRecordFile, List<Integer> amplitudeSamples) {
        this.type = TYPE_TEMP_RECORD;
        this.tempRecordFile = tempRecordFile;
        this.amplitudeSamples = amplitudeSamples;
        if (amplitudeSamples != null) this.processAmplitudes();
    }

    private void processAmplitudes() {
        maxAmplitude = 0;
        averageAmplitude = 0;
        rmsAmplitude = 0;

        int lengthOfAmplitudes = this.amplitudeSamples.size();
        if (lengthOfAmplitudes > 0) {
            int sumAmplitude = 0;
            int squareSumAmplitude = 0;

            for (Integer amplitude : this.amplitudeSamples) {
                if (amplitude > maxAmplitude) maxAmplitude = amplitude;
                sumAmplitude += amplitude;
                squareSumAmplitude += amplitude * amplitude;
            }

            averageAmplitude = (double) sumAmplitude / lengthOfAmplitudes;
            rmsAmplitude = Math.sqrt((double) squareSumAmplitude / lengthOfAmplitudes);
        }
    }

    double getLoudness() {
        return 20 * Math.log10(rmsAmplitude/AMPLITUDE_BASE);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.type == TYPE_TEMP_RECORD) {
            this.tempRecordFile.delete();
        }
    }
}
