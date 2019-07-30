package io.github.privacystreams.test;

import android.Manifest;
import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.github.privacystreams.communication.Message;
import io.github.privacystreams.communication.SentimentOperators;
import io.github.privacystreams.communication.TFIDFOperators;
import io.github.privacystreams.communication.TopicModelOperators;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.items.TestItem;
import io.github.privacystreams.core.items.TestObject;
import io.github.privacystreams.core.purposes.Purpose;
//import androidx.test.rule.GrantPermissionRule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SMSTest {
    /*
//    @Rule
//    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_SMS);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    //unit test for sentiment analysis
    @Test
    public void testVeryPositiveSentiment1() throws Exception {
        TestObject test = new TestObject(0,0,"He is SMART, HANDSOME, and FUNNY.", 0);

        Context appContext = InstrumentationRegistry.getContext();

        String emotion = new UQI(appContext).getData(Message.getAllSMS(), Purpose.ADS("")).
                setField("emotion", SentimentOperators.getEmotion(Message.CONTENT)).
                getFirst("emotion");
        System.out.println("Hello" + emotion);

        String sentiment = SentimentOperators.getEmotion("He is SMART, HANDSOME, and FUNNY.").toString();
        assertEquals("Testing Very Positive Sentiment", sentiment, "Very Positive");
    }


    @Test
    public void testVeryPositiveSentiment2() throws Exception {
        String sentiment = SentimentOperators.getEmotion("He is very smart!!!").toString();
        assertEquals("Testing Very Positive Sentiment", sentiment, "Very Positive");
    }

    @Test
    public void testPositiveSentiment1() throws Exception {
        String sentiment = SentimentOperators.getEmotion("He is smart.").toString();
        assertEquals("Testing Positive Sentiment", sentiment, "Positive");
    }

    @Test
    public void testPositiveSentiment2() throws Exception {
        String sentiment = SentimentOperators.getEmotion("Any good restaurant you recommend around this area??").toString();
        assertEquals("Testing Positive Sentiment", sentiment, "Positive");
    }

    @Test
    public void testNeutralSentiment1() throws Exception {
        String sentiment = SentimentOperators.getEmotion("There is a basketball game.").toString();
        assertEquals("Testing Neutral Sentiment", sentiment, "Neutral");
    }

    @Test
    public void testNeutralSentiment2() throws Exception {
        String sentiment = SentimentOperators.getEmotion("Do you wanna try this new burger place with me tomorrow?").toString();
        assertEquals("Testing Neutral Sentiment", sentiment, "Neutral");
    }

    @Test
    public void testNegativeSentiment1() throws Exception {
        String sentiment = SentimentOperators.getEmotion("Today sucks").toString();
        assertEquals("Testing Negative Sentiment", sentiment, "Negative");
    }

    @Test
    public void testNegativeSentiment2() throws Exception {
        String sentiment = SentimentOperators.getEmotion("Oh no my flight is delayed").toString();
        assertEquals("Testing Negative Sentiment", sentiment, "Negative");
    }

    @Test
    public void testVeryNegativeSentiment1() throws Exception {
        String sentiment = SentimentOperators.getEmotion("Oh no my flight is delayed :(").toString();
        assertEquals("Testing Very Negative Sentiment", sentiment, "Very Negative");
    }

    @Test
    public void testVeryNegativeSentiment2() throws Exception {
        String sentiment = SentimentOperators.getEmotion("TODAY SUCKS!!!").toString();
        assertEquals("Testing Very Negative Sentiment", sentiment, "Very Negative");
    }


    //unit testing for TF-IDF implementation
    @Test
    public void testTFIDF1() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Oh no my flight is delayed :(").toString();
        assert(tfidf_res.toLowerCase().contains("flight"));
        assert(tfidf_res.toLowerCase().contains("delayed"));
        assert(!tfidf_res.toLowerCase().contains("oh"));
        assert(!tfidf_res.toLowerCase().contains("no"));
    }

    @Test
    public void testTFIDF2() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Probably staying in and studying, busy night").toString();
        assert(tfidf_res.toLowerCase().contains("studying"));
        assert(tfidf_res.toLowerCase().contains("busy"));
        assert(!tfidf_res.toLowerCase().contains("in"));
        assert(!tfidf_res.toLowerCase().contains("and"));
    }

    @Test
    public void testTFIDF3() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Let's grab coffee some time together!").toString();
        assert(tfidf_res.toLowerCase().contains("coffee"));
        assert(!tfidf_res.toLowerCase().contains("some"));
    }

    @Test
    public void testTFIDF4() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Today sucks").toString();
        assert(tfidf_res.toLowerCase().contains("sucks"));
        assert(tfidf_res.toLowerCase().contains("today"));
    }

    @Test
    public void testTFIDF5() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Wanted to check if you and Jessy are coming to dinner with me and my family").toString();
        assert(tfidf_res.toLowerCase().contains("jessy"));
        assert(tfidf_res.toLowerCase().contains("dinner"));
        assert(!tfidf_res.toLowerCase().contains("if"));
        assert(!tfidf_res.toLowerCase().contains("you"));
        assert(!tfidf_res.toLowerCase().contains("and"));
        assert(!tfidf_res.toLowerCase().contains("me"));
    }

    @Test
    public void testTFIDF6() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("This makes me sad...").toString();
        assert(tfidf_res.toLowerCase().contains("sad"));
        assert(!tfidf_res.toLowerCase().contains("this"));
    }

    @Test
    public void testTFIDF7() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("There is a basketball game.").toString();
        assert(tfidf_res.toLowerCase().contains("basketball"));
        assert(tfidf_res.toLowerCase().contains("game"));
        assert(!tfidf_res.toLowerCase().contains("there"));
        assert(!tfidf_res.toLowerCase().contains("is"));
        assert(!tfidf_res.toLowerCase().contains("a"));
    }

    @Test
    public void testTFIDF8() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("I will cook tonight.").toString();
        assert(tfidf_res.toLowerCase().contains("cook"));
        assert(tfidf_res.toLowerCase().contains("tonight"));
        assert(!tfidf_res.toLowerCase().contains("i"));
        assert(!tfidf_res.toLowerCase().contains("will"));
    }

    @Test
    public void testTFIDF9() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("How's your basketball practice today?").toString();
        assert(tfidf_res.toLowerCase().contains("basketball"));
        assert(tfidf_res.toLowerCase().contains("practice"));
        assert(!tfidf_res.toLowerCase().contains("your"));
        assert(!tfidf_res.toLowerCase().contains("how's"));
    }

    @Test
    public void testTFIDF10() throws Exception {
        String tfidf_res = TFIDFOperators.getTFIDF("Any good restaurant you recommend around this area??").toString();
        assert(tfidf_res.toLowerCase().contains("area"));
        assert(tfidf_res.toLowerCase().contains("restaurant"));
        assert(!tfidf_res.toLowerCase().contains("any"));
        assert(!tfidf_res.toLowerCase().contains("this"));
    }


    //unit testing for text categorization implementation
    @Test
    public void testCategorization1() throws Exception {
        String categories = TopicModelOperators.getCategories("TODAY SUCKS!!!").toString();
        assert(categories.toLowerCase().contains("Anger"));
        assert(categories.toLowerCase().contains("Swear"));
        assert(categories.toLowerCase().contains("Negemo"));
    }

    @Test
    public void testCategorization2() throws Exception {
        String categories = TopicModelOperators.getCategories("Wanted to check if you and Jessy are coming to dinner with me and my family").toString();
        assert(categories.toLowerCase().contains("Social"));
        assert(categories.toLowerCase().contains("Eating"));
    }

    @Test
    public void testCategorization3() throws Exception {
        String categories = TopicModelOperators.getCategories("I will cook tonight.").toString();
        assert(categories.toLowerCase().contains("Eating"));
        assert(categories.toLowerCase().contains("Future"));
    }

    @Test
    public void testCategorization4() throws Exception {
        String categories = TopicModelOperators.getCategories("How's your basketball practice today?").toString();
        assert(categories.toLowerCase().contains("Sports"));
        assert(categories.toLowerCase().contains("School"));
    }

    @Test
    public void testCategorization5() throws Exception {
        String categories = TopicModelOperators.getCategories("Any good restaurant you recommend around this area??").toString();
        assert(categories.toLowerCase().contains("Social"));
        assert(categories.toLowerCase().contains("Eating"));
    }

    @Test
    public void testCategorization6() throws Exception {
        String categories = TopicModelOperators.getCategories("Let's grab coffee some time together!").toString();
        assert(categories.toLowerCase().contains("Social"));
        assert(categories.toLowerCase().contains("We"));
    }

    @Test
    public void testCategorization7() throws Exception {
        String categories = TopicModelOperators.getCategories("I'm traveling next weekend so I won't be in town").toString();
        assert(categories.toLowerCase().contains("Motion"));
        assert(categories.toLowerCase().contains("Time"));
        assert(categories.toLowerCase().contains("Future"));
    }

    @Test
    public void testCategorization8() throws Exception {
        String categories = TopicModelOperators.getCategories("He really pissed me off").toString();
        assert(categories.toLowerCase().contains("Anger"));
        assert(categories.toLowerCase().contains("Negemo"));
        assert(categories.toLowerCase().contains("Swear"));
    }

    @Test
    public void testCategorization9() throws Exception {
        String categories = TopicModelOperators.getCategories("I miss you!!").toString();
        assert(categories.toLowerCase().contains("Affect"));
        assert(categories.toLowerCase().contains("Sad"));
    }

    @Test
    public void testCategorization10() throws Exception {
        String categories = TopicModelOperators.getCategories("I will pay for dinner tonight.").toString();
        assert(categories.toLowerCase().contains("Money"));
        assert(categories.toLowerCase().contains("Eating"));
    }
    */




}
