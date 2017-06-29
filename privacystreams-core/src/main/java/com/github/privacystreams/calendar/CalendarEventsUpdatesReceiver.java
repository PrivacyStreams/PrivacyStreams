package com.github.privacystreams.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.ArrayList;
import java.util.List;

/**
 * listen from changes in calendar. It would be called in CalendarUpdatesProvider
 */

abstract class CalendarEventsUpdatesReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("process", "change happened");
//        calendarCallback.callIfReceive();
        ifReceive();
    }

    abstract void ifReceive();
}
