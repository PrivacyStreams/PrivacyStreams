package com.github.privacystreams.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.CALL_STATE_RINGING;

/**
 * Receive phone call related broadcasts
 */

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            PhoneStateUpdateListener listener = new PhoneStateUpdateListener();

            tmgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class PhoneStateUpdateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String number) {
            switch (state){
                case CALL_STATE_IDLE:
                    Log.e("state","idle "+number);
                    break;
                case CALL_STATE_RINGING:
                    // incoming
                    Log.e("state","ringing "+number);
                    break;
                case CALL_STATE_OFFHOOK:
                    // outgoing
                    Log.e("state","offhook "+number);
                    break;
            }
        }
    }

}
