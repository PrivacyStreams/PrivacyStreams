package io.github.privacystreams.core.transformations.filter;

import io.github.privacystreams.core.Item;

/**
 * Only keep the changed items in the stream.
 */
final class KeepChangesFilter extends StreamFilter {

    KeepChangesFilter() {
    }

    private transient Item lastItem;

    @Override
    public boolean keep(Item item) {
        if (item.equals(lastItem)) return false;
        this.lastItem = item;
        return true;
    }

}
