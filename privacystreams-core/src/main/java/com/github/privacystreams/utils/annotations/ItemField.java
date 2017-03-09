package com.github.privacystreams.utils.annotations;

import java.lang.annotation.Documented;

/**
 * An annotation representing a item field
 */
@Documented
public @interface ItemField {
    String name();
    Class type();
    String description();
}
