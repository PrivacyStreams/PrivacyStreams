package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.exceptions.PermissionDeniedException;
import com.github.privacystreams.core.exceptions.PrivacyStreamsException;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Set;


/**
 * Created by yuanchun on 02/11/2016.
 * Universal query interface for personal data access
 */

public class UQI {
    private Context context;
    public Context getContext() {
        return this.context;
    }
    public void setContext(Context context) { this.context = context; }

    private Gson gson;
    public Gson getGson() {
        return this.gson;
    }

    private Purpose purpose;
    public Purpose getPurpose() {
        return this.purpose;
    }

    private Function<Void, Void> query;
    public Function<Void, Void> getQuery() {
        return this.query;
    }
    void setQuery(Function<Void, Void> query) { this.query = query; }

    private String uuid;
    public String getUUID() {
        if (this.uuid == null) {
            // TODO change this to other identifier as using device id is not privacy friendly.
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            this.uuid = tm.getDeviceId();
            this.uuid = "uuid_test";
        }
        return this.uuid;
    }

    private PrivacyStreamsException exception;
    public PrivacyStreamsException getException() {
        return exception;
    }

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
        return new MultiItemStream(uqi, mStreamProvider);
    }

    /**
     * Get a personal data item from a provider with a purpose
     * @param sStreamProvider the function to provide the personal data item, e.g. Location.asLastKnown(), Audio.record(100).
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data item
     */
    public ISingleItemStream getDataItem(SingleItemStreamProvider sStreamProvider, Purpose purpose) {
        UQI uqi = this.getUQIWithPurpose(purpose);
        return new SingleItemStream(uqi, sStreamProvider);
    }

    public void evaluate(boolean retry) {
        Logging.debug("Trying to evaluate PrivacyStreams Query.");
        Logging.debug("Purpose: " + this.purpose);
        Logging.debug("Query: " + this.query);
        Logging.debug("Required Permissions: " + this.query.getRequiredPermissions());

        if (PermissionUtils.checkPermissions(this.context, this.query.getRequiredPermissions())) {
            Logging.debug("Evaluating...");
            this.query.apply(this, null);
        }
        else if (retry) {
            // If retry is true, try to request permissions
            Logging.debug("Permission denied, retrying...");
            PermissionUtils.requestPermissionAndEvaluate(this);
        }
        else {
            // If retry is false, cancel all functions.
            Logging.debug("Permission denied, cancelling...");
            Set<String> deniedPermissions = PermissionUtils.getDeniedPermissions(this.context, this.query.getRequiredPermissions());
            this.exception = new PermissionDeniedException(deniedPermissions.toArray(new String[]{}));
            this.query.cancel(this);
            this.context = null; // remove context
        }
    }
}
