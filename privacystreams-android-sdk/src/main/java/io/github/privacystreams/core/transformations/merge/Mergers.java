package io.github.privacystreams.core.transformations.merge;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.PStreamTransformation;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access merger functions
 */
@PSOperatorWrapper
public class Mergers {
    /**
     * Union the stream with another stream.
     *
     * @param anotherStreamProvider the provider of another stream.
     * @return the stream merger function.
     */
    public static PStreamTransformation union(PStreamProvider anotherStreamProvider) {
        return new UnionMerger(anotherStreamProvider);
    }
}
