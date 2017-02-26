package com.github.privacystreams.core.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 14/11/2016.
 * From API 23, it is needed to request permissions at runtime.
 */
public class PermissionActivity extends Activity {

    public static final String REQUEST_CODE = "request_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getSerializableExtra(REQUEST_CODE) != null) {
            int requestCode = (int) getIntent().getSerializableExtra(REQUEST_CODE);
            UQI uqi = PermissionUtils.pendingUQIs.get(requestCode);
            Set<String> requestedPermissions = uqi.getQuery().getRequiredPermissions();
            ActivityCompat.requestPermissions(PermissionActivity.this, requestedPermissions.toArray(new String[requestedPermissions.size()]), requestCode);
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.pendingUQIs.containsKey(requestCode)) {
            UQI uqi = PermissionUtils.pendingUQIs.get(requestCode);
            uqi.evaluate(false);
            PermissionUtils.pendingUQIs.remove(requestCode);
            finish();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
