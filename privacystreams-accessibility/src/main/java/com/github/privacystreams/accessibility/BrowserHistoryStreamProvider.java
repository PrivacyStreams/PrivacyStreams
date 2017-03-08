package com.github.privacystreams.accessibility;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.privacystreams.accessibility.utils.AccessibilityUtils;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.commons.item.Items;
import com.github.privacystreams.commons.comparison.Comparisons;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.List;

import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_CHROME;
import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_FIREFOX;
import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_OPERA;

/**
 * Created by fanglinchen on 2/2/17.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BrowserHistoryStreamProvider extends MultiItemStreamProvider {
    public static String lastSavedUrl = null;
    public static String lastSavedUrlTitle = null;

    @Override
    protected void provide() {
        getUQI().getDataItems(BaseAccessibilityEvent.asUpdates(),
                Purpose.internal("Event Triggers"))
                .filter(Items.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{APP_PACKAGE_FIREFOX, APP_PACKAGE_OPERA, APP_PACKAGE_CHROME}))
                .filter(Comparisons.eq(BaseAccessibilityEvent.EVENT_TYPE,
                        AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onSuccess(Item input) {
                        AccessibilityNodeInfo rootView = input.getValueByField(BaseAccessibilityEvent.ROOT_VIEW);
                        List<AccessibilityNodeInfo> nodeInfos = input.getValueByField(BaseAccessibilityEvent.UI_NODE_LIST);
                        String packageName = input.getValueByField(BaseAccessibilityEvent.PACKAGE_NAME);
                        String url = AccessibilityUtils.getBrowserCurrentUrl(rootView, packageName);
                        String title = AccessibilityUtils.getWebViewTitle(nodeInfos);
                        if(url!=null && title !=null
                                && !url.equals(lastSavedUrl)
                                && !title.equals(lastSavedUrl)){
                            lastSavedUrl = url;
                            lastSavedUrlTitle = title;
                            output(new BrowserHistory(title,packageName,url, System.currentTimeMillis()));
                        }

                    }
                });
    }

}