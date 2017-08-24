package io.github.privacystreams.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.formats.html.HtmlDoclet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Generate API documentation for PrivacyStreams.
 */

public class PSDoclet extends HtmlDoclet {

    private List<PSItemDoc> psItems = new ArrayList<>();
    private List<PSOperatorWrapperDoc> psOperatorWrappers = new ArrayList<>();
    private List<PSPipelineDoc> psPipelines = new ArrayList<>();

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

            if (Utils.instanceOf(classDoc, Consts.TYPE_P_STREAM)) {
                for (MethodDoc methodDoc : classDoc.methods()) {
                    PSPipelineDoc pipelineDoc = PSPipelineDoc.build(classDoc, methodDoc);
                    if (pipelineDoc != null) {
                        this.psPipelines.add(pipelineDoc);
                    }
                }
            }
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

//            itemDocsPrinter.println("# " + this.docTitle + " - Items\n");
//            itemDocsPrinter.println("This document introduces all types of personal data available in PrivacyStreams.\n");

            Collections.sort(this.psItems, new Comparator<PSItemDoc>() {
                @Override
                public int compare(PSItemDoc psItemDoc1, PSItemDoc psItemDoc2) {
                    return psItemDoc1.name.compareTo(psItemDoc2.name);
                }
            });

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

//            operatorDocsPrinter.println("# " + this.docTitle + " - Operators\n");
//            operatorDocsPrinter.println("This document introduces all operators available in PrivacyStreams.\n");

            operatorDocsPrinter.print(Consts.OPERATORS_TABLE_HEADER);

            List<PSOperatorDoc> allOperatorDocs = new ArrayList<>();
            for (PSItemDoc psItemDoc : this.psItems) {
                allOperatorDocs.addAll(psItemDoc.providerDocs);
            }
            for (PSOperatorWrapperDoc psOperatorWrapperDoc : psOperatorWrappers) {
                allOperatorDocs.addAll(psOperatorWrapperDoc.operatorDocs);
            }

            Collections.sort(allOperatorDocs, new Comparator<PSOperatorDoc>() {
                @Override
                public int compare(PSOperatorDoc psOperatorDoc1, PSOperatorDoc psOperatorDoc2) {
                    return psOperatorDoc1.shortSignature.compareTo(psOperatorDoc2.shortSignature);
                }
            });

            for (PSOperatorDoc operatorDoc : allOperatorDocs) {
                operatorDocsPrinter.println(operatorDoc);
            }
            operatorDocsPrinter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            File operatorDocsFile = new File(destDirName, "pipeline.md");
            PrintStream operatorDocsPrinter = new PrintStream(operatorDocsFile);

//            operatorDocsPrinter.println("# " + this.docTitle + " - Transformations and Actions\n");
//            operatorDocsPrinter.println("This document introduces all pipeline functions available in PrivacyStreams.\n");

            operatorDocsPrinter.println("\n## Transformations\n");
            operatorDocsPrinter.print(Consts.PIPELINE_TABLE_HEADER);
            Collections.sort(psPipelines, new Comparator<PSPipelineDoc>() {
                @Override
                public int compare(PSPipelineDoc psDoc1, PSPipelineDoc psDoc2) {
                    return psDoc1.toString().compareTo(psDoc2.toString());
                }
            });
            for (PSPipelineDoc pipelineDoc : psPipelines) {
                if (pipelineDoc.type == PSPipelineDoc.TYPE_TRANSFORMATION)
                    operatorDocsPrinter.println(pipelineDoc);
            }

            operatorDocsPrinter.println("\n## Actions\n");
            operatorDocsPrinter.print(Consts.PIPELINE_TABLE_HEADER);
            Collections.sort(psPipelines, new Comparator<PSPipelineDoc>() {
                @Override
                public int compare(PSPipelineDoc psDoc1, PSPipelineDoc psDoc2) {
                    return psDoc1.toString().compareTo(psDoc2.toString());
                }
            });
            for (PSPipelineDoc pipelineDoc : psPipelines) {
                if (pipelineDoc.type == PSPipelineDoc.TYPE_ACTION)
                    operatorDocsPrinter.println(pipelineDoc);
            }

            operatorDocsPrinter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static int optionLength(String option) {
        return HtmlDoclet.optionLength(option);
    }

    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        return HtmlDoclet.validOptions(options, reporter);
    }

}
