package io.github.privacystreams.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.Type;

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

    public static boolean instanceOf(ClassDoc classDoc, String superClassName) {
        if (classDoc == null || superClassName == null) return false;
        String className = classDoc.containingPackage().name() + "." + classDoc.name();
//        System.out.println(className + " " + superClassName);
        if (className.startsWith(superClassName)) {
            return true;
        }
        return instanceOf(classDoc.superclass(), superClassName);
    }
}
