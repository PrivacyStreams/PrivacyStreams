package io.github.privacystreams.machine_learning;

import java.util.List;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

abstract class MLProcessor<Tout> extends ItemOperator<Tout>{
    protected final List<String> inputFields;

    MLProcessor(List<String> inputFields) {
        this.inputFields = Assertions.notNull("inputFields", inputFields);
        this.addParameters(inputFields);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        return this.infer(uqi, input);
    }

    protected abstract Tout infer(UQI uqi, Item input);
}
