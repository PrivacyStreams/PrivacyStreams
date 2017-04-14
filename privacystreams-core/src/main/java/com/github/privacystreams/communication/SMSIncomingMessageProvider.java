package com.github.privacystreams.communication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;

/**
 * Provide a live stream of incoming SMS messages
 */

class SMSIncomingMessageProvider extends MStreamProvider {

    private SMSReceiver smsReceiver;

    SMSIncomingMessageProvider() {
        this.addRequiredPermissions(Manifest.permission.RECEIVE_SMS);
    }

    @Override
    protected void provide() {
        smsReceiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.getUQI().getContext().registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if (smsReceiver != null) {
            this.getUQI().getContext().unregisterReceiver(smsReceiver);
        }
    }

    public class SMSReceiver extends BroadcastReceiver {
        // SmsManager class is responsible for all SMS related actions
        final SmsManager sms = SmsManager.getDefault();
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                // Get the SMS message received
                final Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    if (pdusObj == null) return;

                    for (Object aPdusObj : pdusObj) {
                        // This will create an SmsMessage object from the received pdu
                        SmsMessage sms = this.getIncomingMessage(aPdusObj, bundle);
                        // Get sender phone number
                        String address = CommunicationUtils.normalizePhoneNumber(sms.getDisplayOriginatingAddress());
                        String body = sms.getDisplayMessageBody();

                        Message message = new Message(Message.TYPE_RECEIVED, body, "system", address, System.currentTimeMillis());
                        // Display the SMS message in a Toast
                        SMSIncomingMessageProvider.this.output(message);
                    }
                }
            }
        }

        private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
            SmsMessage currentSMS;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
            } else {
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
            }

            return currentSMS;
        }
    }

}
