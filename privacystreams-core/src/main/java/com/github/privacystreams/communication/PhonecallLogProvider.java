package com.github.privacystreams.communication;

import android.Manifest;
import android.database.Cursor;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.CommunicationUtils;


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
            case android.provider.CallLog.Calls.OUTGOING_TYPE:
                typeString = CallLog.Types.OUTGOING;
                break;
            case android.provider.CallLog.Calls.INCOMING_TYPE:
                typeString = CallLog.Types.INCOMING;
                break;

            case android.provider.CallLog.Calls.MISSED_TYPE:
                typeString = CallLog.Types.MISSED;
                break;
        }
        return typeString;
    }

    private void getPhoneLogs(){
        Cursor c;
        c = this.getContext().getContentResolver().query(
                android.provider.CallLog.Calls.CONTENT_URI,
                new String[]{android.provider.CallLog.Calls._ID,
                        android.provider.CallLog.Calls.NUMBER,
                        android.provider.CallLog.Calls.DATE,
                        android.provider.CallLog.Calls.TYPE,
                        android.provider.CallLog.Calls.DURATION},
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
                        String id = c.getString(c.getColumnIndex(android.provider.CallLog.Calls._ID));
                        String number = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                        String date = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DATE));
                        String typeString = callLogTypeInString(
                                c.getInt(c.getColumnIndex(android.provider.CallLog.Calls.TYPE)));
                        String duration = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DURATION));

                        output(new CallLog(id, Long.valueOf(date),
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
