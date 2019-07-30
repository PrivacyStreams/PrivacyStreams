package com.example.mldemoapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import io.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.machine_learning.MLOperators;
import io.github.privacystreams.multi.Feature;
import io.github.privacystreams.multi.FeatureProvider;
import io.github.privacystreams.multi.MultiOperators;
import io.github.privacystreams.multi.VarMultiItem;
import io.github.privacystreams.sensor.Gravity;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.sensor.LinearAcceleration;
import io.github.privacystreams.sensor.RotationVector;
import io.github.privacystreams.utils.Assertions;

public class MainActivity extends AppCompatActivity {
    UQI uqi = new UQI(this);
    TextView inference;
    TextView acc_readings;
    TextView grav_readings;
    TextView gyro_readings;
    TextView rot_readings;
    ProgressBar progressBar;

    Button button;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inference = findViewById(R.id.inference);
        acc_readings = findViewById(R.id.accreading);
        grav_readings = findViewById(R.id.gravreadings);
        gyro_readings = findViewById(R.id.gyroreadings);
        rot_readings = findViewById(R.id.rotationreadings);
        progressBar = findViewById(R.id.progressBar);
        final Handler h = new Handler();
        final int delay = 100; //milliseconds
        final StringWrapper acceleration = new StringWrapper();
        final StringWrapper gravity = new StringWrapper();
        final StringWrapper gyroscope = new StringWrapper();
        final StringWrapper rotation = new StringWrapper();
        final StringWrapper mlinference = new StringWrapper();

        final CounterWrapper sitting = new CounterWrapper();
        final CounterWrapper standing = new CounterWrapper();
        final CounterWrapper jumping = new CounterWrapper();

        sitting.counterInit();
        standing.counterInit();
        jumping.counterInit();

