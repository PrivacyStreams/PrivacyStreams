package com.github.privacystreams.audio;

import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * record audio with microphone
 */

class AudioRecorder extends SingleItemStreamProvider {
    private final Long duration;

    AudioRecorder(long duration) {
        this.duration = duration;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(String.valueOf(duration));
        return parameters;
    }

    @Override
    protected void provide(SingleItemStream output) {
        // TODO implement this
    }
}
