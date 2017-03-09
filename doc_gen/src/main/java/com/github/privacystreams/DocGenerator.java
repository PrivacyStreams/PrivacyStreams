package com.github.privacystreams;

import com.sun.javadoc.*;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class DocGenerator {
    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            System.out.println(classes[i]);
        }
        return true;
    }
}
