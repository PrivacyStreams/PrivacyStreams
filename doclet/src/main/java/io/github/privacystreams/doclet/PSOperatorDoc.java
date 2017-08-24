package io.github.privacystreams.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

import java.util.Objects;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSOperatorDoc {

    ClassDoc declaringClassDoc;
    MethodDoc methodDoc;
    String description;
    String paramDesc;

    String shortSignature;
//    String completeSignature;

    Type returnType;

    private PSOperatorDoc(ClassDoc classDoc, MethodDoc methodDoc) {
        this.declaringClassDoc = classDoc;
        this.methodDoc = methodDoc;
        this.description = methodDoc.commentText().replace('\n', ' ');
        this.paramDesc = "";

        Tag[] paramTags = methodDoc.tags("param");
        for (Tag paramTag : paramTags) {
            String paraStr = paramTag.text();
            String paraName = paraStr.substring(0, paraStr.indexOf(' ')).replace('\n', ' ');;
            String paraDesc = paraStr.substring(paraStr.indexOf(' ') + 1).replace('\n', ' ');;
            this.paramDesc += "<br> - `" + paraName + "`: " + paraDesc;
        }

        this.returnType = methodDoc.returnType();
//        ParameterizedType returnType = methodDoc.returnType().asParameterizedType();
//        this.inputType = returnType.typeArguments()[0];
//        this.outputType = returnType.typeArguments()[1];
//        this.completeSignature = "Function<" + this.inputType + ", " + this.outputType + "> " + methodDoc.toString();

        String shortSignature = classDoc.name() + "." + methodDoc.name() + "(";
        boolean firstParameter = true;
        for (Parameter parameter : methodDoc.parameters()) {
            if (firstParameter) {
                shortSignature += Utils.getSimpleTypeName(parameter.type()) + " " + parameter.name();
                firstParameter = false;
            } else {
                shortSignature += ", " + Utils.getSimpleTypeName(parameter.type()) + " " + parameter.name();
            }
        }
        shortSignature += ")";
        this.shortSignature = shortSignature;
    }

    public static PSOperatorDoc build(ClassDoc classDoc, MethodDoc methodDoc) {
        if (methodDoc.isStatic() && methodDoc.isPublic()
                && Utils.instanceOf(methodDoc.returnType().asClassDoc(), Consts.TYPE_FUNCTION)) {
            return new PSOperatorDoc(classDoc, methodDoc);
        }
        return null;
    }

    public String toString() {
        String operatorDocStr = "| `" + Utils.getSimpleTypeName(this.returnType) + "` | **`" + this.shortSignature + "`** <br> " + this.description + this.paramDesc + " |";
        return operatorDocStr;
    }

}
