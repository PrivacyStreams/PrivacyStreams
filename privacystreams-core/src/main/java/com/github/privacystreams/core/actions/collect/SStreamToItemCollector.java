package com.github.privacystreams.core.actions.collect;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.SStream;
import com.github.privacystreams.core.UQI;

import org.greenrobot.eventbus.Subscribe;

/**
 * A function that outputs a multi-item stream.
 */

class SStreamToItemCollector extends Function<SStream, Item> {

    SStreamToItemCollector() {
    }

    private transient Item item;
    private transient volatile boolean isFinished;

    @Override
    public Item apply(UQI uqi, SStream input) {
        this.item = null;
        this.isFinished = false;

        input.register(this);

        while (!this.isFinished && !this.isCancelled) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        input.unregister(this);
        return this.item;
    }

    @Subscribe
    public final void onEvent(Item item) {
        if (this.isCancelled) return;
        this.onInput(item);
    }

    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.isFinished = true;
        }
        else {
            this.item = item;
        }
    }
}
