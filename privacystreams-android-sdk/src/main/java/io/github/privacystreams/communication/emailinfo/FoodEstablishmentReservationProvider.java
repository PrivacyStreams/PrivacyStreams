package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.utils.Logging;


public class FoodEstablishmentReservationProvider extends EmailInfoProvider {
    private static final String REQUEST_DOMAIN = "restaurant";

    @Override
    protected void provide() {
        super.provide();
    }

    public FoodEstablishmentReservationProvider(String api_key, String api_secret){
        super(api_key,api_secret,REQUEST_DOMAIN);
    }

    //TODO delete this constructor when debug ends
    public FoodEstablishmentReservationProvider(String api_key, String api_secret, String userName){
        super(api_key,api_secret,REQUEST_DOMAIN, userName);
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode){
        getFoodReservation(jsonNode);
    }

    private void getFoodReservation(JsonNode jsonNode){
        FoodEstablishmentReservation food = new FoodEstablishmentReservation();
        food.setFieldValue(FoodEstablishmentReservation.RESERVATION_STATUS,jsonNode.get("reservationStatus").toString());
        food.setFieldValue(FoodEstablishmentReservation.RESERVATION_ID,jsonNode.get("reservationId").toString());
        food.setFieldValue(FoodEstablishmentReservation.RESERVATION_FOR,jsonNode.get("reservationFor"));
        food.setFieldValue(FoodEstablishmentReservation.PARTY_SIZE,jsonNode.get("partySize").toString());
        food.setFieldValue(FoodEstablishmentReservation.START_TIME,jsonNode.get("startTime").toString());
        food.setFieldValue(FoodEstablishmentReservation.PROVIDER,jsonNode.get("provider"));
        output(food);
    }
}
