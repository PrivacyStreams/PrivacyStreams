package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

public class ParcelDeliveryProvider extends EmailInfoProvider {
    private static final String REQUEST_DOMAIN = "shipment";

    public ParcelDeliveryProvider(String api_key, String api_secret) {
        super(api_key, api_secret, REQUEST_DOMAIN);
    }

    //TODO delete this constructor when debug ends
    public ParcelDeliveryProvider(String api_key, String api_secret, String userName) {
        super(api_key, api_secret, REQUEST_DOMAIN, userName);
    }

    @Override
    protected void provide() {
        super.provide();
        finish();
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode) {
        getParcelDeliveryInfo(jsonNode);
    }

    private void getParcelDeliveryInfo(JsonNode jsonNode) {
        ParcelDelivery parcel = new ParcelDelivery();
        parcel.setFieldValue(ParcelDelivery.TRACKING_URL, jsonNode.get("trackingUrl").toString());
        parcel.setFieldValue(ParcelDelivery.TRACKING_NUMBER, jsonNode.get("trackingNumber").toString());
        parcel.setFieldValue(ParcelDelivery.EXPECTED_ARRIVAL_UNTIL, jsonNode.get("expectedArrivalUntil").toString());
        parcel.setFieldValue(ParcelDelivery.ITEM_SHIPPED, jsonNode.get("itemShipped"));
        parcel.setFieldValue(ParcelDelivery.PROVIDER, jsonNode.get("provider"));
        parcel.setFieldValue(ParcelDelivery.PART_OF_ORDER, jsonNode.get("partOfOrder"));
        output(parcel);
    }

}
