package com.github.privacystreams.audio;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An Audio item represents an audio, could be an audio record from microphone,
 * an audio file from storage, etc.
 */
@PSItem
public class Audio extends Item {

    /**
     * The timestamp of when the audio/record was generated.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The abstraction of audio data.
     * The value is an `AudioData` instance.
     */
    @PSItemField(type = AudioData.class)
    public static final String AUDIO_DATA = "audio_data";

    Audio(long timestamp, AudioData audioData) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(AUDIO_DATA, audioData);
    }

    /**
     * Provide an Audio item.
     * The audio is recorded from microphone for a certain duration of time.
     *
     * @param duration the time duration of audio.
     * @return the provider.
     */
    // @RequiresPermission(value = Manifest.permission.RECORD_AUDIO)
    public static SStreamProvider record(long duration) {
        return new AudioRecorder(duration);
    }

    /**
     * Provide a live stream of Audio items.
     * The audios are recorded from microphone periodically every certain time interval,
     * and each Audio item is a certain duration of time long.
     * For example, <code>recordPeriodic(1000, 4000)</code> will record audio from 0s-1s, 5s-6s, 10s-11s, ...
     *
     * @param durationPerRecord the time duration of each audio record, in milliseconds.
     * @param interval the time interval between each two records, in milliseconds.
     * @return the provider
     */
    // @RequiresPermission(value = Manifest.permission.RECORD_AUDIO)
    public static MStreamProvider recordPeriodic(long durationPerRecord, long interval) {
        return new AudioPeriodicRecorder(durationPerRecord, interval);
    }

    /**
     * Provide all Audio items in local file system.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static MStreamProvider getFromStorage() {
        return new AudioStorageProvider();
    }
}
