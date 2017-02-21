package com.github.privacystreams.core;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanchun on 14/12/2016.
 * A Function convert a input in type Tin to a output in type Tout
 */

public abstract class Function<Tin, Tout> {

//    public final Tout apply(Tin input) {
//        return this.apply(null, input);
//    }

    public abstract Tout apply(UQI uqi, Tin input);

    public <Ttemp> Function<Tin, Ttemp> compound(Function<Tout, Ttemp> function) {
        return new CompoundFunction<>(this, function);
    }

    protected final String getOperator() {
        return this.getClass().getSimpleName();
    }

    protected abstract List<Object> getParameters();

    public final Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("operator", this.getOperator());
        result.put("parameters", this.getParameters());
        return result;
    }

    public final JSONObject toJson() {
        return new JSONObject(this.toMap());
    }

    public String toString() {
        return this.getOperator() + this.getParameters();
    }

}
