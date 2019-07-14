package io.github.privacystreams.communication;

import android.util.Log;

import net.nunoachenriques.vader.text.TokenizerEnglish;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;

import static io.github.privacystreams.communication.Message.CONTENT;

public class TFIDFAnalyzer extends TFIDFProcessor<String> {

    TFIDFAnalyzer(String messageDataField) {
        super(messageDataField);
    }

    @Override
    protected String getTFIDFScore(UQI uqi, String messageData) {


        class TFIDFCalc{

            public double find_tf(String term, List<String> doc){
                double doc_size = doc.size();
                double counter = 0;

                for(String word: doc){
                    if(term.equalsIgnoreCase(word)){
                        counter ++;
                    }
                }

                return counter/doc_size;
            }

            public boolean contains_term(String term, List<String> doc){
                for(String word: doc){
                    if(term.equalsIgnoreCase(word)){
                        return true;
                    }
                }
                return false;
            }

            public double find_idf(String term, List<List<String>> docs){
                double corpus_size = docs.size();
                double counter = 0;

                for(List<String> doc: docs){
                    if(contains_term(term, doc)){
                        counter++;
                    }
                }

                return Math.log(corpus_size/counter);
            }

            public double tfidf(String term, List<String> doc, List<List<String>> corpus){
                return find_tf(term, doc) * find_idf(term, corpus);
            }

        }

        double max1 = 0.0;
        double max2 = 0.0;
        double max3 = 0.0;
        String word1 = "";
        String word2 = "";
        String word3 = "";

        try {
            TokenizerEnglish tokenizer = new TokenizerEnglish();

            List<String> data = uqi.getData(Message.getAllSMS(), Purpose.ADS("")).asList(CONTENT);

            List<List<String>> myList = new ArrayList<>();


            for(String sent: data){
                myList.add(tokenizer.cleanPunctuationAndSplitWhitespace(sent, ""));
            }

            TFIDFCalc calculator = new TFIDFCalc();

            List<String> tokens = tokenizer.cleanPunctuationAndSplitWhitespace(messageData, "");

            System.out.println(tokens);

            Log.d("TFIDF", messageData);


            for(String word: tokens){

                double res = calculator.tfidf(word, tokens, myList);

                if(!word.equalsIgnoreCase(word1) && !word.equalsIgnoreCase(word2) && !word.equalsIgnoreCase(word3)) {

                    if (res > max1) {
                        max3 = max2;
                        word3 = word2;

                        max2 = max1;
                        word2 = word1;

                        max1 = res;
                        word1 = word;

                    } else if (res > max2) {
                        max3 = max2;
                        word3 = word2;
                        max2 = res;
                        word2 = word;

                    } else if (res > max3) {
                        max3 = res;
                        word3 = word;
                    }
                }

            }


            Log.d("TFIDF", "1st highest score : " + max1 + " with word " + word1);
            Log.d("TFIDF", "2nd highest score : " + max2 + " with word " + word2);
            Log.d("TFIDF", "3rd highest score : " + max3 + " with word " + word3);

            Log.d("TFIDF", "-----------------------------");

        }
        catch(Exception e){

        }

        String res = "1st highest score : " + max1 + " with word " + word1 + "\n"
                    +"2nd highest score : " + max2 + " with word " + word2 + "\n"
                    +"3rd highest score : " + max3 + " with word " + word3;

        return res;

    }
}