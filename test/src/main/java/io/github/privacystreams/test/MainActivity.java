package io.github.privacystreams.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
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
            TestCases testCases = new TestCases(MainActivity.this);

            testCases.testDemo();
            //testCases.testVarMultiItemJSON(getAssets(), "multi.json");
            //testCases.testComfort();
            //testCases.testNewMultiItem();
            //testCases.testVarMultiItemPeriodic();
            //testCases.testVarMultiItemOnce();
            //testCases.testNewMultiItemPeriodic();
            //testCases.testJSONBuilder();
            //testCases.testML(getAssets(), "linear_regression2.json");
            //svm.json
            //kmeans.json
            //linear_regression2.json

            /*Integer screenOrientation;
            switch (getWindowManager().getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_270:
                    screenOrientation = 270;
                case Surface.ROTATION_180:
                    screenOrientation = 180;
                case Surface.ROTATION_90:
                    screenOrientation = 90;
                default:
                    screenOrientation = 0;
            }
            Integer sensorOrientation = 270 - screenOrientation;
            testCases.testTFLiteImageRecognition(getAssets(), sensorOrientation);*/
            //testCases.testTFLiteInterpreter(getAssets(), sensorOrientation);
            //testCases.testMultiItem();



            //testCases.testMultiItemPeriodic();
            //testCases.testEmptyMultiItem();

//            testCases.testTakePhotoBg();
//            testCases.testMerge();
//            testCases.testAccEvents();
//            testCases.testCurrentLocation();
//            testCases.testTextEntry();
//            testCases.testNotification();
//            testCases.testAudio();
//            testCases.testMockData();
//            testCases.testContacts();
//            testCases.testDeviceState();
//
//            testCases.testBrowserSearchUpdates();
//            testCases.testBrowserHistoryUpdates();
//
//            testCases.testAccEvents();
//
//            testCases.testIMUpdates();
 //           testCases.testEmailUpdates();
//            testCases.testEmailList();

            return null;
        }
    }
}