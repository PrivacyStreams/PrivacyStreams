package com.github.privacystreams.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The abstraction of all function in PrivacyStreams.
 * A Function converts a input in type Tin to a output in type Tout.
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

    // TODO consider the case where parameter is a list
    protected void addParameters(Object... parameters) {
        for (Object parameter : parameters) {
            this.parameters.add(parameter);
            if (parameter instanceof Function<?,?>) {
                this.requiredPermissions.addAll(((Function<?,?>) parameter).getRequiredPermissions());
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
        this.isCancelled = true;
        for (Object parameter : this.parameters) {
            if (parameter instanceof Function<?,?>) {
                ((Function<?,?>) parameter).cancel(uqi);
            }
        }
        this.onCancelled(uqi);
    }

    /**
     * Callback when this function is cancelled
     * @param uqi the instance of UQI
     */
    protected void onCancelled(UQI uqi) {
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

}
