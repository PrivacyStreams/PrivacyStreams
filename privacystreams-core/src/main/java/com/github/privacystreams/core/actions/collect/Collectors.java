package com.github.privacystreams.core.actions.collect;

import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.SStream;
import com.github.privacystreams.core.actions.MStreamAction;
import com.github.privacystreams.core.actions.SStreamAction;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access stream collectors.
 */
@PSOperatorWrapper
public class Collectors {

    /**
     * Collect the items in the stream with a collector function, and handle the result with another function.
     *
     * @param itemsCollector the function to collect the items
     * @param resultHandler the function to handle result
     * @param <Tout> the output type of collector
     * @return the function
     */
    public static <Tout> MStreamAction collectItems(Function<List<Item>, Tout> itemsCollector,
                                                    Callback<Tout> resultHandler) {
        return new MStreamCollector<>(itemsCollector, resultHandler);
    }

    /**
     * Collect the items in the stream with a collector function.
     *
     * @param itemsCollector the function to collect the items
     * @return the function
     */
    public static MStreamAction collectItems(Function<List<Item>, Void> itemsCollector) {
        return new MStreamCollector<>(itemsCollector, null);
    }

    /**
     * Collect the item in the stream with a collector function, and handle the result with another function.
     *
     * @param itemCollector the function to collect the item
     * @param resultHandler the function to handle result
     * @param <Tout> the output type of collector
     * @return the function
     */
    public static <Tout> SStreamAction collectItem(Function<Item, Tout> itemCollector,
                                                   Callback<Tout> resultHandler) {
        return new SStreamCollector<>(itemCollector, resultHandler);
    }

    /**
     * Collect the item in the stream with a collector function.
     *
     * @param itemCollector the function to collect the item
     * @return the function
     */
    public static SStreamAction collectItem(Function<Item, Void> itemCollector) {
        return new SStreamCollector<>(itemCollector, null);
    }


    /**
     * Collect the MStream to a list of Items.
     * @return the function
     */
    public static Function<MStream, List<Item>> toItemList() {
        return new MStreamToItemListCollector();
    }

    /**
     * Collect the SStream to an Item.
     * @return the function
     */
    public static Function<SStream, Item> toItem() {
        return new SStreamToItemCollector();
    }
}
