package io.github.privacystreams.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
<<<<<<< HEAD:app/src/main/java/com/github/privacystreams/MainActivity.java
            UseCases useCases = new UseCases(MainActivity.this);

//            useCases.getRecentCalledNames(2);

//            useCases.testImage();
//            useCases.testCurrentLocation();
//            useCases.testTextEntry();
//            useCases.testNotification();
//            useCases.testAudio();
//            useCases.testMockData();


//            for(Item item: useCases.isAtHome()){
//                Log.e("item",(String)item.getValueByField(WifiAp.BSSID));
//            }

//            useCases.testContacts();
//            useCases.testDeviceState();
=======
            TestCases testCases = new TestCases(MainActivity.this);

            testCases.testDumpAccEvents();
            testCases.testCurrentLocation();
//            testCases.testTextEntry();
//            testCases.testNotification();
//            testCases.testAudio();
//            testCases.testMockData();
//            testCases.testContacts();
//            testCases.testDeviceState();
>>>>>>> eb641f8ad850f8242057d9884a6ce35f8fd5ea8f:test/src/main/java/io/github/privacystreams/test/MainActivity.java
//
//            testCases.testBrowserSearchUpdates();
//            testCases.testBrowserHistoryUpdates();
//
<<<<<<< HEAD:app/src/main/java/com/github/privacystreams/MainActivity.java
//            useCases.testAccEvents();
//
            useCases.testIMUpdates();
 //           useCases.testEmailUpdates();
//            useCases.testEmailList();
=======
//            testCases.testAccEvents();
//
//            testCases.testIMUpdates();
 //           testCases.testEmailUpdates();
//            testCases.testEmailList();
>>>>>>> eb641f8ad850f8242057d9884a6ce35f8fd5ea8f:test/src/main/java/io/github/privacystreams/test/MainActivity.java

//useCases.testUpdatesContact();

//            useCases.testWifiUpdates();

//            useCases.testIMUIUpdates();
//            useCases.testCalendarList();
//            useCases.testWifiUpdates();
//            useCases.newTestWifiTrueUpdates();
//            useCases.testUpdatesCalendar();

//            useCases.testDriveList();

            return null;
        }
    }
}