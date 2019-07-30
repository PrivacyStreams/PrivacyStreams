package io.github.privacystreams.communication;

import android.util.Log;

import net.nunoachenriques.vader.SentimentAnalysis;
import net.nunoachenriques.vader.lexicon.English;
import net.nunoachenriques.vader.text.TokenizerEnglish;

import java.util.Map;

import io.github.privacystreams.core.UQI;

public class SentimentAnalyzer extends SentimentProcessor<String> {

    SentimentAnalyzer(String messageDataField) {
        super(messageDataField);
    }

    @Override
    protected String getSentiment(UQI uqi, String messageData) {

        SentimentAnalysis sa = new SentimentAnalysis(new English(), new TokenizerEnglish());

        Map<String, Float> res = sa.getSentimentAnalysis(messageData);

        Float max = 0.0f;

        String emotion = "";


        Log.d("Sentiment", messageData);

        for (Map.Entry<String, Float> entry: res.entrySet()) {

            if(entry.getValue() >= max && !entry.getKey().equalsIgnoreCase("compound")) {
                max = entry.getValue();
                emotion = entry.getKey();
            }
        }

        Float compound = res.get("compound");
        String result = "";

        if(emotion.equalsIgnoreCase("positive")){
            if(compound >= 0.5f){
                result = "Very Positive";
            }
            else{
                result = "Positive";
            }
        }
        else if(emotion.equalsIgnoreCase("neutral")){
            if(res.get("neutral") < 0.7f){
                if(compound >= 0.35f) {
                    result = "Positive";
                }
                else if(compound <= -0.35f) {
                    result = "Negative";
                }
                else{
                    result = "Neutral";
                }
            }
            else{
                result = "Neutral";
            }

        }
        else{
            if(compound <= -0.5f){
                result = "Very Negative";
            }
            else{
                result = "Negative";
            }
        }

        Log.d("Sentiment", res.toString());
        Log.d("Sentiment", result);
        Log.d("Sentiment", "----------------------------------------");

        return result;

    }
}
