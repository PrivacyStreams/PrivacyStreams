package com.github.privacystreams.audio;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * record audio periodically with microphone
 */

class PeriodicAudioRecordProvider extends MultiItemStreamProvider {
    private final Long duration_per_record;
    private final Long interval;

    PeriodicAudioRecordProvider(long duration_per_record, long interval) {
        this.duration_per_record = duration_per_record;
        this.interval = interval;
    }

    @Override
    protected void provide(MultiItemStream output) {
        // TODO implement this
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(String.valueOf(duration_per_record));
        parameters.add(String.valueOf(interval));
        return parameters;
    }
}
