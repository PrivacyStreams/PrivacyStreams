package com.github.privacystreams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.commons.time.TimeOperators;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.items.TestItem;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.image.ImageOperators;

public class MainActivity extends AppCompatActivity {
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UQI uqi = new UQI(MainActivity.this);
        uqi.getData(TestItem.asUpdates(10, 10.0, 10), Purpose.TEST("Testing first data query."))
                .limit(10)
//                .groupBy("x")
                .debug();

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

            useCases.getRecentCalledNames(2);

//            useCases.testCurrentLocation();
//            useCases.testTextEntry();
 //           useCases.testNotification();
//            useCases.testAudio();
//            useCases.testMockData();
//            useCases.testContacts();
//            useCases.testDeviceState();
//            useCases.testTextEntry();
//
//            useCases.testBrowserSearchUpdates();
//            useCases.testBrowserHistoryUpdates();
//            useCases.testWifiUpdates(30);
//
//            useCases.testLightUpdatesProvider();
//            useCases.testBlueToothUpatesProvider();
//            useCases.testPhysicalMotionUpdatesProvider();
//            useCases.testLightUpdatesProvider();
//
//            useCases.testUIAction();
          useCases.testIMUpdates();
            return null;
        }
    }
}
