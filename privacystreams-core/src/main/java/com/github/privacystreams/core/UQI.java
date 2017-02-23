package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.privacystreams.core.purposes.Purpose;

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

    /**
     * Get a personal data stream from a provider with a purpose
     * @param mStreamProvider the function to provide the personal data stream, e.g. Location.asUpdates(), SMS.asHistory().
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data stream
     */
    public IMultiItemStream getDataItems(MultiItemStreamProvider mStreamProvider, Purpose purpose) {
        this.purpose = purpose;
        return mStreamProvider.apply(this, null);
    }

    /**
     * Get a personal data item from a provider with a purpose
     * @param sStreamProvider the function to provide the personal data item, e.g. Location.asLastKnown(), Audio.record(100).
     * @param purpose the purpose of personal data use, e.g. Purpose.ads("xxx").
     * @return the personal data item
     */
    public ISingleItemStream getDataItem(SingleItemStreamProvider sStreamProvider, Purpose purpose) {
        this.purpose = purpose;
        return sStreamProvider.apply(this, null);
    }

    <Tout, TStream extends Stream> Tout evaluate(LazyFunction<Void, TStream> streamProvider,
                                                 TStream stream,
                                                 Function<TStream, Tout> streamAction) {

        // TODO add user authentication here
        Function<Void, Tout> function = streamProvider.compound(streamAction);
        Logging.debug("PrivacyStreams Query: " + function.toString());

        Set<String> queryRequiredPermissions = function.getRequiredPermissions();
        Logging.debug(queryRequiredPermissions.toString());

        streamProvider.evaluate();
        return streamAction.apply(this, stream);
    }

}
