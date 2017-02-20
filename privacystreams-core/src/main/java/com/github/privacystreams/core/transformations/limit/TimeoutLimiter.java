package com.github.privacystreams.core.transformations.limit;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Logging;

/**
 * Created by yuanchun on 28/11/2016.
 * limit the time of the function
 */
final class TimeoutLimiter extends StreamLimiter {
    private final long timeoutMillis;

    TimeoutLimiter(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    /*
     * Start the timer when the function starts evaluating
     */
    protected void onStart(MultiItemStream input, MultiItemStream output) {
        new TimeoutCancelTask(input, output, this.timeoutMillis).start();
    }

    @Override
    protected boolean keep(Item item) {
        return true;
    }

    /*
     * An idle task which simply sleeps some millis seconds.
     */
    private class TimeoutCancelTask extends Thread {
        private long millis;
        private MultiItemStream input;

        TimeoutCancelTask(MultiItemStream input, MultiItemStream output, long millis) {
            this.input = input;
            this.millis = millis;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(this.millis);
            } catch (InterruptedException e) {
                Logging.warn("TimeoutLimiter failed.");
                e.printStackTrace();
            }
            input.write(null);
            TimeoutLimiter.this.cancel();
        }
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(String.valueOf(timeoutMillis));
        return parameters;
    }
}
