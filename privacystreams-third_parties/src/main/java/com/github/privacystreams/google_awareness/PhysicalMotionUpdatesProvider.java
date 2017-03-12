package com.github.privacystreams.google_awareness;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.privacystreams.core.*;
import com.github.privacystreams.core.BuildConfig;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by lenovo on 2017/3/6.
 */

public class PhysicalMotionUpdatesProvider extends MultiItemStreamProvider {
    //Set up the fence key for the four fences we need
    private static final String WALKING = "Walking";
    private static final String TILTING  = "Tilting";
    private static final String ONFOOT = "On Foot";
    private static final String RUNNING = "Running";
    private final String FENCE_RECEIVER_ACTION = BuildConfig.APPLICATION_ID+ "FENCE_RECEIVER_ACTION";
    private GoogleApiClient client;                                                 //While using google awarenesss, a google api client is needed for connection
    private FenceReceiver myFenceReceiver;


    @Override
    protected void provide() {
        //Establish Connection
        client = new GoogleApiClient.Builder(getContext())
                .addApi(Awareness.API)
                .build();
        client.connect();
        //Set up the receiver
        myFenceReceiver = new FenceReceiver();
        getContext().registerReceiver(myFenceReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));
        registerFence(WALKING, DetectedActivityFence.during(DetectedActivityFence.WALKING));                                       //register the fences
        registerFence(TILTING, DetectedActivityFence.during(DetectedActivityFence.TILTING));
        registerFence(ONFOOT, DetectedActivityFence.during(DetectedActivityFence.ON_FOOT));
        registerFence(RUNNING, DetectedActivityFence.during(DetectedActivityFence.RUNNING));
    }

    // Register the fence and add it to the pending intent
    protected void registerFence(final String fenceKey, final AwarenessFence fence) {
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(fenceKey, fence, PendingIntent.getBroadcast(getContext(), 0, new Intent(FENCE_RECEIVER_ACTION), 0))             //Add fence to the pendingIntent
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
            //Check if it is the desired action that we are looking for
            if(TextUtils.equals(FENCE_RECEIVER_ACTION,intent.getAction())){
                FenceState fenceState = FenceState.extract(intent);
                //Check the state info in case some error happened
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        // When new motion has been detected, output a new physical activity
                        output(new PhysicalActivity(System.currentTimeMillis(),fenceState.getFenceKey()));
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
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        //Unregister the fences
        unregisterFence(WALKING);
        unregisterFence(TILTING);
        unregisterFence(ONFOOT);
        unregisterFence(RUNNING);
        // unregister the Receiver
        getContext().unregisterReceiver(myFenceReceiver);
    }
}
