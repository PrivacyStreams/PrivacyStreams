package io.github.privacystreams.communication;

import net.nunoachenriques.vader.text.TokenizerEnglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.privacystreams.core.R;
import io.github.privacystreams.core.UQI;


public class TopicModelAnalyzer extends TopicModelProcessor<String> {

    TopicModelAnalyzer(String messageDataField) {
        super(messageDataField);
    }

    @Override
    protected String processCategories(UQI uqi, String messageData) {

        class Dictionary {
            private final Map<Integer, String> categoryMap;
            private final Map<String, List<Integer>> wordCategories;

            private Dictionary() {
                categoryMap = new HashMap<Integer, String>();
                wordCategories = new HashMap<String, List<Integer>>();

                //parseCategories(uqi);
                //mapCategoriesToWords("");
            }

            public Dictionary fromDicFile(final String fileContents) {
                Dictionary dictionary = new Dictionary();
                //dictionary.parseCategories(fileContents);
                //dictionary.mapCategoriesToWords(fileContents);

                return dictionary;
            }

            public String toString() {
                String asString = "";
                if(categoryMap != null) {
                    for(final Integer categoryIndex : categoryMap.keySet()) {
                        asString += String.valueOf(categoryIndex) + " : " + categoryMap.get(categoryIndex) + "\n";
                    }
                }

                if(wordCategories != null) {
                    for(final String word : wordCategories.keySet()) {
                        asString += word + " : " + wordCategories.get(word) + "\n";
                    }
                }

                return asString;
            }

            private void parseCategories(UQI uqi) {
                InputStream fileStream = null;
                try {
                    fileStream = uqi.getContext().getResources().openRawResource(R.raw.liwc2001);
                    //fileStream = getApplicationContext().getResources().openRawResource(R.raw.liwc2001);



                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

                    String line = "";
                    boolean categoriesAreBeingParsed = true;
                    int categorySymbolCount = 0;
                    final String CATEGORY_SECTION_CHAR = "%";

                    do {
                        try {
                            line = reader.readLine();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            line = null;
                        }

                        if (line != null) {
                            if (line.equals(CATEGORY_SECTION_CHAR)) {
                                categorySymbolCount++;
                                categoriesAreBeingParsed = categorySymbolCount < 2;
                            } else {
                                String[] entry = line.split("\t");
                                int index = Integer.parseInt(entry[0]);
                                String value = entry[1];

                                categoryMap.put(index, value);
                            }
                        }
                    } while (categoriesAreBeingParsed || line != null);
                }
                catch(Exception e){

                }
                finally {
                    if(fileStream != null){
                        try {
                            fileStream.close();
                        }
                        catch(Exception e){

                        }
                    }
                }
            }

            private void mapCategoriesToWords(UQI uqi) {
                InputStream fileStream = null;
                try {
                    fileStream = uqi.getContext().getResources().openRawResource(R.raw.liwc2001);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

                    String line = "";
                    int categorySymbolCount = 0;
                    final String CATEGORY_SECTION_CHAR = "%";

                    do {
                        try {
                            line = reader.readLine();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            line = null;
                        }

                        if (line != null) {
                            if (line.equals(CATEGORY_SECTION_CHAR)) {
                                categorySymbolCount++;
                            }
                            else if(categorySymbolCount >= 2){
                                String[] entry = line.split("\t");
                                String term = entry[0];
                                List<String> value = Arrays.asList(entry).subList(1, entry.length);

                                List<Integer> categories = new ArrayList<>();
                                for(int i = 0; i < value.size(); i++){
                                    categories.add(Integer.parseInt(value.get(i)));
                                }

                                wordCategories.put(term, categories);
                            }

                        }
                    } while (line != null);

                }
                catch(Exception e) {

                }
                finally {
                    if(fileStream != null){
                        try {
                            fileStream.close();
                        }
                        catch(Exception e){

                        }
                    }
                }
            }
        }

        Dictionary dic = new Dictionary();
        dic.parseCategories(uqi);
        dic.mapCategoriesToWords(uqi);
        //System.out.println(dic);

        TokenizerEnglish tokenizer = new TokenizerEnglish();
        List<String> sent = tokenizer.cleanPunctuationAndSplitWhitespace(messageData, "");
        Map<String, Integer> info = new HashMap<String, Integer>();

        for(int i = 0; i < sent.size(); i++){

            if (dic.wordCategories.get(sent.get(i)) != null) {

                for (int j = 0; j < dic.wordCategories.get(sent.get(i)).size(); j++) {

                    int cur = dic.wordCategories.get(sent.get(i)).get(j);

                    if (info.get(dic.categoryMap.get(cur)) == null) {
                        info.put(dic.categoryMap.get(cur), 1);
                    } else {
                        info.put(dic.categoryMap.get(cur), (info.get(dic.categoryMap.get(cur)) + 1));
                    }
                }
            }
        }

        System.out.println(info);
        System.out.println("---------------------------------");

        return "";

    }

}
