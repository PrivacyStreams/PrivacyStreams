package com.github.privacystreams.utils.annotations;

import java.lang.annotation.Documented;

/**
 * An annotation representing a transformation function.
 */
@Documented
public @interface PSTransformation {
    boolean changeOrder() default false;
}
