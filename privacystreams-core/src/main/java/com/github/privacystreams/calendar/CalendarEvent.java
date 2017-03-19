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
    @PSItemField(type = String.class)
    public static final String ID = "id";

    /**
     * Event title.
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";


    /**
     * Start time.
     */
    @PSItemField(type = Long.class)
    public static final String STARTTIME = "starttime";


    /**
     * Duration.
     * TODO: Change to long type, Use string type for now, in RFC2445 format.
     */
    @PSItemField(type = String.class)
    public static final String DURATION = "duration";

    /**
     * Event location.
     */
    @PSItemField(type = String.class)
    public static final String EVENT_LOCATION = "event_location";

    CalendarEvent(String id, String title, long startime, String duration, String eventLocation) {
        this.setFieldValue(ID, id);
        this.setFieldValue(TITLE, title);
        this.setFieldValue(STARTTIME, startime);
        this.setFieldValue(DURATION, duration);
        this.setFieldValue(EVENT_LOCATION, eventLocation);
    }

    /**
     * Provide a list of Contact items from device's contacts database.
     *
     * @return the provider function.
     */
    public static MStreamProvider asList() {
        return new CalendarEventListProvider();
    }

}
