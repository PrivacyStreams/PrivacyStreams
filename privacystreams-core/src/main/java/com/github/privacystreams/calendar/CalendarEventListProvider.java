package com.github.privacystreams.calendar;

import android.Manifest;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.util.Calendar;

/**
 * Provide existing calendar events.
 */

class CalendarEventListProvider extends MStreamProvider {

    public static boolean isUpcomingToday(long timestamp){
        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.setTimeInMillis(timestamp);


        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        return  eventCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) &&
                eventCalendar.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR) &&
                eventCalendar.after(nowCalendar);
    }


    CalendarEventListProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CALENDAR);
    }


    private void getCalendarInfo() {

        Cursor c;
        c = this.getContext().getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,

                new String[]{CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DURATION},
                null,
                null,
                null
        );

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(
                        CalendarContract.Events._ID));
                String title = c.getString(c.getColumnIndex(
                        CalendarContract.Events.TITLE));
                Long startTime = c.getLong(c.getColumnIndex(CalendarContract.Events.DTSTART));
                String location = c.getString(c.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                Long duration = c.getLong(c.getColumnIndex(CalendarContract.Events.DURATION));
                Log.e("event", title + "," + location);
                CalendarEvent calendarEvent = new CalendarEvent(id, title, startTime, duration, location);
                output(calendarEvent);

                c.moveToNext();
            }
            c.close();
        }

    }

    @Override
    protected void provide() {
        getCalendarInfo();
        CalendarEventListProvider.this.finish();
    }
}
