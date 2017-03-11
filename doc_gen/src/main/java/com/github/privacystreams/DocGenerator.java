package com.github.privacystreams;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.formats.html.HtmlDoclet;
import com.sun.tools.javac.code.Attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class DocGenerator extends HtmlDoclet {
    private static final String ITEM_ANNOTATION = "com.github.privacystreams.utils.annotations.PSItem";
    private static final String OPERATOR_WRAPPER_ANNOTATION = "com.github.privacystreams.utils.annotations.PSOperatorWrapper";
    private static final String ITEM_FIELD_ANNOTATION = "com.github.privacystreams.utils.annotations.PSItemField";

    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();

        List<ClassDoc> itemClasses = new ArrayList<>();
        List<ClassDoc> operatorWrappers = new ArrayList<>();

        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];

            AnnotationDesc[] annotations = classDoc.annotations();
            for (AnnotationDesc annotation : annotations) {
                AnnotationTypeDoc annotationType = annotation.annotationType();
                if (ITEM_ANNOTATION.equals(annotationType.toString())) {
                    itemClasses.add(classDoc);
                    System.out.println(genItemDocs(classDoc));
                    break;
                }
                if (OPERATOR_WRAPPER_ANNOTATION.equals(annotationType.toString())) {
                    operatorWrappers.add(classDoc);
                    break;
                }
            }

        }

//        System.out.println(itemClasses);
//        System.out.println(operatorWrappers);

        return true;
    }

    public static String genItemDocs(ClassDoc itemClass) {
        String itemDoc = "";
        itemDoc += "Class: " + itemClass.name() + "\n";
        itemDoc += "Description: " + itemClass.commentText() + "\n";
        itemDoc += "Fields:\n";
        for (FieldDoc fieldDoc : itemClass.fields()) {
            AnnotationDesc[] annotations = fieldDoc.annotations();
            for (AnnotationDesc annotation : annotations) {
                AnnotationTypeDoc annotationType = annotation.annotationType();
                if (ITEM_FIELD_ANNOTATION.equals(annotationType.toString())) {
                    itemDoc += fieldDoc.commentText() + "\n";
                    itemDoc += fieldDoc.name() + " " + fieldDoc.constantValue() + "\n";
                    for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
                        itemDoc += elementValuePair.element().name() + " " + elementValuePair.value() + "\n";
                    }

                    break;
                }
            }
        }
        return itemDoc;
    }
}
