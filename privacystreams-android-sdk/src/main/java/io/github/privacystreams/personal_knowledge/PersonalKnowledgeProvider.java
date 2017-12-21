package io.github.privacystreams.personal_knowledge;

import android.os.Build;

import io.github.privacystreams.accessibility.AccEvent;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.notification.Notification;
import io.github.privacystreams.utils.PermissionUtils;

/**
 * Created by ylimit on 17-12-19.
 * Provide a stream of personal knowledge
 */

class PersonalKnowledgeProvider extends PStreamProvider {

    /**
     * Interval of AccessibilityEvent and Notification sampling.
     */
    private static final long INTERVAL = 1000;

    PersonalKnowledgeProvider() {
        this.addRequiredPermissions(
                PermissionUtils.USE_ACCESSIBILITY_SERVICE,
                PermissionUtils.USE_NOTIFICATION_SERVICE);
    }

    @Override
    protected void provide() {
        Purpose myPurpose = Purpose.LIB_INTERNAL("Extract personal knowledge.");

        this.getUQI().getData(AccEvent.asUIActions(), myPurpose)
                .sampleByInterval(INTERVAL)
                .debug();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getUQI().getData(Notification.asUpdates(), myPurpose)
                    .sampleByInterval(INTERVAL)
                    .debug();
        }
    }
}
