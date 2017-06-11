package com.github.privacystreams.communication;

import android.os.Handler;
import android.os.Looper;

import com.github.privacystreams.utils.ConnectionUtils;
import com.github.privacystreams.utils.Duration;
import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.Logging;
import com.google.api.services.gmail.Gmail;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This is the provider which constantly fetches the gmail updates.
 */
 class GmailUpdatesProvider extends BaseGmailProvider{
    private Timer timer = new Timer();
    private boolean running = false;
    private long lastTime = (System.currentTimeMillis()- Duration.hours(100))/1000; // The unit is second

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
                        try{
                            if(ConnectionUtils.isDeviceOnline(getContext())){
                                if(mLastEmailTime!=0){
                                    new FetchEmailTask().execute(buildTimeQuery(mLastEmailTime));
                                }
                                else{
                                    new FetchEmailTask().execute(buildTimeQuery(lastTime));
                                    lastTime = System.currentTimeMillis();
                                }
                            }
                            else
                                Logging.error("No internet connection");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doEmailUpdatesTask,0, Globals.EmailConfig.pollingInterval);

    }
}
