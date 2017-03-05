package com.github.privacystreams.accessibility;

import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.commons.common.ItemCommons;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_CHROME;
import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_FIREFOX;
import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_OPERA;
import static com.github.privacystreams.accessibility.utils.AppUtils.APP_PACKAGE_SEARCHBOX;

/**
 * Created by fanglinchen on 3/5/17.
 */

public class BrowserSearchUpdatesProvider extends MultiItemStreamProvider {

    @Override
    protected void provide() {
        getUQI().getDataItems(TextEntry.asUpdates(),
                Purpose.internal("Event Triggers"))
                .filter(ItemCommons.isFieldIn(BaseAccessibilityEvent.PACKAGE_NAME,
                        new String[]{APP_PACKAGE_SEARCHBOX, APP_PACKAGE_FIREFOX, APP_PACKAGE_OPERA, APP_PACKAGE_CHROME}))
                .forEach(new Callback<Item>() {

                    @Override
                    protected void onSuccess(Item input) {
                        output(new BrowserSearch(input.getValueByField(TextEntry.CONTENT).toString(),
                                System.currentTimeMillis()));
                    }
                });
    };
}
