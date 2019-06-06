package io.github.privacystreams.multi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.location.Geolocation;
import io.github.privacystreams.utils.AlarmScheduler;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;

class MultiItemPeriodic extends PStreamProvider {
    private long duration = 1000;
    private long interval;
    private List<MultiItem.ItemType> item_types;
    private List<Object> items = new ArrayList<>();
    private List<Purpose> purposes;
    private int limit = -1;
    private final int asUpdatesDelay = 1000;
    private int cameraId = 0;
    private int mask = 0;
    private long afterTime = 0;
    private long beforeTime = 0;
    private String level = Geolocation.LEVEL_EXACT;

    MultiItemPeriodic(List<MultiItem.ItemType> item_types, List<Purpose> purposes, long interval){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
        this.interval = interval;
        this.addParameters(interval);
    }

    MultiItemPeriodic(List<MultiItem.ItemType> item_types, List<Purpose> purposes, long interval, long duration, int limit){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
        this.interval = interval;
        this.addParameters(interval);
        if(duration > 0) {
            this.duration = duration;
            this.addParameters(duration);
        }
        this.duration = duration;
        this.addParameters(duration);
        if(limit > 0) {
            this.limit = limit;
            this.addParameters(limit);
        }
    }
    MultiItemPeriodic(List<MultiItem.ItemType> item_types, List<Purpose> purposes, long interval, long duration, int limit,
                  int cameraId, int mask, String level, long afterTime, long beforeTime){
        this.item_types = item_types;
        this.addParameters(item_types);
        this.purposes = purposes;
        this.addParameters(purposes);
        this.duration = duration;
        this.addParameters(duration);
        this.duration = duration;
        this.addParameters(duration);
        this.interval = interval;
        this.addParameters(interval);
        this.limit = limit;
        this.addParameters(limit);
        this.cameraId = cameraId;
        this.addParameters(cameraId);
        this.mask = mask;
        this.addParameters(mask);
        this.level = level;
        this.addParameters(level);
        this.afterTime = afterTime;
        this.addParameters(afterTime);
        this.beforeTime = beforeTime;
        this.addParameters(beforeTime);
    }

    private transient AlarmScheduler alarmScheduler;

    @Override
    protected void provide() {
        if (Globals.AudioConfig.useAlarmScheduler) {
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
        MultiItem multiItem = null;
        try {
            //HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            multiItem = (MultiItem) this.getUQI().getData(MultiItem.oneshot(item_types, purposes, duration, limit, cameraId,
                    mask, level, afterTime, beforeTime), Purpose.TEST("multi_item")).getFirst();
        } catch (Exception e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("MultiItemPeriodic failed. Perhaps the duration is too short."));
        }
        if (multiItem != null) this.output(multiItem);
    }

    @Override
    protected void onCancel(UQI uqi) {
        if (Globals.AudioConfig.useAlarmScheduler) {
            try {
                alarmScheduler.destroy();
            } catch (Exception e) {
                Logging.error(e.getMessage());
            }
        }
        super.onCancel(uqi);
    }
}
