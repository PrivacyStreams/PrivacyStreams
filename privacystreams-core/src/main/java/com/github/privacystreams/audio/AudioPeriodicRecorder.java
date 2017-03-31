package com.github.privacystreams.audio;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Record audio periodically with the microphone.
 */

class AudioPeriodicRecorder extends MStreamProvider {
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
            Audio audioItem = AudioRecorder.recordAudio(this.getUQI(), this.durationPerRecord);
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
