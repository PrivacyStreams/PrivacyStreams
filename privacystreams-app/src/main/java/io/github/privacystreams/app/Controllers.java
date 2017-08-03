package io.github.privacystreams.app;


import android.content.Context;
import android.content.Intent;

import io.github.privacystreams.core.actions.collect.Collectors;

public class Controllers {
    private Context context;

    Controllers(Context context) {
        this.context = context;
    }

    public void startCollectService() {
        Intent collectServiceIntent = new Intent(this.context, PStreamCollectService.class);
        this.context.startService(collectServiceIntent);
    }

    public void stopCollectService() {
        Intent collectServiceIntent = new Intent(this.context, PStreamCollectService.class);
        this.context.stopService(collectServiceIntent);
    }
}
