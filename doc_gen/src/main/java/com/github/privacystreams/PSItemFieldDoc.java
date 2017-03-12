package com.github.privacystreams;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.FieldDoc;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSItemFieldDoc {

    FieldDoc fieldDoc;
    PSItemDoc psItemDoc;

    String reference;
    String name;
    String type;
    String description;

    private PSItemFieldDoc(PSItemDoc psItemDoc, FieldDoc fieldDoc, AnnotationDesc annotation) {
        this.psItemDoc = psItemDoc;
        this.reference = psItemDoc.name + "." + fieldDoc.name();
        this.name = fieldDoc.constantValue().toString();
        this.description = fieldDoc.commentText().replace('\n', ' ');

        for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
            if ("type".equals(elementValuePair.element().name())) {
                this.type = elementValuePair.value().value().toString();
            }
        }
    }

    public static PSItemFieldDoc build(PSItemDoc psItemDoc, FieldDoc fieldDoc) {
        AnnotationDesc[] annotations = fieldDoc.annotations();
        for (AnnotationDesc annotation : annotations) {
            AnnotationTypeDoc annotationType = annotation.annotationType();
            if (Consts.ITEM_FIELD_ANNOTATION.equals(annotationType.toString())) {
                return new PSItemFieldDoc(psItemDoc, fieldDoc, annotation);
            }
        }
        return null;
    }

    public String toString() {
        return "| `" + this.reference + "` | " + this.name + " | " + this.type + " | " + this.description +  " |";
    }

}
