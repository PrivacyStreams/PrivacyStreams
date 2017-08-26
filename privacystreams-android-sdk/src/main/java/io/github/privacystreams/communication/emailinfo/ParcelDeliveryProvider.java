package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Logging;

public class ParcelDeliveryProvider extends EmailInfoProvider {
    private static final String REQUEST_DOMAIN = "parceldelivery";

    public ParcelDeliveryProvider(String api_key, String api_secret){
        super(api_key,api_secret,REQUEST_DOMAIN);
    }

    @Override
    protected void provide() {
        super.provide();
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode){
        getParcelDeliveryInfo(jsonNode);
    }

    private void getParcelDeliveryInfo(JsonNode jsonNode){
        Logging.error("new parcel");
        ParcelDelivery parcel = new ParcelDelivery();
        parcel.setFieldValue(ParcelDelivery.TRACKING_URL,jsonNode.get("trackingUrl").toString());
        parcel.setFieldValue(ParcelDelivery.TRACKING_NUMBER,jsonNode.get("trackingNumber").toString());
        parcel.setFieldValue(ParcelDelivery.EXPECTED_ARRIVAL_UNTIL,jsonNode.get("expectedArrivalUntil").toString());
        parcel.setFieldValue(ParcelDelivery.ITEM_SHIPPED,jsonNode.get("itemShipped"));
        parcel.setFieldValue(ParcelDelivery.PROVIDER,jsonNode.get("provider"));
        parcel.setFieldValue(ParcelDelivery.PART_OF_ORDER,jsonNode.get("partOfOrder"));
        output(parcel);
    }

}
