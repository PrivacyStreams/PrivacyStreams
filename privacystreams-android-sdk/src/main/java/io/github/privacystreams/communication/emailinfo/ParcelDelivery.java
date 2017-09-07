package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;


public class ParcelDelivery extends Item {

    /*Fields*/
    @PSItemField(type = String.class)
    public static final String TRACKING_URL = "tracking_url";

    @PSItemField(type = String.class)
    public static final String TRACKING_NUMBER = "tracking_number";

    @PSItemField(type = String.class)
    public static final String EXPECTED_ARRIVAL_UNTIL = "expected_arrival_until";

    @PSItemField(type = JsonNode.class)
    public static final String ITEM_SHIPPED = "item_shipped";

    @PSItemField(type = JsonNode.class)
    public static final String PROVIDER = "provider";

    @PSItemField(type = JsonNode.class)
    public static final String PART_OF_ORDER = "part_of_order";

    public static PStreamProvider getParcelDeliverys(String api_key, String api_secret) {
        return new ParcelDeliveryProvider(api_key, api_secret);
    }

    //TODO delete this method when debug ends
    public static PStreamProvider getParcelDeliverys(String api_key, String api_secret, String userName) {
        return new ParcelDeliveryProvider(api_key, api_secret, userName);
    }
}
