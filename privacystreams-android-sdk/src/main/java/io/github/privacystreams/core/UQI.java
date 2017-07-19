package io.github.privacystreams.core;

import android.content.Context;

import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.PermissionUtils;

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
     * Get a PStream from a provider with a purpose.
     * For example, using `uqi.getData(Contact.getLogs(), Purpose.FEATURE("..."))`
     * will return a stream of contacts.
     * @param pStreamProvider the function to provide the personal data stream,
     *                        e.g. Geolocation.asUpdates().
     * @param purpose the purpose of personal data use, e.g. Purpose.ADS("xxx").
     * @return a multi-item stream
     */
    public PStream getData(PStreamProvider pStreamProvider, Purpose purpose) {
        this.provider2Purpose.put(pStreamProvider, purpose);
        return new PStream(this, pStreamProvider);
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

    private transient Map<Function<Void, PStream>, PStream> reusedMProviders = new HashMap<>();
    private transient Set<Function<Void, ? extends Stream>> evaluatedProviders = new HashSet<>();

    /**
     * Reuse a PStream.
     * @param stream the stream to reuse.
     */
    void reuse(PStream stream, int numOfReuses) {
        Function<Void, PStream> reusedProvider = stream.getStreamProvider();
        stream.receiverCount = numOfReuses;
        reusedMProviders.put(reusedProvider, stream);
    }

    private boolean tryReuse(Function<Void, Void> query) {
        if (reusedMProviders != null) {
            for (Function<Void, PStream> provider : reusedMProviders.keySet()) {
                if (query.startsWith(provider)) {
                    Function<? super PStream, Void> newQuery = query.removeStart(provider);
                    if (!this.evaluatedProviders.contains(provider)) {
                        PStream stream = reusedMProviders.get(provider);
                        PStream newStream = provider.apply(this, null);
                        newStream.receiverCount = stream.receiverCount;
                        reusedMProviders.put(provider, newStream);
                        evaluatedProviders.add(provider);
                    }
                    newQuery.apply(this, reusedMProviders.get(provider));
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
