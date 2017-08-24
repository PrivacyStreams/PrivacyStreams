package io.github.privacystreams.doclet;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSItemFieldDoc {

    FieldDoc fieldDoc;
    PSItemDoc psItemDoc;

    String reference;
    String name;
    Type type;
    String description;

    private PSItemFieldDoc(PSItemDoc psItemDoc, FieldDoc fieldDoc, AnnotationDesc annotation) {
        this.psItemDoc = psItemDoc;
        this.reference = fieldDoc.name();
        this.name = fieldDoc.constantValue().toString();
        this.description = fieldDoc.commentText().replace('\n', ' ');

        for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
            if ("type".equals(elementValuePair.element().name())) {
                Object typeValue = elementValuePair.value().value();
                this.type = (Type) typeValue;
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
        return "| `" + this.reference + "` | `" + Utils.getSimpleTypeName(this.type) + "` | " + this.description +  " |";
    }

}
