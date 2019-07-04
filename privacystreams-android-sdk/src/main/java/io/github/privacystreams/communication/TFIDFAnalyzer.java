package io.github.privacystreams.communication;

import io.github.privacystreams.core.UQI;

public class TFIDFAnalyzer extends TFIDFProcessor<String> {

    TFIDFAnalyzer(String messageDataField) {
        super(messageDataField);
    }

    @Override
    protected String getTFIDFScore(UQI uqi, String messageData) {

        return "";

    }

}
