package com.github.privacystreams.google_awareness;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Created by lenovo on 2017/3/6.
 */

public class PhysicalActivity extends Item{
    /**
     * The timestamp of when a new type of physical activity is detected.
     */
    @PSItemField(type = Long.class)
    private static final String TIMESTAMP = "timestamp";

    /**
     * The type of the detected physical activity.
     */
    private static final String MOTIONTYPE ="motiontype";

    PhysicalActivity(long timestamp, String motionType){
        this.setFieldValue(TIMESTAMP, timestamp);                        //Assign value to each of the member variable
        this.setFieldValue(MOTIONTYPE, motionType);
    }

    public static MultiItemStreamProvider asUpdates() {
        return new PhysicalMotionUpdatesProvider();
    }
}
