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


        Log.d("CindyDebug", messageData);

        for (Map.Entry<String, Float> entry: res.entrySet()) {

            if(entry.getValue() >= max && entry.getKey() != "compound") {
                max = entry.getValue();
                emotion = entry.getKey();
            }
        }

        Float compound = res.get("compound");
        String result = "";

        if(emotion == "positive"){
            if(compound >= 0.5f){
                result = "Very Positive";
            }
            else{
                result = "Positive";
            }
        }
        else if(emotion == "neutral"){
            if(res.get("neutral") < 0.7f){
                if(compound >= 0.35f){
                    result = "Positive";


                        
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

        Log.d("CindyDebug", res.toString());
        Log.d("CindyDebug", result);
        Log.d("CindyDebug", "-------------------");

        return result;

    }

}
