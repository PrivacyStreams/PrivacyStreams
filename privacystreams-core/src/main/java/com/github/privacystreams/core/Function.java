package com.github.privacystreams.core;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuanchun on 14/12/2016.
 * A Function convert a input in type Tin to a output in type Tout
 */

public abstract class Function<Tin, Tout> {

    private Set<String> requiredPermissions;
    private List<Object> parameters;

    public final Set<String> getRequiredPermissions() {
        return this.requiredPermissions;
    }

    protected final List<Object> getParameters() {
        return this.parameters;
    }

    /**
     * Each Function must initialize requiredPermissions field in constructor.
     */
    public Function() {
        this.requiredPermissions = new HashSet<>();
    }

    public abstract Tout apply(UQI uqi, Tin input);

    public <Ttemp> Function<Tin, Ttemp> compound(Function<Tout, Ttemp> function) {
        return new CompoundFunction<>(this, function);
    }

    protected final String getOperator() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        return this.getOperator() + this.getParameters();
    }

}
