package com.github.privacystreams;

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class DocGenerator extends Standard {
    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            System.out.println(classes[i]);
        }
        return true;
    }
}
