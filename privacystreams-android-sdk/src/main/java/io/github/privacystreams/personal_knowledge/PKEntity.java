package io.github.privacystreams.personal_knowledge;

/**
 * An entity in personal knowledge graph.
 */

public class PKEntity {
    private String type;
    private String value;
    private String desc;

    private static final String TYPE_PEOPLE = "People";
    private static final String TYPE_PLACE = "Place";
    private static final String TYPE_ITEM = "Item";

    PKEntity(String type, String value, String desc) {
        this.type = type;
        this.value = value;
        this.desc = desc;
    }

    public static PKEntity newEntity(String type, String value, String desc) {
        return new PKEntity(type, value, desc);
    }

    public static PKEntity newPeople(String value, String desc) {
        return new PKEntity(TYPE_PEOPLE, value, desc);
    }

    public static PKEntity newPlace(String value, String desc) {
        return new PKEntity(TYPE_PLACE, value, desc);
    }

    public static PKEntity newItem(String value, String desc) {
        return new PKEntity(TYPE_ITEM, value, desc);
    }
}
