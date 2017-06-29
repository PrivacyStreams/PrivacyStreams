package com.github.privacystreams.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * listen from changes in calendar. It would be called in CalendarUpdatesProvider
 */

abstract class CalendarEventsUpdatesReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        onCalendarEventsUpdatesReceived();
    }

    abstract void onCalendarEventsUpdatesReceived();
}
