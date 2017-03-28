package com.github.privacystreams.audio;

import android.Manifest;
import android.media.MediaRecorder;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.GlobalConfig;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.StorageUtils;
import com.github.privacystreams.utils.time.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Record audio for a duration from microphone
 */

class AudioRecorder extends SStreamProvider {
    private final Long duration;

    AudioRecorder(long duration) {
        this.duration = duration;
        this.addParameters(duration);
        this.addRequiredPermissions(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void provide() {
        Audio audioItem = recordAudio(this.getUQI(), this.duration);

        if (audioItem != null)
            this.output(audioItem);

        this.finish();
    }

    static Audio recordAudio(UQI uqi, long duration) {
        try {
            List<Integer> amplitudes = new ArrayList<>();

            // TODO test this
            MediaRecorder recorder = new MediaRecorder();
            recorder.setAudioSource(GlobalConfig.AudioConfig.audioSource);
            recorder.setOutputFormat(GlobalConfig.AudioConfig.outputFormat);
            recorder.setAudioEncoder(GlobalConfig.AudioConfig.audioEncoder);

            String audioPath = "temp/audio_" + TimeUtils.getTimeTag() + ".3gp";
            File tempAudioFile = StorageUtils.getValidFile(uqi.getContext(), audioPath, false);
            recorder.setOutputFile(tempAudioFile.getAbsolutePath());

            recorder.prepare();

            long startTime = System.currentTimeMillis();
            recorder.start();   // Recording is now started

            while (true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime > duration) {
                    break;
                }
                amplitudes.add(recorder.getMaxAmplitude());
                Thread.sleep(GlobalConfig.AudioConfig.amplitudeSamplingRate);
            }

            recorder.stop();
            recorder.reset();   // You can reuse the object by going back to setAudioSource() step
            recorder.release(); // Now the object cannot be reused

            AudioData audioData = new AudioData(tempAudioFile, amplitudes);

            return new Audio(startTime, audioData);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            Logging.warn("AudioRecorder failed to record audio.");
        }
        return null;
    }
}
