package io.github.privacystreams.multi;

import java.io.IOException;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.AlarmScheduler;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;

import static io.github.privacystreams.utils.Globals.AudioConfig.useAlarmScheduler;

class NewMultiItemPeriodic extends PStreamProvider {
    private Long interval;
    private List<ItemType> itemTypes;
    NewMultiItemPeriodic(List<ItemType> itemTypes, long interval) {
        this.interval = interval;
        this.itemTypes = itemTypes;
    }

    private transient AlarmScheduler alarmScheduler;

    @Override
    protected void provide() {
        if (Globals.AudioConfig.useAlarmScheduler || Globals.ImageConfig.bgUseAlarmScheduler) {
            alarmScheduler = new AlarmScheduler(getContext(), this.getClass().getName()) {
                @Override
                protected void run() {
                    if (!isCancelled) {
                        recordOnce();
                        alarmScheduler.schedule(interval);
                    } else {
                        finish();
                    }
                }
            };
            alarmScheduler.schedule(0);
        } else {
            while (!this.isCancelled) {
                recordOnce();
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.finish();
        }
    }


    private void recordOnce() {
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
        if (useAlarmScheduler) {
            try {
                alarmScheduler.destroy();
            } catch (Exception e) {
                Logging.error(e.getMessage());
            }
        }
        super.onCancel(uqi);
    }
}

