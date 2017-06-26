package com.github.privacystreams.calendar;

import android.Manifest;
import android.content.IntentFilter;
import android.icu.util.Calendar;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.transformations.filter.Filters;

/**
 * provide updates of calender
 */

public class CalendarUpdatesProvider extends MStreamProvider{
    private CalendarUpdatesReceiver calendarUpdatesReceiver;
    UQI uqi = new UQI(getContext());
    public CalendarUpdatesProvider(){
        this.addRequiredPermissions(Manifest.permission.READ_CALENDAR);
    }
    @Override
    public void provide(){
        calendarUpdatesReceiver = new CalendarUpdatesReceiver();
        IntentFilter filter = new IntentFilter();
        MStream calendarEvents = uqi.getData(CalendarEvent.getAll(), Purpose.FEATURE("to get update information of calendar"));

//        this.getUQI().getContext().registerReceiver(calendarUpdatesReceiver, )
    }

}