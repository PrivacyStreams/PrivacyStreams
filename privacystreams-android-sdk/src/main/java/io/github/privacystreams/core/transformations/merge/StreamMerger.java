package io.github.privacystreams.core.transformations.merge;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.PStreamTransformation;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;

import static io.github.privacystreams.utils.Assertions.notNull;

/**
 * Merge the stream with another stream.
 */
abstract class StreamMerger extends PStreamTransformation {

    private PStreamProvider anotherProvider;

    StreamMerger(PStreamProvider anotherProvider) {
        this.anotherProvider = notNull("anotherProvider", anotherProvider);
        this.addParameters(anotherProvider);
    }

    @Override
    protected void init() {
        super.init();
        this.getUQI().getData(this.anotherProvider, Purpose.LIB_INTERNAL("Merge streams"))
                .forEach(new Function<Item, Void>() {
                    @Override
                    public Void apply(UQI uqi, Item input) {
                        StreamMerger.this.onInputAnother(input);
                        return null;
                    }
                });
    }

    protected abstract void onInputAnother(Item item);
}
