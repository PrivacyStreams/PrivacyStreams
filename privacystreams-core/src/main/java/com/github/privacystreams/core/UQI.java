package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by yuanchun on 02/11/2016.
 * Universal query interface for personal data access
 */

public class UQI {
    private Context context;
    private Gson gson;
    private Purpose purpose;
    private Function<Void, Void> query;
    private String uuid;

    public Context getContext() {
        return this.context;
    }
    public Gson getGson() {
        return this.gson;
    }
    public Purpose getPurpose() {
        return this.purpose;
    }
    public Function<Void, Void> getQuery() {
        return this.query;
    }
    public String getUUID() {
        if (this.uuid == null) {
            // TODO change this to other identifier as using device id is not privacy friendly.
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            this.uuid = tm.getDeviceId();
            this.uuid = "uuid_test";
        }
        return this.uuid;
    }
    void setQuery(Function<Void, Void> query) { this.query = query; }

    public UQI(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.purpose = null;
        this.query = null;
        this.uuid = null;
    }

    private UQI(Context context, Purpose purpose) {
        this(context);
        this.purpose = purpose;
    }

    private UQI getUQIWithPurpose(Purpose newPurpose) {
        if (this.purpose == null) this.purpose = newPurpose;
        if (this.purpose == newPurpose) return this;
        return new UQI(this.context, newPurpose);
    }

    /**
     * Get a personal data stream from a provider with a purpose
     * @param mStreamProvider the function to provide the personal data stream, e.g. Location.asUpdates(), SMS.asHistory().
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data stream
     */
    public IMultiItemStream getDataItems(MultiItemStreamProvider mStreamProvider, Purpose purpose) {
        UQI uqi = this.getUQIWithPurpose(purpose);
        return mStreamProvider.apply(uqi, null);
    }

    /**
     * Get a personal data item from a provider with a purpose
     * @param sStreamProvider the function to provide the personal data item, e.g. Location.asLastKnown(), Audio.record(100).
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data item
     */
    public ISingleItemStream getDataItem(SingleItemStreamProvider sStreamProvider, Purpose purpose) {
        UQI uqi = this.getUQIWithPurpose(purpose);
        return sStreamProvider.apply(uqi, null);
    }

    public void evaluate(boolean retry) {
        Logging.debug("Trying to evaluate PrivacyStreams Query.");
        Logging.debug("Purpose: " + this.purpose);
        Logging.debug("Query: " + this.query);
        Logging.debug("Required Permissions: " + this.query.getRequiredPermissions());

        if (PermissionUtils.checkPermissions(this.context, this.query.getRequiredPermissions())) {
            this.query.apply(this, null);
        }
        else if (retry) {
            // If retry is true, try to request permissions
            PermissionUtils.requestPermissionAndEvaluate(this);
        }
        else {
            // If retry is false, notify all functions permission denied.
            // TODO
        }
    }

}
