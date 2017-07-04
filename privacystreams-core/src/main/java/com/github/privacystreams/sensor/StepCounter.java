package com.github.privacystreams.sensor;

import android.os.Build;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;
import com.google.api.client.googleapis.util.Utils;

/**
 * Step counter.
 */
@PSItem
public class StepCounter extends Item {

    /**
     * Rotation vector component along the x axis (x * sin(θ/2)).
     */
    @PSItemField(type = Float.class)
    public static final String STEPS = "steps";

    StepCounter(float steps) {
        this.setFieldValue(STEPS, steps);
    }

    /**
     * Provide a live stream of sensor readings from the step counter.
     * @return the provider.
     */
    public static MStreamProvider asUpdates(int sensorDelay){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new StepCounterUpdatesProvider(sensorDelay);
        }
        else {
            Logging.warn("Step counter requires SDK version above 19.");
            return null;
        }
    }
}
