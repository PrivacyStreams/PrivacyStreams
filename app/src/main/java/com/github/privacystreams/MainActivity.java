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
        uqi.getData(Image.getFromStorage(), Purpose.UTILITY("taking picture."))
                .setField("lat_lng", ImageOperators.getLatLng(Image.IMAGE_DATA))
                .setField("imagePath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .ifPresent("imagePath", new Callback<String>() {
                    @Override
                    protected void onInput(String imagePath) {
                        System.out.println(imagePath);
                    }
                    @Override
                    protected void onFail(PSException exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                });

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
            useCases.testImage();
            return null;
        }
    }
}