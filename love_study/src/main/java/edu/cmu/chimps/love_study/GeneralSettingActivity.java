package edu.cmu.chimps.love_study;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.provider.Settings;

import static edu.cmu.chimps.love_study.Utils.isAccessibilityEnabled;
import static edu.cmu.chimps.love_study.Utils.isTrackingEnabled;


public class GeneralSettingActivity extends PreferenceActivity {

    private static Context context;
    private static boolean tracking_clicked;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!isTrackingEnabled(context) && tracking_clicked){
            Intent serviceIntent = new Intent(context,TrackingService.class);
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            context.startService(serviceIntent);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            Preference trackingServicePreference =findPreference("collectDataButton");
            trackingServicePreference
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(!isAccessibilityEnabled(context)){
                        tracking_clicked = true;
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    }
                    return false;
                }
            });
        }
    }


}