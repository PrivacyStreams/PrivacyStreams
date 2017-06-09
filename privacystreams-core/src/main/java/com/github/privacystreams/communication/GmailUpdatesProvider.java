package com.github.privacystreams.communication;

import android.os.Handler;
import android.os.Looper;

import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.utils.ConnectionUtils;
import com.github.privacystreams.utils.Duration;
import com.github.privacystreams.utils.Globals;
import com.google.api.services.gmail.Gmail;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This is the provider which will constantly update the gmail information.
 */
 class GmailUpdatesProvider extends BaseGmailProvider implements GmailResultListener{
    private Timer timer = new Timer();
    private boolean running = false;
    private long lastTime = System.currentTimeMillis()- Duration.hours(100);

    @Override
    protected void provide() {
        Looper.prepare();
        super.provide();
        /**
         * For queries in all other times later on, when the app does not need to get authorization and permission from the
         * activity all over again.
         */

        if(authorized){
            doEmailUpdates();
        }
        Looper.loop();
    }

    @Override
    public void onSuccess(Gmail service) {
        mService = service;
        if(running){
            timer.cancel();
        }
        else{
            timer = new Timer();
        }
        doEmailUpdates();
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail Updates canceled."));
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
                                new MakeRequestTask().execute(buildTimeQuery(lastTime));
                                lastTime = System.currentTimeMillis();
                            }

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
