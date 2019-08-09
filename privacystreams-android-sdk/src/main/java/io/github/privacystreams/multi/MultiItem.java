package io.github.privacystreams.multi;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;

public class MultiItem extends Item {
    private FeatureProvider[] fp;
    private List<Item> items;

    MultiItem(FeatureProvider[] fp, List<Item> items){
        this.fp = fp;
        this.items = items;
    }

    public static PStreamProvider once(FeatureProvider ... featureProviders) {
        return new MultiItemOnce(featureProviders);
    }

    public static PStreamProvider periodic(long interval, FeatureProvider ... featureProviders) {
        return new MultiItemPeriodic(interval, featureProviders);
    }

    public static PStreamProvider fromJSON(String json){
        JSONmulti jm = new Gson().fromJson(json, JSONmulti.class);
        if(jm.getInterval() == 0){
            return new MultiItemOnce(jm.getFeatureProviders());
        }
        else{
            return new MultiItemPeriodic(jm.getInterval(), jm.getFeatureProviders());
        }
    }

    public static PStreamProvider fromJSON(AssetManager assets, String jsonFileName){
        return fromJSON(loadJSONFromAsset(assets, jsonFileName));
    }

    public static String loadJSONFromAsset(AssetManager assets, String jsonFileName) {
        String json = null;
        try {
            InputStream is = assets.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