        h.postDelayed(new Runnable(){
            public void run(){
                uqi.getData(VarMultiItem.once(
                        new FeatureProvider(Gravity.asUpdates(1000),
                                new Feature(MultiOperators.getField("x"), "gr-x"),
                                new Feature(MultiOperators.getField("y"), "gr-y"),
                                new Feature(MultiOperators.getField("z"), "gr-z"),
                                new Feature(ArithmeticOperators.magnitude("gr-x"), "grav-x"),
                                new Feature(ArithmeticOperators.magnitude("gr-y"), "grav-y"),
                                new Feature(ArithmeticOperators.magnitude("gr-z"), "grav-z")),
                        new FeatureProvider(LinearAcceleration.asUpdates(1000),
                                new Feature(MultiOperators.getField("x"), "ac-x"),
                                new Feature(MultiOperators.getField("y"), "ac-y"),
                                new Feature(MultiOperators.getField("z"), "ac-z"),
                                new Feature(ArithmeticOperators.magnitude("x", "y", "z"), "acc-mag"),
                                new Feature(ArithmeticOperators.magnitude("ac-x"), "acc-x"),
                                new Feature(ArithmeticOperators.magnitude("ac-y"), "acc-y"),
                                new Feature(ArithmeticOperators.magnitude("ac-z"), "acc-z")),
                        new FeatureProvider(Gyroscope.asUpdates(1000),
                                new Feature(MultiOperators.getField("x"), "gy-x"),
                                new Feature(MultiOperators.getField("y"), "gy-y"),
                                new Feature(MultiOperators.getField("z"), "gy-z"),
                                new Feature(ArithmeticOperators.magnitude("x", "y", "z"), "gyro-mag"),
                                new Feature(ArithmeticOperators.magnitude("gy-x"), "gyro-x"),
                                new Feature(ArithmeticOperators.magnitude("gy-y"), "gyro-y"),
                                new Feature(ArithmeticOperators.magnitude("gy-z"), "gyro-z")),
                        new FeatureProvider(RotationVector.asUpdates(1000),
                                new Feature(MultiOperators.getField("x"), "ro-x"),
                                new Feature(MultiOperators.getField("y"), "ro-y"),
                                new Feature(MultiOperators.getField("z"), "ro-z"),
                                new Feature(MultiOperators.getField("scalar"), "rot-scal"),
                                new Feature(ArithmeticOperators.magnitude("ro-x"), "rot-x"),
                                new Feature(ArithmeticOperators.magnitude("ro-y"), "rot-y"),
                                new Feature(ArithmeticOperators.magnitude("ro-z"), "rot-z"))),
                        Purpose.TEST("Testing VarMultiItem with machine learning"))
                        .setField("accel-info", MLOperators.tuple("ac-x", "ac-y", "ac-z"))
                        .setField("grav-info", MLOperators.tuple("gr-x", "gr-y", "gr-z"))
                        .setField("gyro-info", MLOperators.tuple("gy-x", "gy-y", "gy-z"))
                        .setField("rot-info", MLOperators.tuple("ro-x", "ro-y", "ro-z"))
                        .setField("kmeans", MLOperators.kMeans(
                                new double[][]{
                                        {0.45471999, 1.04448164, 1.04090225, 2.61713678, 9.23112872, 0.7837394,
                                                0.36713625, 0.50426427, 0.30196964, 0.5280388, 0.47068397, 0.32142937,
                                                1.85730299, 0.79573277, 0.61576487
                                        },
                                        {4.06515604, 13.9749835, 2.16966325, 2.37990332, 9.32534567, 1.16835537,
                                                0.64130556, 0.78660903, 0.75012374, 0.48046058, 0.46830624, 0.32439547,
                                                15.14152523, 1.36411487, 0.62154035
                                        },
                                        {0.72011546, 0.82753984, 0.34509707, 7.0277247, 2.18473627, 6.01806478,
                                                0.22414147, 0.16951266, 0.29964697, 0.23122469, 0.78190289, 0.06460487,
                                                1.32389057, 0.4555022, 0.50051182

                                        }},
                                "acc-x", "acc-y", "acc-z",
                                "grav-x", "grav-y", "grav-z",
                                "gyro-x", "gyro-y", "gyro-z",
                                "rot-x", "rot-y", "rot-z",
                                "acc-mag", "gyro-mag", "rot-scal"))
                        .setField("accel-grav-gyro-rot-kmeans", MLOperators.tuple("accel-info", "grav-info",
                                "gyro-info", "rot-info", "kmeans"))
                        .limit(1)
                        .forEach("accel-grav-gyro-rot-kmeans", new Callback<List<Object>>() {
                            protected void onInput(List<Object> input){
                                acceleration.setString(input.get(0)+"");
                                gravity.setString(input.get(1)+"");
                                gyroscope.setString(input.get(2)+"");
                                rotation.setString(input.get(3)+"");
                                String kmeans;
                                switch(((Integer)input.get(4)).intValue()){
                                    case 0:
                                        kmeans = "STANDING/WALKING";
                                        standing.increaseCounter();
                                        break;
                                    case 1:
                                        kmeans = "JUMPING";
                                        jumping.increaseCounter();
                                        break;
                                    case 2:
                                        kmeans = "SITTING/LAYING";
                                        sitting.increaseCounter();
                                        break;
                                    default:
                                        kmeans = "OOPS";
                                }
                                mlinference.setString(kmeans);
                            }
                        });
                /*uqi.getData(Light.asUpdates(1000), Purpose.TEST(""))
                        .limit(1)
                        .forEach("illuminance", new Callback<Float>() {
                            protected void onInput(Float input){
                                s.setString(input+"");
                                System.out.println("illuminance: "+s.getString());
                            }
                        });
                        */


                inference.setText(mlinference.getString());
                acc_readings.setText(acceleration.getString());
                grav_readings.setText(gravity.getString());
                gyro_readings.setText(gyroscope.getString());
                rot_readings.setText(rotation.getString());
                progressBar.setMax(sitting.getCounter() + standing.getCounter() + jumping.getCounter());
                progressBar.setProgress(sitting.getCounter());
                progressBar.setSecondaryProgress(sitting.getCounter() + standing.getCounter());

                h.postDelayed(this, delay);
            }
        }, delay);
    }

}



