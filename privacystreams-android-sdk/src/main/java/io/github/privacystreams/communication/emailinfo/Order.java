package io.github.privacystreams.communication.emailinfo;


import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class Order extends Item {

    /*Fields*/
    @PSItemField(type = JsonNode.class)
    public static final String ACCEPTED_OFFER = "accepted_offer";

    @PSItemField(type = String.class)
    public static final String ORDER_NUMBER = "order_number";

    @PSItemField(type = String.class)
    public static final String DESCRIPTION = "description";

    @PSItemField(type = JsonNode.class)
    public static final String PART_OF_INVOICE = "part_of_invoice";

    @PSItemField(type = JsonNode.class)
    public static final String SELLER = "seller";

    @PSItemField(type = JsonNode.class)
    public static final String BROKER = "broker";

    @PSItemField(type = String.class)
    public static final String PAYMENT_SCHEDULED = "payment-scheduled";

    @PSItemField(type = String.class)
    public static final String PRICE = "price";

    @PSItemField(type = String.class)
    public static final String SUBTOTAL = "sub_total";

    @PSItemField(type = String.class)
    public static final String REFUND = "refund";

    public static PStreamProvider getOrder(String api_key, String api_secret) {
        return new OnlineOrderProvider(api_key, api_secret);
    }

    //TODO delete this method when debug ends
    public static PStreamProvider getOrder(String api_key, String api_secret, String userName) {
        return new OnlineOrderProvider(api_key, api_secret, userName);
    }

}
