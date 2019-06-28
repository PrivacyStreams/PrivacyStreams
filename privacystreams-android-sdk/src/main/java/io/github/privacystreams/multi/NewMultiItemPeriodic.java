package io.github.privacystreams.multi;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;

class NewMultiItemPeriodic extends PStreamProvider {
    private Long interval;
    private List<ItemType> itemTypes;
    private Timer timer;
    NewMultiItemPeriodic(List<ItemType> itemTypes, long interval) {
        this.interval = interval;
        this.itemTypes = itemTypes;
        this.timer = new Timer();
    }

    @Override
    protected void provide() {
        timer.scheduleAtFixedRate(new NewMultiItemTimerTask(this), 0, interval);
    }


    void recordOnce() {
        NewMultiItem multiItem = null;
        try {
            multiItem = NewMultiItemOnce.recordOnce(this.getUQI(), this.itemTypes);
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiRecorder failed."));
        }
        if (multiItem != null) this.output(multiItem);
    }

    @Override
    protected void onCancel(UQI uqi) {
        timer.cancel();
        super.onCancel(uqi);
    }
}

