package io.github.privacystreams.test;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected Object doInBackground(Object[] objects) {
            TestCases testCases = new TestCases(MainActivity.this);
            //testCases.testEmailList();;
            // testCases.testsift();
            //  testCases.testContact(apiKey,apiSecret);
            // testCases.testFlight(api_key,api_secret);
            //  testCases.testFood(apiKey,apiSecret);
            //   testCases.testParcel(api_key,api_secret);
            //testCases.testImage();
            //testCases.testInvoice(api_key,api_secret,"whatever");

            //  testCases.testOrder(api_key,api_secret);
            //   testCases.testDriveList();
            //   testCases.testEmailList();
            //  testCases.testCurrentLocation();
            // testCases.testLocation();
           // testCases.testImageUpdate();
           //testCases.testImage();
            //testCases.testBrowserSearchUpdates();
            testCases.testBrowserHistoryUpdates();
           // testCases.testLocalFileUpdate();
            return null;
        }
    }
}