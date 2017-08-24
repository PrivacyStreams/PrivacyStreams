package io.github.privacystreams.communication.emailinfo;

import java.util.HashMap;
import java.util.Map;

public enum Domain {
    UNKNOWN(0, "Unknown"),
    PURCHASE(1, "Order"),
    SHIPMENT(2, "ParcelDelivery"),
    BILL(3, "Invoice"),
    EVENT(4, "EventReservation"),
    RESTAURANT(5, "FoodEstablishmentReservation"),
    HOTEL(6, "LodgingReservation"),
    TRAIN(7, "TrainReservation"),
    DEAL(8, "Deal"),
    CONTACT(9, "Contact"),
    CAR_RENTAL(10, "RentalCarReservation"),
    FLIGHT(11, "FlightReservation"),
    BOARDING_PASS(12, "BoardingPass"),
    REMINDER(13, "Reminder");

    private final int id;
    private final String name;
    private static Map<String, Domain> constants = new HashMap<>();

    static {
        for (Domain d: values()) {
            constants.put(d.name, d);
        }
    }

    Domain(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Domain getDomain(String type) {
        return constants.get(type);
    }
}
