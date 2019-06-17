package io.github.privacystreams.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.privacystreams.communication.Message;
import io.github.privacystreams.communication.SentimentOperators;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;

@RunWith(AndroidJUnit4.class)
public class SMSTest {
    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getContext();
        try {

            String emotion = new UQI(appContext).getData(Message.getAllSMS(), Purpose.ADS("")).setField("emotion", SentimentOperators.getEmotion(Message.CONTENT)).getFirst("emotion");
            System.out.println(emotion);
        }
        catch(Exception e){

        }
        assert(false);
    }
}
