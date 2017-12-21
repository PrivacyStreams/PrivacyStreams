package io.github.privacystreams.personal_knowledge;

import io.github.privacystreams.core.Item;

/**
 * An instance of personal knowledge.
 */

public class PKRelation extends Item {
    private PKEntity source;
    private PKEntity target;
    private String type;
    private String desc;

    PKRelation(PKEntity source, PKEntity target, String type) {

    }
}
