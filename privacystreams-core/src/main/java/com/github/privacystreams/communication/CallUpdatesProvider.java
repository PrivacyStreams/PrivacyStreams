package com.github.privacystreams.communication;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.Date;

import static com.github.privacystreams.utils.CommunicationUtils.normalizePhoneNumber;

/**
 * Provide a live stream of phone call events.
 */
class CallUpdatesProvider extends MStreamProvider {

    private CallReceiver callReceiver;

    CallUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.PROCESS_OUTGOING_CALLS);
        this.addRequiredPermissions(Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void provide() {
        callReceiver = new CallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        this.getUQI().getContext().registerReceiver(callReceiver, filter);
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if (callReceiver != null) {
            this.getUQI().getContext().unregisterReceiver(callReceiver);
        }
    }

    public class CallReceiver extends PhonecallReceiver {

        @Override
        protected void onIncomingCallReceived(Context ctx, String number, Date start) {

        }

        @Override
        protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

        }

        @Override
        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            Call call = new Call(null, start.getTime(), normalizePhoneNumber(number),
                    end.getTime() - start.getTime(), Call.TYPE_INCOMING);
            CallUpdatesProvider.this.output(call);
        }

        @Override
        protected void onOutgoingCallStarted(Context ctx, String number, Date start) {

        }

        @Override
        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
            Call call = new Call(null, start.getTime(), normalizePhoneNumber(number),
                    end.getTime() - start.getTime(), Call.TYPE_OUTGOING);
            CallUpdatesProvider.this.output(call);
        }

        @Override
        protected void onMissedCall(Context ctx, String number, Date start) {
            Call call = new Call(null, start.getTime(), normalizePhoneNumber(number),
                    0L, Call.TYPE_MISSED);
            CallUpdatesProvider.this.output(call);
        }
    }
}
