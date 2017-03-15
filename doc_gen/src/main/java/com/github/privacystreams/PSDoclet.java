package com.github.privacystreams;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.formats.html.HtmlDoclet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class PSDoclet extends HtmlDoclet {

    private List<PSItemDoc> psItems = new ArrayList<>();
    private List<PSOperatorWrapperDoc> psOperatorWrappers = new ArrayList<>();

    private String outputDir;
    private String docTitle;
    private void readOptions(String[][] options) {
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals("-d")) {
                this.outputDir = opt[1];
            }
            if (opt[0].equals("-doctitle")) {
                this.docTitle = opt[1];
            }
        }
    }

    private boolean build(RootDoc rootDoc) {
        this.readOptions(rootDoc.options());

        ClassDoc[] classes = rootDoc.classes();

        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];

            PSItemDoc itemDoc = PSItemDoc.build(classDoc);
            if (itemDoc != null) this.psItems.add(itemDoc);

            PSOperatorWrapperDoc operatorWrapperDoc = PSOperatorWrapperDoc.build(classDoc);
            if (operatorWrapperDoc != null) this.psOperatorWrappers.add(operatorWrapperDoc);
        }

        this.dump();
        return true;
    }

    public static boolean start(RootDoc rootDoc) {
        return new PSDoclet().build(rootDoc);
    }

    private void dump() {
        String destDirName = this.outputDir;

        try {
            File itemDocsFile = new File(destDirName, "items.md");
            PrintStream itemDocsPrinter = new PrintStream(itemDocsFile);

            itemDocsPrinter.println("# " + this.docTitle + " - Items\n");

            itemDocsPrinter.println("This document contains all types of personal data available in PrivacyStreams.\n");

            for (PSItemDoc psItemDoc : this.psItems) {
                itemDocsPrinter.println("- [" + psItemDoc.name + "](#" + psItemDoc.name.toLowerCase() + ")");
            }

            itemDocsPrinter.println();

            for (PSItemDoc psItemDoc : this.psItems) {
                itemDocsPrinter.println(psItemDoc);
            }
            itemDocsPrinter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            File operatorDocsFile = new File(destDirName, "operators.md");
            PrintStream operatorDocsPrinter = new PrintStream(operatorDocsFile);
            operatorDocsPrinter.println("# " + this.docTitle + " - Operators\n");

            operatorDocsPrinter.println("This document contains all operators available in PrivacyStreams.\n");

            operatorDocsPrinter.print(Consts.OPERATORS_TABLE_HEADER);

            List<PSOperatorDoc> allOperatorDocs = new ArrayList<>();
//            for (PSItemDoc psItemDoc : this.psItems) {
//                allOperatorDocs.addAll(psItemDoc.providerDocs);
//            }
            for (PSOperatorWrapperDoc psOperatorWrapperDoc : psOperatorWrappers) {
                allOperatorDocs.addAll(psOperatorWrapperDoc.operatorDocs);
            }

            for (PSOperatorDoc operatorDoc : allOperatorDocs) {
                operatorDocsPrinter.println(operatorDoc);
            }
            operatorDocsPrinter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static int optionLength(String option) {
        return HtmlDoclet.optionLength(option);
    }

    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        return HtmlDoclet.validOptions(options, reporter);
    }

}
