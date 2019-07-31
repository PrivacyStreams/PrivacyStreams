package io.github.privacystreams.multi;

import java.util.Timer;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;

public class MultiItemPeriodic extends PStreamProvider {
    private FeatureProvider[] features;
    private long interval;
    private Timer timer;

    MultiItemPeriodic(long interval, FeatureProvider[] features){
        this.interval = interval;
        this.features = features;
        this.timer = new Timer();
    }


    @Override
    protected void provide() {
        timer.scheduleAtFixedRate(new MultiItemTimerTask(this), 0, interval);
    }


    void recordOnce() {
        MultiItem multiItem = null;
        try {
            multiItem = MultiItemOnce.recordOnce(this.getUQI(), this.features);
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
