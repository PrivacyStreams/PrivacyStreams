package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class FoodEstablishmentReservation extends Item {


    /*Fields*/
    @PSItemField(type = String.class)
    public static final String RESERVATION_STATUS = "reservation_status";

    @PSItemField(type = String.class)
    public static final String RESERVATION_ID = "reservation_id";

    @PSItemField(type = JsonNode.class)
    public static final String RESERVATION_FOR = "reservation_for";


    @PSItemField(type = String.class)
    public static final String PARTY_SIZE = "party_size";

    @PSItemField(type = String.class)
    public static final String START_TIME = "start_time";

    @PSItemField(type = JsonNode.class)
    public static final String PROVIDER = "provider";

    public static PStreamProvider getFoodEstablishmentReservations(String api_key, String api_secret){
        return new FoodEstablishmentReservationProvider(api_key, api_secret);
    }

    //TODO delete this method when debug ends
    public static PStreamProvider getFoodEstablishmentReservations(String api_key, String api_secret, String userName){
        return new FoodEstablishmentReservationProvider(api_key, api_secret, userName);
    }
}
