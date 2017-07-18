package io.github.privacystreams.audio;

import android.Manifest;

import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.providers.PStreamProvider;

import java.io.IOException;

/**
 * Record audio periodically with the microphone.
 */

class AudioPeriodicRecorder extends PStreamProvider {
    private final Long durationPerRecord;
    private final Long interval;

    AudioPeriodicRecorder(long durationPerRecord, long interval) {
        this.durationPerRecord = durationPerRecord;
        this.interval = interval;
        this.addParameters(durationPerRecord, interval);
        this.addRequiredPermissions(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void provide() {
        while (!this.isCancelled) {
            Audio audioItem = null;
            try {
                audioItem = AudioRecorder.recordAudio(this.getUQI(), this.durationPerRecord);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
                this.raiseException(this.getUQI(), PSException.INTERRUPTED("AudioPeriodicRecorder failed. Perhaps the audio duration is too short."));
            }
            if (audioItem != null) this.output(audioItem);
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.finish();
    }

}
