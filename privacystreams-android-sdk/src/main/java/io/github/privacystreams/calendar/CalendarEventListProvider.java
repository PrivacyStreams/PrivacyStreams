package io.github.privacystreams.calendar;

import android.Manifest;
import android.database.Cursor;
import android.provider.CalendarContract;
import androidx.annotation.RequiresPermission;

import java.util.Calendar;

import io.github.privacystreams.core.PStreamProvider;

/**
 * Provide existing calendar events.
 */

class CalendarEventListProvider extends PStreamProvider {

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

    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    private void getCalendarInfo() {

        Cursor c;
        c = this.getContext().getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,

                new String[]{CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
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
                Long endTime = c.getLong(c.getColumnIndex(CalendarContract.Events.DTEND));
                String location = c.getString(c.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                String duration = c.getString(c.getColumnIndex(CalendarContract.Events.DURATION));
                CalendarEvent calendarEvent = new CalendarEvent(id, title, startTime, endTime, duration, location);
                output(calendarEvent);

                c.moveToNext();
            }
            c.close();
        }

    }

    @Override
    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    protected void provide() {
        getCalendarInfo();
        CalendarEventListProvider.this.finish();
    }
}
