package com.github.privacystreams.calendar;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * The meta information for a calendar event.
 */
@PSItem
public class CalendarEvent extends Item {
    /**
     * Event ID.
     */
    @PSItemField(type = Long.class)
    public static final String ID = "id";

    /**
     * Event title.
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";


    /**
     * Event start time.
     */
    @PSItemField(type = Long.class)
    public static final String START_TIME = "start_time";


    /**
     * Duration of the event.
     */
    @PSItemField(type = Long.class)
    public static final String END_TIME = "end_time";

    /**
     * Event location.
     */
    @PSItemField(type = String.class)
    public static final String EVENT_LOCATION = "event_location";

    /**
     * Event status.
     */
    @PSItemField(type = String.class)
    public static String STATUS = "status";

    CalendarEvent(long id, String title, long startTime, long endTime, String eventLocation) {
        this.setFieldValue(ID, id);
        this.setFieldValue(TITLE, title);
        this.setFieldValue(START_TIME, startTime);
        this.setFieldValue(END_TIME, endTime);
        this.setFieldValue(EVENT_LOCATION, eventLocation);
        this.setFieldValue(STATUS, "added");
    }

    CalendarEvent(CalendarEvent another){
        this.setFieldValue(CalendarEvent.ID, another.getValueByField(CalendarEvent.ID));
        this.setFieldValue(CalendarEvent.STATUS, another.getValueByField(CalendarEvent.STATUS));
        this.setFieldValue(CalendarEvent.END_TIME, another.getValueByField(CalendarEvent.END_TIME));
        this.setFieldValue(CalendarEvent.EVENT_LOCATION, another.getValueByField(CalendarEvent.EVENT_LOCATION));
        this.setFieldValue(CalendarEvent.START_TIME, another.getValueByField(CalendarEvent.START_TIME));
        this.setFieldValue(CalendarEvent.TITLE, another.getValueByField(CalendarEvent.TITLE));
    }

    /**
     * Provide all CalendarEvent items from device's calendar database.
     * This provider requires `android.permission.READ_CALENDAR` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_CALENDAR)
    public static MStreamProvider getAll() {
        return new CalendarEventListProvider();
    }
    public static MStreamProvider getUpdates() {
        return new CalendarEventUpdatesProvider();
    }
}
