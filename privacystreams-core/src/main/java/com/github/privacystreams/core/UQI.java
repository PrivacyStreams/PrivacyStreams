package com.github.privacystreams.core;

import android.content.Context;

import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.PermissionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * The unified query interface for all kinds of personal data.
 * You will need to construct an UQI with `UQI uqi = new UQI(context);`
 * To get a stream of personal data, simply call `uqi.getData` with a Stream provider.
 */

public class UQI {
    private Map<Function<Void, ?>, Purpose> provider2Purpose;
    private Set<Function<Void, Void>> queries;

    private Purpose getPurposeOfQuery(Function<Void, Void> query) {
        return this.provider2Purpose.get(query.getHead());
    }

    private transient Context context;
    public Context getContext() {
        return this.context;
    }
    public void setContext(Context context) { this.context = context; }

    private transient PSException exception;
    public PSException getException() {
        return exception;
    }

    public UQI(Context context) {
        this.context = context;
        this.provider2Purpose = new HashMap<>();
        this.queries = new HashSet<>();
    }

    /**
     * Get a MStream from a provider with a purpose.
     * For example, using `uqi.getData(Contact.getLogs(), Purpose.FEATURE("..."))`
     * will return a stream of contacts.
     * @param mStreamProvider the function to provide the personal data stream,
     *                        e.g. Geolocation.asUpdates().
     * @param purpose the purpose of personal data use, e.g. Purpose.ADS("xxx").
     * @return a multi-item stream
     */
    public MStream getData(MStreamProvider mStreamProvider, Purpose purpose) {
        this.provider2Purpose.put(mStreamProvider, purpose);
        return new MStream(this, mStreamProvider);
    }

    /**
     * Get an SStream from a provider with a purpose.
     * For example, using `uqi.getData(Geolocation.asLastKnown(), Purpose.FEATURE("..."))`
     * will return a stream that contains one location item.
     * @param sStreamProvider the function to provide the personal data item,
     *                        e.g. Location.asLastKnown(), Audio.record(100).
     * @param purpose the purpose of personal data use, e.g. Purpose.ADS("xxx").
     * @return a single-item stream
     */
    public SStream getData(SStreamProvider sStreamProvider, Purpose purpose) {
        this.provider2Purpose.put(sStreamProvider, purpose);
        return new SStream(this, sStreamProvider);
    }

    /**
     * Stop all query in this UQI.
     */
    public void stopAll() {
        Logging.debug("Trying to stop all PrivacyStreams Queries.");

        this.exception = PSException.INTERRUPTED("Stopped by app.");
        for (Function<Void, Void> query : queries) {
            query.cancel(this);
        }
    }

    private transient Map<Function<Void, MStream>, MStream> reusedMProviders = new HashMap<>();
    private transient Map<Function<Void, SStream>, SStream> reusedSProviders = new HashMap<>();
    private transient Set<Function<Void, ? extends Stream>> evaluatedProviders = new HashSet<>();

    /**
     * Reuse a MStream.
     * @param stream the stream to reuse.
     */
    void reuse(MStream stream, int numOfReuses) {
        Function<Void, MStream> reusedProvider = stream.getStreamProvider();
        stream.receiverCount = numOfReuses;
        reusedMProviders.put(reusedProvider, stream);
    }

    /**
     * Reuse a SStream.
     * @param stream the stream to reuse.
     */
    void reuse(SStream stream, int numOfReuses) {
        Function<Void, SStream> reusedProvider = stream.getStreamProvider();
        stream.receiverCount = numOfReuses;
        reusedSProviders.put(reusedProvider, stream);
    }

    private boolean tryReuse(Function<Void, Void> query) {
        if (reusedMProviders != null) {
            for (Function<Void, MStream> provider : reusedMProviders.keySet()) {
                if (query.startsWith(provider)) {
                    Function<? super MStream, Void> newQuery = query.removeStart(provider);
                    if (!this.evaluatedProviders.contains(provider)) {
                        MStream stream = reusedMProviders.get(provider);
                        MStream newStream = provider.apply(this, null);
                        newStream.receiverCount = stream.receiverCount;
                        reusedMProviders.put(provider, newStream);
                        evaluatedProviders.add(provider);
                    }
                    newQuery.apply(this, reusedMProviders.get(provider));
                    return true;
                }
            }
        }
        if (reusedSProviders != null) {
            for (Function<Void, SStream> provider : reusedSProviders.keySet()) {
                if (query.startsWith(provider)) {
                    Function<? super SStream, Void> newQuery = query.removeStart(provider);
                    if (!this.evaluatedProviders.contains(provider)) {
                        SStream stream = reusedSProviders.get(provider);
                        SStream newStream = provider.apply(this, null);
                        newStream.receiverCount = stream.receiverCount;
                        reusedSProviders.put(provider, newStream);
                        evaluatedProviders.add(provider);
                    }
                    newQuery.apply(this, reusedSProviders.get(provider));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evaluate current UQI.
     *
     * @param query the query to evaluate.
     * @param retry whether to try again if the permission is denied.
     */
    public void evaluate(Function<Void, Void> query, boolean retry) {
        Logging.debug("Trying to evaluate query {" + query + "}. Purpose {" + this.getPurposeOfQuery(query) + "}");
        Logging.debug("Required Permissions: " + query.getRequiredPermissions());

        this.queries.add(query);

        if (PermissionUtils.checkPermissions(this.context, query.getRequiredPermissions())) {
            Logging.debug("Permission granted, evaluating...");
            boolean reused = this.tryReuse(query);
            if (!reused) query.apply(this, null);
            Logging.debug("Evaluated.");
        }
        else if (retry) {
            // If retry is true, try to request permissions
            Logging.debug("Permission denied, retrying...");
            PermissionUtils.requestPermissionAndEvaluate(this, query);
        }
        else {
            // If retry is false, cancel all functions.
            Logging.debug("Permission denied, cancelling...");
            Set<String> deniedPermissions = PermissionUtils.getDeniedPermissions(this.context, query.getRequiredPermissions());
            this.exception = PSException.PERMISSION_DENIED(deniedPermissions);
            query.cancel(this);
//            this.context = null; // remove context
            Logging.debug("Cancelled.");
        }
    }

    /**
     * Cancel a query with an exception.
     */
    void cancelQueriesWithException(Function<?, ?> function, PSException exception) {
        this.exception = exception;
        for (Function<Void, Void> query : this.queries) {
            if (query.containsFunction(function)) query.cancel(this);
        }
    }
}
