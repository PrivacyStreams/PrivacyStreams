package com.github.privacystreams.google_awareness;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A motion event generated with Google Awareness API
 * TODO clarify this class before making this public
 */

class AwarenessMotion extends Item{
    /**
     * The timestamp of the event
     */
    @PSItemField(type = Long.class)
    private static final String TIMESTAMP = "timestamp";

    /**
     * The motion type, which is the return value of google Awareness API `FenceState.getFenceKey()`
     */
    private static final String MOTION_TYPE ="motion_type";

    AwarenessMotion(long timestamp, String motionType){
        this.setFieldValue(TIMESTAMP, timestamp);                        //Assign value to each of the member variable
        this.setFieldValue(MOTION_TYPE, motionType);
    }

    /**
     * Provide a live stream of AwarenessMotion items.
     *
     * @return the function
     */
    // @RequiresPermission(value = "com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    public static MStreamProvider asUpdates() {
        return new AwarenessMotionUpdatesProvider();
    }
}
