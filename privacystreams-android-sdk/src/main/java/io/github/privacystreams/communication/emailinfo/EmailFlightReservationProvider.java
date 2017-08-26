package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.communication.EmailInfoProvider;

public class EmailFlightReservationProvider extends EmailInfoProvider {
    private static final String REQUEST_DOMAIN = "flight";

    public EmailFlightReservationProvider(String api_key, String api_secret){
        super(api_key,api_secret,REQUEST_DOMAIN);
    }

    @Override
    protected void provide() {
        super.provide();
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode){
        getFlightInfo(jsonNode);
    }

    private void getFlightInfo(JsonNode jsonNode){
        FlightReservation flight = new FlightReservation();
        flight.setFieldValue(FlightReservation.RESERVATION_ID,jsonNode.get("reservationId").toString());
        flight.setFieldValue(FlightReservation.RESERVATION_STATUS,jsonNode.get("reservationStatus").toString());
        flight.setFieldValue(FlightReservation.RESERVATION_FOR,jsonNode.get("reservationFor"));
        flight.setFieldValue(FlightReservation.RESERVED_TICKET,jsonNode.get("reservedTicket"));
        flight.setFieldValue(FlightReservation.PROGRAM_MEMBERSHIP_USED,jsonNode.get("programMembershipUsed"));
        flight.setFieldValue(FlightReservation.BROKER,jsonNode.get("broker"));
        output(flight);
    }

}
