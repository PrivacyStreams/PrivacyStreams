package io.github.privacystreams.audio;

import android.Manifest;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.AlarmScheduler;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;

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

    private transient AlarmScheduler alarmScheduler;

    @Override
    protected void provide() {
        if (Globals.AudioConfig.useAlarmScheduler) {
            alarmScheduler = new AlarmScheduler(getContext(), this.getClass().getName()) {
                @Override
                protected void run() {
                    if (!isCancelled) {
                        recordOnce();
                        alarmScheduler.schedule(interval);
                    } else {
                        finish();
                    }
                }
            };
            alarmScheduler.schedule(0);
        } else {
            while (!this.isCancelled) {
                recordOnce();
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.finish();
        }
    }

    private void recordOnce() {
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
    }

    @Override
    protected void onCancel(UQI uqi) {
        if (Globals.AudioConfig.useAlarmScheduler) {
            try {
                alarmScheduler.destroy();
            } catch (Exception e) {
                Logging.error(e.getMessage());
            }
        }
        super.onCancel(uqi);
    }
}
