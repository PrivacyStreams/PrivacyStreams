package io.github.privacystreams.accessibility;

import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.AppUtils;

/**
 * Provide a live stream of browser search events.
 */

class BrowserSearchEventsProvider extends PStreamProvider {

    @Override
    protected void provide() {
        getUQI().getData(AccEvent.asTextEntries(), Purpose.LIB_INTERNAL("Event Triggers"))
                .filter(ItemOperators.isFieldIn(AccEvent.PACKAGE_NAME,
                        new String[]{
                                AppUtils.APP_PACKAGE_SEARCHBOX,
                                AppUtils.APP_PACKAGE_FIREFOX,
                                AppUtils.APP_PACKAGE_OPERA,
                                AppUtils.APP_PACKAGE_CHROME}))
                .forEach(new Callback<Item>() {
                    @Override
                    protected void onInput(Item input) {
                        output(new BrowserSearch(input.getValueByField(AccEvent.TEXT).toString()));
                    }
                });
    };
}
