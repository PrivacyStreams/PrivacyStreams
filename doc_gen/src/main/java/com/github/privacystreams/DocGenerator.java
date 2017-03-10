package com.github.privacystreams;

import com.sun.javadoc.*;
import com.sun.tools.doclets.formats.html.HtmlDoclet;
import com.sun.tools.doclets.standard.Standard;
import com.sun.tools.javac.code.Attribute;

import java.util.Arrays;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class DocGenerator extends HtmlDoclet {
    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            System.out.println(classes[i]);
            ClassDoc classDoc = classes[i];
            System.out.println(Arrays.asList(classDoc.typeParameters()));
        }
        System.out.println("I'm printing something!");

        return true;
    }
}
