package io.github.privacystreams.audio;

import android.Manifest;
import android.media.MediaRecorder;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.StorageUtils;
import io.github.privacystreams.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Record audio for a duration from microphone
 */

class AudioRecorder extends PStreamProvider {
    private final Long duration;

    AudioRecorder(long duration) {
        this.duration = duration;
        this.addParameters(duration);
        this.addRequiredPermissions(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void provide() {
        Audio audioItem = null;
        try {
            audioItem = recordAudio(this.getUQI(), this.duration);
            this.output(audioItem);
        } catch (IOException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("AudioRecorder failed."));
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("AudioRecorder failed. Perhaps the audio duration is too short."));
        }
        this.finish();
    }

    static Audio recordAudio(UQI uqi, long duration) throws IOException {
        List<Integer> amplitudes = new ArrayList<>();

        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(Globals.AudioConfig.audioSource);
        recorder.setOutputFormat(Globals.AudioConfig.outputFormat);
        recorder.setAudioEncoder(Globals.AudioConfig.audioEncoder);

        String audioPath = "temp/audio_" + TimeUtils.getTimeTag() + ".amr";
        File tempAudioFile = StorageUtils.getValidFile(uqi.getContext(), audioPath, false);
        recorder.setOutputFile(tempAudioFile.getAbsolutePath());

        recorder.prepare();
        recorder.start();   // Recording is now started

        long startTime = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > duration) {
                break;
            }
            amplitudes.add(recorder.getMaxAmplitude());
        }

        recorder.stop();
        recorder.reset();   // You can reuse the object by going back to setAudioSource() step
        recorder.release(); // Now the object cannot be reused

        AudioData audioData = AudioData.newTempRecord(tempAudioFile, amplitudes);

        return new Audio(startTime, audioData);
    }
}
