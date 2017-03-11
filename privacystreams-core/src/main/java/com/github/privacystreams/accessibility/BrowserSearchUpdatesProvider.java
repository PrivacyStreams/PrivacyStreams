package com.github.privacystreams.accessibility;

import com.github.privacystreams.commons.item.Items;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.AppUtils;

/**
 * Created by fanglinchen on 3/5/17.
 */

public class BrowserSearchUpdatesProvider extends MultiItemStreamProvider {

    @Override
    protected void provide() {
        getUQI().getDataItems(TextEntry.asUpdates(), Purpose.internal("Event Triggers"))
                .filter(Items.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{AppUtils.APP_PACKAGE_SEARCHBOX, AppUtils.APP_PACKAGE_FIREFOX, AppUtils.APP_PACKAGE_OPERA, AppUtils.APP_PACKAGE_CHROME}))
                .forEach(new Callback<Item>() {

                    @Override
                    protected void onSuccess(Item input) {
                        output(new BrowserSearch(input.getValueByField(TextEntry.CONTENT).toString(),
                                System.currentTimeMillis()));
                    }
                });
    };
}
