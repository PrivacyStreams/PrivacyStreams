package io.github.privacystreams.accessibility;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.AccessibilityUtils;
import io.github.privacystreams.utils.AppUtils;

import java.util.List;

/**
 * Provide a live stream of browser visit events.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class BrowserVisitEventsProvider extends PStreamProvider {
    private static String lastSavedUrl = null;
    private static String lastSavedUrlTitle = null;

    @Override
    protected void provide() {
        getUQI().getData(AccEvent.asWindowChanges(), Purpose.LIB_INTERNAL("Event Triggers"))
                .filter(ItemOperators.isFieldIn(AccEvent.PACKAGE_NAME, new String[]{AppUtils.APP_PACKAGE_FIREFOX, AppUtils.APP_PACKAGE_OPERA, AppUtils.APP_PACKAGE_CHROME}))
                .filter(Comparators.eq(AccEvent.EVENT_TYPE, AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        AccessibilityNodeInfo rootNode = input.getValueByField(AccEvent.ROOT_NODE);
                        List<AccessibilityNodeInfo> nodeInfos = AccessibilityUtils.preOrderTraverse(rootNode);
                        String packageName = input.getValueByField(AccEvent.PACKAGE_NAME);
                        String url = AccessibilityUtils.getBrowserCurrentUrl(rootNode, packageName);
                        String title = AccessibilityUtils.getWebViewTitle(nodeInfos);
                        if(url!=null && title !=null
                                && !url.equals(lastSavedUrl)
                                && !title.equals(lastSavedUrlTitle)){
                            lastSavedUrl = url;
                            lastSavedUrlTitle = title;
                            output(new BrowserVisit(title, packageName, url));
                        }

                    }
                });
    }

}