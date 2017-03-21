package com.github.privacystreams.communication;

import android.Manifest;
import android.database.Cursor;
import android.provider.CallLog;
import android.telecom.Call;
import android.util.Log;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;

import java.util.Calendar;

import static android.R.attr.type;


/**
 * Created by yuanchun on 21/11/2016.
 * a stream of call logs
 */

class PhonecallLogProvider extends MStreamProvider {

    PhonecallLogProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CALL_LOG);
    }

    String callLogTypeInString(int type){
        String typeString = null;
        switch (type){
            case CallLog.Calls.OUTGOING_TYPE:
                typeString = Phonecall.Types.OUTGOING;
                break;
            case CallLog.Calls.INCOMING_TYPE:
                typeString = Phonecall.Types.INCOMING;
                break;

            case CallLog.Calls.MISSED_TYPE:
                typeString = Phonecall.Types.MISSED;
                break;
        }
        return typeString;
    }

    private void getPhoneLogs(){
        Cursor c;
        c = this.getContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls._ID,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.DATE,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DURATION},
                null,
                null,
                null
        );
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    while (!c.isAfterLast()) {
                        String id = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                        String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                        String date = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
                        String typeString = callLogTypeInString(
                                c.getInt(c.getColumnIndex(CallLog.Calls.TYPE)));
                        String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));

                        output(new Phonecall(id, Long.valueOf(date),
                                CommunicationUtils.normalizePhoneNumber(number),
                                Long.valueOf(duration),
                                typeString));
                        c.moveToNext();
                    }

                }
                c.moveToNext();
            }
        }
        c.close();

    }
    @Override
    protected void provide() {
        getPhoneLogs();
        PhonecallLogProvider.this.finish();
    }

}
