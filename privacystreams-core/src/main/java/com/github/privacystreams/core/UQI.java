package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.permission.PermissionActivity;
import com.github.privacystreams.core.utils.permission.PermissionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.privacystreams.core.purposes.Purpose;

import java.security.acl.Permission;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


/**
 * Created by yuanchun on 02/11/2016.
 * Universal query interface for personal data access
 */

public class UQI {
    private Context context;
    private Gson gson;
    private Purpose purpose;

    public UQI(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.purpose = null;
    }

    private UQI(Context context, Purpose purpose) {
        this(context);
        this.purpose = purpose;
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
        return this.gson;
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

    <Tout, TStream extends Stream> Tout evaluate(LazyFunction<Void, TStream> streamProvider,
                                                 Function<TStream, Tout> streamAction) {

        Function<Void, Tout> function = streamProvider.compound(streamAction);
        Logging.debug("PrivacyStreams Query: " + function.toString());

        Set<String> requiredPermissions = function.getRequiredPermissions();
        Logging.debug("Required Permissions: " + requiredPermissions.toString());

        if (PermissionUtils.checkPermissions(this.context, requiredPermissions)) {
            return function.apply(this, null);
        }
        else {
            // TODO if the permissions are not granted, try request
            PermissionActivity.requestPermissions(this.context, requiredPermissions.toArray(new String[]{}));
            Logging.warn("Permissions not granted, you have to try again after permission granted!");
            return null;
        }
    }

}
