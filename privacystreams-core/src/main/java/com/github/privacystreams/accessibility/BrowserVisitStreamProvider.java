package com.github.privacystreams.accessibility;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.AccessibilityUtils;
import com.github.privacystreams.utils.AppUtils;

import java.util.List;

/**
 * Created by fanglinchen on 2/2/17.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class BrowserVisitStreamProvider extends MultiItemStreamProvider {
    private static String lastSavedUrl = null;
    private static String lastSavedUrlTitle = null;

    @Override
    protected void provide() {
        getUQI().getDataItems(BaseAccessibilityEvent.asUpdates(), Purpose.internal("Event Triggers"))
                .filter(ItemOperators.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME, new String[]{AppUtils.APP_PACKAGE_FIREFOX, AppUtils.APP_PACKAGE_OPERA, AppUtils.APP_PACKAGE_CHROME}))
                .filter(Comparators.eq(BaseAccessibilityEvent.EVENT_TYPE, AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onSuccess(Item input) {
                        AccessibilityNodeInfo rootView = input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        List<AccessibilityNodeInfo> nodeInfos = AccessibilityUtils.preOrderTraverse(rootView);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        String url = AccessibilityUtils.getBrowserCurrentUrl(rootView, packageName);
                        String title = AccessibilityUtils.getWebViewTitle(nodeInfos);
                        if(url!=null && title !=null
                                && !url.equals(lastSavedUrl)
                                && !title.equals(lastSavedUrl)){
                            lastSavedUrl = url;
                            lastSavedUrlTitle = title;
                            output(new BrowserVisit(title,packageName, url, System.currentTimeMillis()));
                        }

                    }
                });
    }

}