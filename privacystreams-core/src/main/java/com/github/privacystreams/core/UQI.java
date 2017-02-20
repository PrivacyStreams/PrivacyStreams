package com.github.privacystreams.core;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.privacystreams.core.purposes.Purpose;


/**
 * Created by yuanchun on 02/11/2016.
 * Universal query interface for personal data access
 */

public class UQI {
    private Context context;
    private Gson gson;

    public UQI(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Context getContext() {
        return context;
    }

    private String uuid = null;
    public String getUUID() {
        if (this.uuid == null) {
            // TODO change this to other identifier as using device id is not privacy friendly.
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            this.uuid = tm.getDeviceId();
            this.uuid = "uuid_test";
        }
        return this.uuid;
    }

    public Gson getGson() {
        return gson;
    }

    /**
     * Get a personal data stream from a provider with a purpose
     * @param streamProvider the function to provide the personal data stream, e.g. Location.asUpdates(), SMS.asHistory().
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data stream
     */
    public IMultiItemStream getDataItems(Function<Void, MultiItemStream> streamProvider, Purpose purpose) {
        return streamProvider.apply(this, null);
    }

    /**
     * Get a personal data item from a provider with a purpose
     * @param itemProvider the function to provide the personal data item, e.g. Location.asLastKnown(), Audio.record(100).
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data item
     */
    public ISingleItemStream getDataItem(Function<Void, SingleItemStream> itemProvider, Purpose purpose) {
        return itemProvider.apply(this, null);
    }

}
