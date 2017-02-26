package com.github.privacystreams.collector.pam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.privacystreams.R;
import com.github.privacystreams.collector.GeneralSettingActivity;
import com.github.privacystreams.collector.reminders.MissedSurveyListActivity;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;



public class PAMActivity extends AppCompatActivity {


    Bitmap[] images;
    int[] imageIds;
    private final Random random = new Random();

    private static String pam_photo_id;
    private static Location userLocation;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private int selection = GridView.INVALID_POSITION;
    private GridView gridview;

    public static final String[] IMAGE_FOLDERS = new String[]{
            "1_afraid",
            "2_tense",
            "3_excited",
            "4_delighted",
            "5_frustrated",
            "6_angry",
            "7_happy",
            "8_glad",
            "9_miserable",
            "10_sad",
            "11_calm",
            "12_satisfied",
            "13_gloomy",
            "14_tired",
            "15_sleepy",
            "16_serene"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTitle("Select a Photo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) this.findViewById(R.id.pam_grid);
        Button submit = (Button) this.findViewById(R.id.post_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check PAM input
                if (null == pam_photo_id) {
                    Toast toast = Toast.makeText(PAMActivity.this, "Please select a picture!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    onSubmit();
                }
            }
        });
        // TODO JARED: is location used anymore, without ProbeLibrary?

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager)
                PAMActivity.this.getSystemService(Context.LOCATION_SERVICE);
        Location gpsloc =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location netloc =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (isBetterLocation(gpsloc, netloc)) {
            userLocation = gpsloc;
        } else {
            userLocation = netloc;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        loadImages();
        setupPAM();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.general_config:
                startActivity(new Intent(this,GeneralSettingActivity.class));
                return true;
            case R.id.missed_surveys:
                startActivity(new Intent(this, MissedSurveyListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void onSubmit() {
        try {

            int idx = Integer.valueOf(pam_photo_id.split("_")[0]);
            DateTime dt = new DateTime();

            PamSchema pamSchema = new PamSchema(idx, dt);
            JSONObject body = pamSchema.toJSON();

            // attach location information is available
            if(userLocation != null) {
                JSONObject location = new JSONObject();
                location.put("latitude", userLocation.getLatitude());
                location.put("longitude", userLocation.getLongitude());
                location.put("accuracy", userLocation.getAccuracy());
                location.put("altitude", userLocation.getAltitude());
                location.put("bearing", userLocation.getBearing());
                location.put("speed", userLocation.getSpeed());
                location.put("timestamp", userLocation.getTime());
                body.put("location", location);
            }

            Log.e("body",body.toString());

            Toast.makeText(PAMActivity.this, "Thank you. Your response is being saved.", Toast.LENGTH_LONG).show();
            // clear selection
            pam_photo_id = null;

            PAMActivity.this.finish();
        } catch (Exception e) {
            Toast.makeText(PAMActivity.this, "Submission failed. Please contact study coordinator", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to
     *                            compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        if (location == null) {
            return false;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void loadImages() {
        images = new Bitmap[IMAGE_FOLDERS.length];
        imageIds = new int[IMAGE_FOLDERS.length];

        AssetManager assets = getResources().getAssets();
        String subFolder;
        for (int i = 0; i < IMAGE_FOLDERS.length; i++) {
            subFolder = "pam_images/" + IMAGE_FOLDERS[i];
            try {
                String filename = assets.list(subFolder)[random.nextInt(3)];
                images[i] = BitmapFactory.decodeStream(assets.open(subFolder + "/" + filename));
                imageIds[i] = filename.split("_")[1].charAt(0) - '0';
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void setupPAM() {
        // Start PAM
        gridview.setAdapter(new BaseAdapter() {

            private final int width = PAMActivity.this.getWindowManager().getDefaultDisplay()
                    .getWidth();

            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (null == convertView) {
                    imageView = new ImageView(PAMActivity.this);
                    imageView.setLayoutParams(new AbsListView.LayoutParams(width / 4, width / 4));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setColorFilter(null);
                } else {
                    imageView = (ImageView) convertView;
                }
                imageView.setImageBitmap(images[position]);

                if (position == selection)
                    highlightSelection(imageView);

                return imageView;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (selection != gridview.INVALID_POSITION)
                    ((ImageView) parent.getChildAt(selection)).setColorFilter(null);
                highlightSelection(v);
                selection = position;
                pam_photo_id = IMAGE_FOLDERS[position];
            }
        });
    }

    private void highlightSelection(View v) {
        ((ImageView) v).setColorFilter(0xffff9933, PorterDuff.Mode.MULTIPLY);
    }


}
