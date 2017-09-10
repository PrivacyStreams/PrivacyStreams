package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class FlightReservation extends Item {

    /*Fields*/
    @PSItemField(type = String.class)
    public static final String RESERVATION_ID = "reservation_id";

    @PSItemField(type = String.class)
    public static final String RESERVATION_STATUS = "reservation_status";

    @PSItemField(type = JsonNode.class)
    public static final String RESERVATION_FOR = "reservation_for";

    @PSItemField(type = JsonNode.class)
    public static final String RESERVED_TICKET = "reserved_ticket";

    @PSItemField(type = JsonNode.class)
    public static final String PROGRAM_MEMBERSHIP_USED = "program_membership_used";

    @PSItemField(type = JsonNode.class)
    public static final String BROKER = "broker";

    public static PStreamProvider getFlightReservation(String api_key, String api_secret) {
        return new EmailFlightReservationProvider(api_key, api_secret);
    }

    //TODO delete this function when debug ends
    public static PStreamProvider getFlightReservation(String api_key, String api_secret, String userName) {
        return new EmailFlightReservationProvider(api_key, api_secret, userName);
    }
}
