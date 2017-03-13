package com.github.privacystreams;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;

/**
 * Created by yuanchun on 12/03/2017.
 */

public class Utils {
    public static String getSimpleTypeName(Type type) {
        if (type instanceof ParameterizedType) {
            String typeName = type.typeName() + "<";
            boolean firstParameter = true;
            for (Type parameter : ((ParameterizedType) type).typeArguments()) {
                if (firstParameter) {
                    typeName += getSimpleTypeName(parameter);
                    firstParameter = false;
                }
                else {
                    typeName += "," + getSimpleTypeName(parameter);
                }
            }
            typeName += ">";
            return typeName;
        }
        return type.typeName();
    }
}
