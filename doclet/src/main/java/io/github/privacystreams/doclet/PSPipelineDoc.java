package io.github.privacystreams.doclet;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

import java.util.Locale;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSPipelineDoc {

    public static final int TYPE_TRANSFORMATION = 1;
    public static final int TYPE_ACTION = 2;

    int type;

    ClassDoc declaringClassDoc;
    MethodDoc methodDoc;
    String description;

    String shortSignature;
//    String completeSignature;
    String tag;

    Type returnType;
    Type inputType;
    Type outputType;

    private PSPipelineDoc(ClassDoc classDoc, MethodDoc methodDoc, AnnotationDesc annotation, int type) {
        this.declaringClassDoc = classDoc;
        this.methodDoc = methodDoc;
        this.description = methodDoc.commentText().replace('\n', ' ');;
        Tag[] paramTags = methodDoc.tags("param");
        for (Tag paramTag : paramTags) {
            String paraStr = paramTag.text();
            String paraName = paraStr.substring(0, paraStr.indexOf(' ')).replace('\n', ' ');;
            String paraDesc = paraStr.substring(paraStr.indexOf(' ') + 1).replace('\n', ' ');;
            this.description += "<br> - `" + paraName + "`: " + paraDesc;
        }

        this.returnType = methodDoc.returnType();

        String shortSignature = methodDoc.name() + "(";
        boolean firstParameter = true;
        for (Parameter parameter : methodDoc.parameters()) {
            if (firstParameter) {
                shortSignature += Utils.getSimpleTypeName(parameter.type()) + " " + parameter.name();
                firstParameter = false;
            }
            else {
                shortSignature += ", " + Utils.getSimpleTypeName(parameter.type()) + " " + parameter.name();
            }
        }
        shortSignature += ")";
        this.shortSignature = shortSignature;

        this.type = type;

        if (type == TYPE_TRANSFORMATION) {
            this.tag = "Keep order";
            for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
                if ("changeOrder".equals(elementValuePair.element().name())) {
                    boolean typeValue = (boolean) elementValuePair.value().value();
                    if (typeValue) this.description += "<br>This transformation will change the order of items.";
                    else this.description += "<br>This transformation will NOT change the order of items.";
                }
            }
            this.tag = "";
        }
        else if (type == TYPE_ACTION) {
            this.tag = "Blocking";
            for (AnnotationDesc.ElementValuePair elementValuePair : annotation.elementValues()) {
                if ("blocking".equals(elementValuePair.element().name())) {
                    boolean typeValue = (boolean) elementValuePair.value().value();
                    if (typeValue) this.description += "<br>This action will block current thread.";
                    else this.description += "<br>This action will NOT block current thread.";
                }
            }
        }
    }

    public static PSPipelineDoc build(ClassDoc classDoc, MethodDoc methodDoc) {
        AnnotationDesc[] annotations = methodDoc.annotations();
        for (AnnotationDesc annotation : annotations) {
            AnnotationTypeDoc annotationType = annotation.annotationType();
            if (Consts.TRANSFORMATION_ANNOTATION.equals(annotationType.toString())) {
                return new PSPipelineDoc(classDoc, methodDoc, annotation, TYPE_TRANSFORMATION);
            }
            else if (Consts.ACTION_ANNOTATION.equals(annotationType.toString())) {
                return new PSPipelineDoc(classDoc, methodDoc, annotation, TYPE_ACTION);
            }
        }
        return null;
    }

    public String toString() {
        String operatorDocStr = String.format(Locale.ENGLISH,
                "| `%s,%s` | **`%s`** <br> %s |",
//                this.type == TYPE_TRANSFORMATION ? "transform" : "output",
                Utils.getSimpleTypeName(this.declaringClassDoc),
                Utils.getSimpleTypeName(this.returnType),
                this.shortSignature,
                this.description
                );
        return operatorDocStr;
    }

}
