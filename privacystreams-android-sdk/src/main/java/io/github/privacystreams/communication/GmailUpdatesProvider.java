package io.github.privacystreams.communication;

import android.os.Handler;
import android.os.Looper;

import io.github.privacystreams.utils.DeviceUtils;
import io.github.privacystreams.utils.Logging;
import com.google.api.services.gmail.Gmail;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This is the provider which constantly fetches the gmail updates.
 */
class GmailUpdatesProvider extends BaseGmailProvider {
    private transient Timer timer = new Timer();
    private transient boolean running = false;
    // Last time of fetching emails
    private transient long lastTime = System.currentTimeMillis() / 1000;

    private long frequency;

    GmailUpdatesProvider(long frequency) {
        this.frequency = frequency;
        this.addParameters(frequency);
    }

    /**
     * For queries in all other times later on, when the app does not need to get
     * authorization and permission from the activity all over again.
     */
    @Override
    protected void provide() {
        Looper.prepare();
        super.provide();

        if(authorized){
            doEmailUpdates();
        }
        Looper.loop();
    }

    @Override
    public void onSuccess(Gmail service) {
        super.onSuccess(service);
        if(running){
            timer.cancel();
        }
        else{
            timer = new Timer();
        }
        doEmailUpdates();
    }


    private void doEmailUpdates(){
        running = true;
        final Handler handler = new Handler();
        TimerTask doEmailUpdatesTask = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(DeviceUtils.isDeviceOnline(getContext())){
                                if (mLastEmailTime != 0) {
                                    new FetchEmailTask().execute(buildTimeQuery(mLastEmailTime));
                                }
                                else {
                                    new FetchEmailTask().execute(buildTimeQuery(lastTime));
                                    lastTime = System.currentTimeMillis() / 1000;
                                }
                            }
                            else
                                Logging.error("No internet connection");
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        timer.schedule(doEmailUpdatesTask, 0, GmailUpdatesProvider.this.frequency);

    }
}
