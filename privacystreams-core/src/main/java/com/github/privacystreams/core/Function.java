package com.github.privacystreams.core;

import com.github.privacystreams.core.exceptions.PSException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The abstraction of all function in PrivacyStreams.
 * A Function converts a input in type `Tin` to a output in type `Tout`.
 */

public abstract class Function<Tin, Tout> {

    private transient Set<String> requiredPermissions;
    private transient List<Object> parameters;

    protected transient volatile boolean isCancelled;

    public final Set<String> getRequiredPermissions() {
        return this.requiredPermissions;
    }

    protected final List<Object> getParameters() {
        return this.parameters;
    }

    protected void addParameters(Object... parameters) {
        for (Object parameter : parameters) {
            this.parameters.add(parameter);
            if (parameter instanceof Function<?,?>) {
                this.requiredPermissions.addAll(((Function<?,?>) parameter).getRequiredPermissions());
            }
            if (parameter instanceof List<?>) {
                for (Object listItem : (List<?>) parameter) {
                    if (listItem instanceof Function<?, ?>)
                        this.requiredPermissions.addAll(((Function<?, ?>) listItem).getRequiredPermissions());
                }
            }
        }
    }

    protected void addRequiredPermissions(String... permissions) {
        this.requiredPermissions.addAll(Arrays.asList(permissions));
    }

    /**
     * Each Function must initialize requiredPermissions field in constructor.
     */
    public Function() {
        this.requiredPermissions = new HashSet<>();
        this.parameters = new ArrayList<>();
        this.isCancelled = false;
    }

    /**
     * Apply this function
     * @param uqi the instance of UQI
     * @param input the function input
     * @return the function output
     */
    public abstract Tout apply(UQI uqi, Tin input);

    /**
     * Cancel this function
     * @param uqi the instance of UQI
     */
    protected final void cancel(UQI uqi) {
//        Logging.debug("Cancelling: " + this.getClass().getSimpleName());
        this.isCancelled = true;
        for (Object parameter : this.parameters) {
            if (parameter instanceof Function<?,?>) {
                ((Function<?,?>) parameter).cancel(uqi);
            }
        }
//        Logging.debug("onCancel: " + this.getClass().getSimpleName());
        this.onCancel(uqi);
    }

    /**
     * Callback when this function is cancelled
     * @param uqi the instance of UQI
     */
    protected void onCancel(UQI uqi) {
        // Do nothing
    }

    /**
     * Compound this function with another function
     * @param function another function
     * @param <Ttemp> the intermediate variable type between two functions
     * @return the compound function
     */
    public <Ttemp> Function<Tin, Ttemp> compound(Function<Tout, Ttemp> function) {
        return new CompoundFunction<>(this, function);
    }

    public String toString() {
        return this.getClass().getSimpleName() + this.getParameters();
    }

    public Function<Tin, ?> getHead() {
        if (this instanceof CompoundFunction<?, ?, ?>) {
            return ((CompoundFunction<Tin, ?, Tout>) this).getFunction1().getHead();
        }
        else {
            return this;
        }
    }

    public Function<?, Tout> getTail() {
        if (this instanceof CompoundFunction<?, ?, ?>) {
            return ((CompoundFunction<Tin, ?, Tout>) this).getFunction2().getTail();
        }
        else {
            return this;
        }
    }

    <T> boolean startsWith(Function<Tin, T> prefix) {
        if (this == prefix) return true;
        if (this instanceof CompoundFunction<?, ?, ?>) {
            return ((CompoundFunction<Tin, ?, Tout>) this).getFunction1().startsWith(prefix);
        }
        return false;
    }

    <T, Ttemp> Function<? super T, Tout> removeStart(Function<Tin, T> prefix) {
        if (this instanceof CompoundFunction<?, ?, ?>) {
            Function<Tin, ? extends Ttemp> function1 = ((CompoundFunction<Tin, Ttemp, Tout>) this).getFunction1();
            if (function1 == prefix) return ((CompoundFunction<Tin, T, Tout>) this).getFunction2();
            Function<? super T, ? extends Ttemp> function1New = function1.removeStart(prefix);
            if (function1New == null) return null;
            return new CompoundFunction<>(function1New, ((CompoundFunction<Tin, Ttemp, Tout>) this).getFunction2());
        }
        return null;
    }

    protected final void raiseException(UQI uqi, PSException psException) {
        uqi.cancelQueriesWithException(this, psException);
    }

    boolean containsFunction(Function<?, ?> function) {
        if (this == function) return true;
        for (Object parameter : this.parameters) {
            if (parameter instanceof Function<?,?> && ((Function<?, ?>) parameter).containsFunction(function)) {
                return true;
            }
        }
        return false;
    }

}
