package com.github.privacystreams.core.items;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Assertions;

import java.util.List;


/**
 * Provide a live stream of EmptyItem
 */

class EmptyItemUpdatesProvider extends MStreamProvider {

    private final long interval;

    EmptyItemUpdatesProvider(long interval) {
        this.interval = interval;
        this.addParameters(interval);
    }

    @Override
    protected void provide() {
        while (!this.isCancelled) {
            this.output(new EmptyItem());
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.finish();
    }
}
