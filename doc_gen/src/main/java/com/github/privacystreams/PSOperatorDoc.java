package com.github.privacystreams;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;

/**
 * Created by yuanchun on 11/03/2017.
 */

public class PSOperatorDoc {

    ClassDoc declaringClassDoc;
    MethodDoc methodDoc;
    String description;

    String shortSignature;
    String returnTypeStr;
//    String completeSignature;

    Type inputType;
    Type outputType;

    private PSOperatorDoc(ClassDoc classDoc, MethodDoc methodDoc) {
        this.declaringClassDoc = classDoc;
        this.methodDoc = methodDoc;
        this.description = methodDoc.commentText().replace('\n', ' ');;

        ParameterizedType returnType = methodDoc.returnType().asParameterizedType();
        this.inputType = returnType.typeArguments()[0];
        this.outputType = returnType.typeArguments()[1];
        this.returnTypeStr = "Function<" + this.inputType.typeName() + ", " + this.outputType.typeName() + ">";

//        this.completeSignature = "Function<" + this.inputType + ", " + this.outputType + "> " + methodDoc.toString();

        String shortSignature = classDoc.name() + "." + methodDoc.name() + "(";
        boolean firstParameter = true;
        for (Parameter parameter : methodDoc.parameters()) {
            if (firstParameter) {
                shortSignature += parameter.typeName() + " " + parameter.name();
                firstParameter = false;
            }
            else {
                shortSignature += ", " + parameter.typeName() + " " + parameter.name();
            }
        }
        shortSignature += ")";
        this.shortSignature = shortSignature;
    }

    public static PSOperatorDoc build(ClassDoc classDoc, MethodDoc methodDoc) {
        if (methodDoc.isStatic() && methodDoc.isPublic() && Consts.TYPE_FUNCTION.equals(methodDoc.returnType().typeName())) {
            return new PSOperatorDoc(classDoc, methodDoc);
        }
        return null;
    }

    public String toString() {
        String operatorDocStr = "| `" + this.returnTypeStr + "` | `" + this.shortSignature + "` <br> " + this.description + " |";
        return operatorDocStr;
    }

}
