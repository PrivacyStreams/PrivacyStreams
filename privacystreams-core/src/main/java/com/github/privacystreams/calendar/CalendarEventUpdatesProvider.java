package com.github.privacystreams.calendar;

import android.Manifest;
import android.content.IntentFilter;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * listens to changes in calendar.
 * Uses CalendarListProvider to get lists of calendars stored in mobile
 * it returns calendar has been changed while a change happens
 * the returned calendar's status field would be set as one of "added, deleted and edited" according
 * to what change had been made
 *
 * this provider is tested on huawei Nexus 6p, huawei p9 lite, and samsung galaxy s8,
 * but it does not work on the samsung galaxy s7 used to test, because the calendar list taken from
 * that device using CalendarEventListProvider contains all the calendar events that have been deleted
 */

public class CalendarEventUpdatesProvider extends MStreamProvider {
    private List calendarEventsList;
    private CalendarUpdatesReceiver calendarUpdatesReceiver;

    public CalendarEventUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CALENDAR);
    }

    @Override
    public void provide() {
        calendarUpdatesReceiver = new CalendarUpdatesReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.PROVIDER_CHANGED");
        filter.addDataAuthority("com.android.calendar", null);
        filter.addDataScheme("content");
        filter.setPriority(1000);

        getContext().registerReceiver(calendarUpdatesReceiver, filter);

        UQI uqi = new UQI(getContext());
        try {
            calendarEventsList = uqi.getData(CalendarEvent.getAll(),
                            Purpose.FEATURE("to get update information of calendar")).asList();
        } catch (PSException e) {
            e.printStackTrace();
            Logging.error("data getting from uqi is not valid");
        }
        uqi.stopAll();
    }

    @Override
    protected void onCancel(UQI uqi){
        this.getContext().unregisterReceiver(calendarUpdatesReceiver);
    }

    public class CalendarUpdatesReceiver extends CalendarEventUpdatesReceiver{

        @Override
        public void onCalendarEventsUpdatesReceived(){
            List newCalendarEventsList = null;
            UQI uqi = new UQI(getContext());
            //gets new calendar list after change
            try {
                newCalendarEventsList = uqi
                        .getData(CalendarEvent.getAll(),
                                Purpose.FEATURE("get updated calendar information")).asList();
            } catch (PSException e) {
                e.printStackTrace();
                Logging.error("updated data getting from uqi is not valid");
            }
            uqi.stopAll();
            List oldCalendarEventsList = new ArrayList(calendarEventsList);
            CalendarEvent editedCalendarEvent =
                    outputChangedCalendarEvent(oldCalendarEventsList, newCalendarEventsList);
            if (editedCalendarEvent!=null){
                CalendarEventUpdatesProvider.this.output(editedCalendarEvent);
            }
        }
    }

    /**
     * compares two calendar lists to see if a calendar event is added, deleted or edited.
     * @param oldCalendarEventsList a deep copy of the calendar list before change
     * @param newCalendarEventsList calendar list after change
     * @return changed calendar event
     */

    private CalendarEvent outputChangedCalendarEvent(List oldCalendarEventsList, List newCalendarEventsList){
        //add
        if(oldCalendarEventsList.size()<newCalendarEventsList.size()){
            CalendarEvent changedCalendarEvent;
            changedCalendarEvent = (CalendarEvent) newCalendarEventsList
                    .get(newCalendarEventsList.size()-1);
            changedCalendarEvent.setFieldValue(CalendarEvent.STATUS, CalendarEvent.STATUS_ADDED);
            calendarEventsList = newCalendarEventsList;
            return changedCalendarEvent;
        }
        //delete
        else if(oldCalendarEventsList.size()>newCalendarEventsList.size()){
            int calendarIndex =0;
            while(calendarIndex<newCalendarEventsList.size()){
                CalendarEvent calendarEvent = (CalendarEvent) oldCalendarEventsList.get(calendarIndex);
                long id = calendarEvent.getValueByField(CalendarEvent.ID);
                CalendarEvent newCalendarEvent =
                        (CalendarEvent) newCalendarEventsList.get(calendarIndex);
                long newId = newCalendarEvent.getValueByField(CalendarEvent.ID);
                if(id!=newId) {
                    calendarEvent.setFieldValue(CalendarEvent.STATUS, CalendarEvent.STATUS_DELETED);
                    calendarEventsList = newCalendarEventsList;
                    return calendarEvent;
                }
                calendarIndex++;
            }
            //when the deleted item is what created last
            CalendarEvent calendarEvent
                    = (CalendarEvent) oldCalendarEventsList.get(oldCalendarEventsList.size()-1);
            calendarEvent.setFieldValue(CalendarEvent.STATUS, CalendarEvent.STATUS_DELETED);
            calendarEventsList = newCalendarEventsList;
            return calendarEvent;
        }
        //edit
        else{
            int calendarIndex = 0;
            while(calendarIndex<newCalendarEventsList.size()){
                CalendarEvent calendarEvent = (CalendarEvent) oldCalendarEventsList.get(calendarIndex);
                CalendarEvent newCalendarEvent
                        = (CalendarEvent) newCalendarEventsList.get(calendarIndex);
                //refresh the create time of calendarEvent for the coming comparison
                long newTimeCreated = newCalendarEvent.getValueByField(CalendarEvent.TIME_CREATED);
                calendarEvent.setFieldValue(CalendarEvent.TIME_CREATED, newTimeCreated);
                if(!calendarEvent.equals(newCalendarEvent)){
                    newCalendarEvent.setFieldValue(CalendarEvent.STATUS, CalendarEvent.STATUS_EDITED);
                    calendarEventsList = newCalendarEventsList;
                    return newCalendarEvent;
                }
                calendarIndex++;
            }
        }
        calendarEventsList = newCalendarEventsList;
        return null;
    }
}
