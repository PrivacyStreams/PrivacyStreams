package com.github.privacystreams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.device.WifiAp;

public class MainActivity extends AppCompatActivity {
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask().execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object[] objects) {
            UseCases useCases = new UseCases(MainActivity.this);

//            useCases.getRecentCalledNames(2);

//            useCases.testImage();
//            useCases.testCurrentLocation();
//            useCases.testTextEntry();
//            useCases.testNotification();
//            useCases.testAudio();
//            useCases.testMockData();

            for(Item item: useCases.isAtHome()){
                Log.e("item",(String)item.getValueByField(WifiAp.BSSID));
            }

//            useCases.testContacts();
//            useCases.testDeviceState();
//
//            useCases.testBrowserSearchUpdates();
//            useCases.testBrowserHistoryUpdates();
//
//            useCases.testAccEvents();
//
//            useCases.testIMUpdates();
 //           useCases.testEmailUpdates();
//            useCases.testEmailList();

//useCases.testUpdatesContact();

//            useCases.testWifiUpdates();

//            useCases.testIMUIUpdates();
//            useCases.testCalendarList();
//            useCases.testWifiUpdates();
//            useCases.newTestWifiTrueUpdates();
//            useCases.testUpdatesCalendar();
<<<<<<< HEAD
=======
            useCases.testDriveList();

>>>>>>> 2d03d98f4d2ae44e4b6c096ef3bfd1fab92e1d8f

            return null;
        }
    }
}