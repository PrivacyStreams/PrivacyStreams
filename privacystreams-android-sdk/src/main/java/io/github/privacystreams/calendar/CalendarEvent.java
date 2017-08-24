package io.github.privacystreams.calendar;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

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
     * Event end time.
     */
    @PSItemField(type = Long.class)
    public static final String END_TIME = "end_time";
    /**
     * Duration of the event, in RFC2445 format.
     */
    @PSItemField(type = String.class)
    public static final String DURATION = "duration";

    /**
     * Event location.
     */
    @PSItemField(type = String.class)
    public static final String EVENT_LOCATION = "event_location";

    public static final String STATUS_ADDED = "added";
    public static final String STATUS_DELETED = "deleted";
    public static final String STATUS_EDITED = "edited";

    /**
     * Event status.
     */
    @PSItemField(type = String.class)
    public static String STATUS = "status";

    CalendarEvent(long id, String title, long startTime, long endTime, String duration, String eventLocation) {

        this.setFieldValue(ID, id);
        this.setFieldValue(TITLE, title);
        this.setFieldValue(START_TIME, startTime);
        this.setFieldValue(END_TIME, endTime);
        this.setFieldValue(DURATION, duration);
        this.setFieldValue(EVENT_LOCATION, eventLocation);
        this.setFieldValue(STATUS, STATUS_ADDED);
    }

    CalendarEvent(CalendarEvent another) {
        for (String key : another.toMap().keySet()) {
            this.setFieldValue(key, another.getValueByField(key));
        }

    }

    /**
     * Provide all CalendarEvent items from device's calendar database.
     * This provider requires `android.permission.READ_CALENDAR` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_CALENDAR)
    public static PStreamProvider getAll() {
        return new CalendarEventListProvider();
    }

    public static PStreamProvider getUpdates() {
        return new CalendarEventUpdatesProvider();
    }
}
