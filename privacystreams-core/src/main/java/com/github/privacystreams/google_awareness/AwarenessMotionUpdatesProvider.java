package com.github.privacystreams.google_awareness;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.privacystreams.core.BuildConfig;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


/**
 * Provide motion context with Google Awareness API
 */
class AwarenessMotionUpdatesProvider extends MStreamProvider {
    private class GoogleApiFixUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private final Thread.UncaughtExceptionHandler mWrappedHandler;

        GoogleApiFixUncaughtExceptionHandler(Thread.UncaughtExceptionHandler wrappedHandler) {
            mWrappedHandler = wrappedHandler;
        }

        @Override public void uncaughtException(Thread t, Throwable e) {

            if (e instanceof SecurityException &&
                    e.getMessage().contains("Invalid API Key for package")) {
                return;
            }


            // resend the exception
            mWrappedHandler.uncaughtException(t, e);
        }
    }

    private static final String WALKINGFENCE = "Walking Fence";                     //Set up the fence key for the four fences we need
    private static final String TILTINGFENCE = "Tilting Fence";
    private static final String ONFOOTFENCE = "On Foot Fence";
    private static final String RUNNINGFENCE = "Running Fence";
    private final String FENCE_RECEIVER_ACTION = BuildConfig.APPLICATION_ID+ "FENCE_RECEIVER_ACTION";
    private PendingIntent myPendingIntent;
    private GoogleApiClient client;                                                 //While using google awarenesss, a google api client is needed for connection
    private FenceReceiver myFenceReceiver;
    private Intent intent;
    private IntentFilter myFillter;
    AwarenessFence walkingFence, tiltingFence, onFootFence, runningFence;  //Objects for detecting the physical motion

    @Override
    protected void provide() {
        Thread thread = Thread.currentThread();
        Thread.UncaughtExceptionHandler wrapped = thread.getUncaughtExceptionHandler();
        if (!(wrapped instanceof GoogleApiFixUncaughtExceptionHandler)) {
            GoogleApiFixUncaughtExceptionHandler handler = new GoogleApiFixUncaughtExceptionHandler(wrapped);
            thread.setUncaughtExceptionHandler(handler);
        }
//        Thread thread = Thread.currentThread();
//        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                System.out.println(thread.getName() + " throws exception: " + throwable);
//            }
//        });

            client = new GoogleApiClient.Builder(getContext())                              //Establish Connection
                    .addApi(Awareness.API)
                    .build();
            client.connect();
            walkingFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);     //Create Fence
            tiltingFence = DetectedActivityFence.during(DetectedActivityFence.TILTING);
            onFootFence = DetectedActivityFence.during(DetectedActivityFence.ON_FOOT);
            runningFence = DetectedActivityFence.during(DetectedActivityFence.RUNNING);

            intent = new Intent(FENCE_RECEIVER_ACTION);                                     //Set up the intent and intent filter
            myFillter = new IntentFilter(FENCE_RECEIVER_ACTION);
            myPendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);           //Set up the pendingIntent
            myFenceReceiver = new FenceReceiver();                                              //Set up the receiver
            getContext().registerReceiver(myFenceReceiver, myFillter);
            registerFence(WALKINGFENCE, walkingFence);                                       //register the fences
            registerFence(TILTINGFENCE, tiltingFence);
            registerFence(ONFOOTFENCE, onFootFence);
            registerFence(RUNNINGFENCE, runningFence);
    }

    // Register the fence and add it to the pending intent
    protected void registerFence(final String fenceKey, final AwarenessFence fence) {
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(fenceKey, fence, myPendingIntent)             //Add fence to the pendingIntent
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.e(fenceKey, "Fence was successfully registered.");
                        } else {
                            Log.e(fenceKey, "Fence could not be registered: " + status);
                        }
                    }
                });
    }

    public class FenceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(TextUtils.equals(FENCE_RECEIVER_ACTION,intent.getAction())){    //Check if is the desired action that we are looking for
                FenceState fenceState = FenceState.extract(intent);
                switch (fenceState.getCurrentState()) {                         //Check the state info incase some error happened
                    case FenceState.TRUE:
                        Log.e(fenceState.getFenceKey(), "Active");

                        // When new motion has been detected, output a new physical activity
                        output(new AwarenessMotion(System.currentTimeMillis(), fenceState.getFenceKey()));
                        break;
                }
            }
        }
    }
    //Method for unregister all of the fences added
    protected void unregisterFence(final String fenceKey) {
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .removeFence(fenceKey)
                        .build()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Log.e("Fence", "Fence " + fenceKey + " successfully removed.");

                } else {
                    Log.e("Fence", "Fence " + fenceKey + " can not be removed.");
                }
            }
        });
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        unregisterFence(WALKINGFENCE);                              //Unregister the fences
        unregisterFence(TILTINGFENCE);
        unregisterFence(ONFOOTFENCE);
        unregisterFence(RUNNINGFENCE);
        getContext().unregisterReceiver(myFenceReceiver);           // unregister the Receiver
    }
}