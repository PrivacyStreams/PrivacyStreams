package io.github.privacystreams.communication;

import android.util.Log;

import net.nunoachenriques.vader.text.TokenizerEnglish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        double max4 = 0.0;
        double max5 = 0.0;

        String word1 = "";
        String word2 = "";
        String word3 = "";
        String word4 = "";
        String word5 = "";

        //NLTK Stopwords List + more
        List<String> stopWords = Arrays.asList("ourselves", "hers", "between", "yourself", "but", "again",
                "there", "about", "once", "during", "out", "very", "having", "with", "they", "own", "an", "be",
                "some", "for", "do", "its", "yours", "such", "into", "of", "most", "itself", "other", "off",
                "is", "s", "am", "or", "who", "as", "from", "him", "each", "the", "themselves", "until", "below",
                "are", "we", "these", "your", "his", "through", "don", "nor", "me", "were", "her", "more", "himself",
                "this", "down", "should", "our", "their", "while", "above", "both", "up", "to", "ours", "had", "she",
                "all", "no", "when", "at", "any", "before", "them", "same", "and", "been", "have", "in", "will",
                "on", "does", "yourselves", "then", "that", "because", "what", "over", "why", "so", "can", "did",
                "not", "now", "under", "he", "you", "herself", "has", "just", "where", "too", "only", "myself",
                "which", "those", "i", "after", "few", "whom", "t", "being", "if", "theirs", "my", "against", "a",
                "by", "doing", "it", "how", "further", "was", "here", "than",
                //non NLTK stopwords
                "don't", "doesn't", "didn't", "how's", "who's", "what's", "haven't", "hasn't", "hadn't",
                "isn't", "aren't", "wasn't", "weren't",
                "it's", "he's", "she's", "they're", "we're", "i'm", "you're",
                "oh", "would", "wouldn't", "can't");

        try {
            TokenizerEnglish tokenizer = new TokenizerEnglish();

            List<String> data = uqi.getData(Message.getAllSMS(), Purpose.ADS("")).asList(CONTENT);

            List<List<String>> myList = new ArrayList<>();

            for(String sent: data){
                myList.add(tokenizer.cleanPunctuationAndSplitWhitespace(sent, ""));
            }

            TFIDFCalc calculator = new TFIDFCalc();

            List<String> tokens = tokenizer.cleanPunctuationAndSplitWhitespace(messageData, "");


            Log.d("TFIDF", messageData);


            for(String word: tokens){

                if(stopWords.contains(word.toLowerCase())){
                    continue;
                }

                double res = calculator.tfidf(word, tokens, myList);

                if(!word.equalsIgnoreCase(word1) && !word.equalsIgnoreCase(word2) && !word.equalsIgnoreCase(word3) &&
                !word.equalsIgnoreCase(word4) && !word.equalsIgnoreCase(word5)) {

                    if (res > max1) {
                        max5 = max4;
                        word5 = word4;

                        max4 = max3;
                        word4 = word3;

                        max3 = max2;
                        word3 = word2;

                        max2 = max1;
                        word2 = word1;

                        max1 = res;
                        word1 = word;

                    } else if (res > max2) {
                        max5 = max4;
                        word5 = word4;

                        max4 = max3;
                        word4 = word3;

                        max3 = max2;
                        word3 = word2;

                        max2 = res;
                        word2 = word;

                    } else if (res > max3) {
                        max5 = max4;
                        word5 = word4;

                        max4 = max3;
                        word4 = word3;

                        max3 = res;
                        word3 = word;

                    } else if (res > max4) {
                        max5 = max4;
                        word5 = word4;

                        max4 = res;
                        word4 = word;

                    } else if (res > max5){
                        max5 = res;
                        word5 = word;
                    }
                }

            }


            Log.d("TFIDF", "1st highest score : " + max1 + " with word " + word1.toLowerCase());
            Log.d("TFIDF", "2nd highest score : " + max2 + " with word " + word2.toLowerCase());
            Log.d("TFIDF", "3rd highest score : " + max3 + " with word " + word3.toLowerCase());
            Log.d("TFIDF", "4th highest score : " + max4 + " with word " + word4.toLowerCase());
            Log.d("TFIDF", "5th highest score : " + max5 + " with word " + word5.toLowerCase());

        }
        catch(Exception e){

        }

        HashMap<String, Double> res = new HashMap<>();

        res.put(word1.toLowerCase(), max1);
        res.put(word2.toLowerCase(), max2);
        res.put(word3.toLowerCase(), max3);
        res.put(word4.toLowerCase(), max4);
        res.put(word5.toLowerCase(), max5);

        Log.d("TFIDF", res.toString());
        Log.d("TFIDF", "----------------------------------------");

        return res.toString();

    }
}
