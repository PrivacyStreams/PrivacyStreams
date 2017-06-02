package com.github.privacystreams.accessibility;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.AccessibilityUtils;
import com.github.privacystreams.utils.AppUtils;

import java.util.List;

/**
 * Provide a live stream of browser visit events.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class BrowserVisitStreamProvider extends MStreamProvider {
    private static String lastSavedUrl = null;
    private static String lastSavedUrlTitle = null;

    @Override
    protected void provide() {
        getUQI().getData(BaseAccessibilityEvent.asUpdates(), Purpose.LIB_INTERNAL("Event Triggers"))
                .filter(ItemOperators.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME, new String[]{AppUtils.APP_PACKAGE_FIREFOX, AppUtils.APP_PACKAGE_OPERA, AppUtils.APP_PACKAGE_CHROME}))
                .filter(Comparators.eq(BaseAccessibilityEvent.EVENT_TYPE, AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        AccessibilityNodeInfo rootView = input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        List<AccessibilityNodeInfo> nodeInfos = AccessibilityUtils.preOrderTraverse(rootView);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        String url = AccessibilityUtils.getBrowserCurrentUrl(rootView, packageName);
                        String title = AccessibilityUtils.getWebViewTitle(nodeInfos);
                        if(url!=null && title !=null
                                && !url.equals(lastSavedUrl)
                                && !title.equals(lastSavedUrlTitle)){
                            lastSavedUrl = url;
                            lastSavedUrlTitle = title;
                            output(new BrowserVisit(title,packageName, url, System.currentTimeMillis()));
                        }

                    }
                });
    }

}