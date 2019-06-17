package io.github.privacystreams.communication;

import net.nunoachenriques.vader.SentimentAnalysis;
import net.nunoachenriques.vader.lexicon.English;
import net.nunoachenriques.vader.text.TokenizerEnglish;

import java.util.LinkedList;
import java.util.List;

import io.github.privacystreams.core.UQI;

public class SentimentAnalyzer extends SentimentProcessor<String> {

    SentimentAnalyzer(String messageDataField) {
        super(messageDataField);
    }

    @Override
    protected String getSentiment(UQI uqi, String messageData) {
        //sentiment analyze code
        List<String> sentences = new LinkedList<String>();
        sentences.add("VADER is smart, handsome, and funny.");
        sentences.add("VADER is smart, handsome, and funny!");
        sentences.add("VADER is very smart, handsome, and funny.");
        sentences.add("VADER is VERY SMART, handsome, and FUNNY.");
        sentences.add("VADER is VERY SMART, handsome, and FUNNY!!!");
        sentences.add("VADER is VERY SMART, really handsome, and INCREDIBLY FUNNY!!!");
        sentences.add("The book was good.");
        sentences.add("The book was kind of good.");
        sentences.add("The plot was good, but the characters are uncompelling and the dialog is not great.");
        sentences.add("A really bad, horrible book.");
        sentences.add("At least it isn't a horrible book.");
        sentences.add(":) and :D");
        sentences.add("");
        sentences.add("Today sux");
        sentences.add("Today sux!");
        sentences.add("Today SUX!");
        sentences.add("Today kinda sux! But I'll get by, lol");

        System.out.println("hello world");

        SentimentAnalysis sa = new SentimentAnalysis(new English(), new TokenizerEnglish());

        System.out.println("hello world");

        for (String sentence : sentences) {
            System.out.println(sentence);
            System.out.println(sa.getSentimentAnalysis(sentence).toString());
        }

        System.out.println("hello world");

        return "";
    }

}
