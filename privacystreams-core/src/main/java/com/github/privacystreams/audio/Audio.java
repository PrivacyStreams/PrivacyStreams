package com.github.privacystreams.audio;

import android.net.Uri;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.SStream;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An audio record.
 */
@PSItem
public class Audio extends Item {

    /**
     * The timestamp of when current audio record is generated.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The URI of the audio file.
     */
    @PSItemField(type = String.class)
    public static final String AUDIO_URI = "audio_uri";

    Audio(long timestamp, Uri file_uri) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(AUDIO_URI, file_uri.toString());
    }

    /**
     * Provide an Audio item.
     * The audio is recorded from microphone for a certain duration of time.
     *
     * @param duration the time duration of audio.
     * @return the provider.
     */
    public static Function<Void, SStream> record(long duration) {
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
    public static Function<Void, MStream> recordPeriodic(long durationPerRecord, long interval) {
        return new AudioPeriodicRecorder(durationPerRecord, interval);
    }
}
