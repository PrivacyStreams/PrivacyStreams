package io.github.privacystreams.test;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

import org.tensorflow.lite.Interpreter;

import io.github.privacystreams.accessibility.AccEvent;
import io.github.privacystreams.accessibility.BrowserSearch;
import io.github.privacystreams.accessibility.BrowserVisit;
import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioOperators;
import io.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.commons.list.ListOperators;
import io.github.privacystreams.commons.statistic.StatisticOperators;
import io.github.privacystreams.commons.time.TimeOperators;
import io.github.privacystreams.communication.Call;
import io.github.privacystreams.communication.Contact;
import io.github.privacystreams.communication.Message;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStream;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.actions.collect.Collectors;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.items.EmptyItem;
import io.github.privacystreams.core.items.TestItem;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.device.BluetoothDevice;
import io.github.privacystreams.device.DeviceEvent;
import io.github.privacystreams.device.DeviceOperators;
import io.github.privacystreams.device.WifiAp;
import io.github.privacystreams.communication.Email;
import io.github.privacystreams.image.Image;
import io.github.privacystreams.image.ImageOperators;
import io.github.privacystreams.location.Geolocation;
import io.github.privacystreams.location.GeolocationOperators;
import io.github.privacystreams.location.LatLon;
import io.github.privacystreams.machine_learning.JSONBuilder;
import io.github.privacystreams.machine_learning.MLOperators;
import io.github.privacystreams.machine_learning.Recognition;
import io.github.privacystreams.multi.Feature;
import io.github.privacystreams.multi.FeatureProvider;
import io.github.privacystreams.multi.JSONmulti;
import io.github.privacystreams.multi.JSONmultiFeature;
import io.github.privacystreams.multi.MultiItem;
import io.github.privacystreams.multi.MultiOperators;
import io.github.privacystreams.notification.Notification;
import io.github.privacystreams.io.IOOperators;
import io.github.privacystreams.sensor.Gravity;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.sensor.LinearAcceleration;
import io.github.privacystreams.sensor.RotationVector;
import io.github.privacystreams.utils.Duration;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.TimeUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import static io.github.privacystreams.commons.statistic.StatisticOperators.count;
import static io.github.privacystreams.commons.statistic.StatisticOperators.sum;
import static io.github.privacystreams.commons.string.StringOperators.sha1;
import static io.github.privacystreams.commons.time.TimeOperators.recent;

/**
 * Some show cases of PrivacyStreams
 */
public class TestCases {
    private UQI uqi;
    private Context context;

    public TestCases(Context context) {
        this.context = context;
        this.uqi = new UQI(context);
    }


    public void testMLMultiDemo(){
        uqi.getData(MultiItem.periodic(2000,
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
                Purpose.TEST("Testing MultiItem with machine learning"))
                .setField("kmeans", MLOperators.kMeans(
                        new double[][]{
                                {0.45471999,  1.04448164,  1.04090225,  2.61713678,  9.23112872,  0.7837394,
                                0.36713625,  0.50426427,  0.30196964,  0.5280388,   0.47068397,  0.32142937,
                                1.85730299,  0.79573277,  0.61576487
                                },
                                {4.06515604, 13.9749835,   2.16966325,  2.37990332,  9.32534567,  1.16835537,
                                        0.64130556,  0.78660903,  0.75012374,  0.48046058,  0.46830624,  0.32439547,
                                        15.14152523,  1.36411487,  0.62154035
                                },
                                {0.72011546,  0.82753984,  0.34509707,  7.0277247,   2.18473627,  6.01806478,
                                        0.22414147,  0.16951266,  0.29964697,  0.23122469,  0.78190289,  0.06460487,
                                        1.32389057,  0.4555022,   0.50051182

                                }},
                        "acc-x", "acc-y", "acc-z",
                        "grav-x", "grav-y", "grav-z",
                        "gyro-x", "gyro-y", "gyro-z",
                        "rot-x", "rot-y", "rot-z",
                        "acc-mag", "gyro-mag", "rot-scal"))
                .forEach("kmeans", new Callback<Integer>() {
                    protected void onInput(Integer input){
                        switch(input.intValue()){
                            case 0:
                                System.out.println("STANDING/WALKING");
                                break;
                            case 1:
                                System.out.println("JUMPING");
                                break;
                            case 2:
                                System.out.println("SITTING/LAYING");
                                break;
                            default:
                                System.out.println("Should not be here");
                        }
                    }
                });
    }

    public void testML_Linear_Regression(){
        System.out.println("TESTING ML JSON");
        uqi.getData(MultiItem.periodic(2000,
                new FeatureProvider(Audio.record(1000),
                        new Feature(AudioOperators.calcLoudness("audio_data"), "loudness")),
                new FeatureProvider(Light.asUpdates(1000),
                        new Feature(MultiOperators.getField("illuminance"), "brightness"))),
                Purpose.TEST("Testing ML JSON"))
                .setField("lr", MLOperators.linearRegression(new float[]{3, 1}, 2,
                        "brightness", "loudness"))
                .setField("tuple", MLOperators.tuple("loudness", "brightness", "lr"))
                .forEach("tuple", new Callback<List<Float>>() {
                    protected void onInput(List<Float> input){
                        System.out.println("Brightess: "+input.get(1));
                        System.out.println("Loudness: "+input.get(0));
                        System.out.println("Linear Regression Output: "+input.get(2));
                    }
                });
    }


    public void testMLJSON(AssetManager assets){
        System.out.println("TESTING ML JSON");
        uqi.getData(MultiItem.periodic(2000,
                new FeatureProvider(Audio.record(1000),
                    new Feature(AudioOperators.calcLoudness("audio_data"), "loudness")),
                new FeatureProvider(Light.asUpdates(1000),
                        new Feature(MultiOperators.getField("illuminance"), "brightness"))),
                Purpose.TEST("Testing ML JSON"))
                .setField("lr", MLOperators.machineLearning(assets, "linear_regression2.json"))
                .setField("tuple", MLOperators.tuple("loudness", "brightness", "lr"))
                .forEach("tuple", new Callback<List<Float>>() {
                    protected void onInput(List<Float> input){
                        System.out.println("Brightess: "+input.get(1));
                        System.out.println("Loudness: "+input.get(0));
                        System.out.println("Linear Regression Output: "+input.get(2));
                    }
                });
    }
    public void testMLJSONBuilder(){
        System.out.println("TESTING ML JSON");
        List<String> inputFields = new ArrayList<>();
        inputFields.add("brightness");
        inputFields.add("loudness");

        List<Float> weights = new ArrayList<>();
        weights.add(Float.valueOf((float)3));
        weights.add(Float.valueOf((float)1));

        uqi.getData(MultiItem.periodic(2000,
                new FeatureProvider(Audio.record(1000),
                        new Feature(AudioOperators.calcLoudness("audio_data"), "loudness")),
                new FeatureProvider(Light.asUpdates(1000),
                        new Feature(MultiOperators.getField("illuminance"), "brightness"))),
                Purpose.TEST("Testing ML JSON"))
                .setField("lr", MLOperators.machineLearning(JSONBuilder.LINEAR_REGRESSION(weights, 2, inputFields)))
                .setField("tuple", MLOperators.tuple("loudness", "brightness", "lr"))
                .forEach("tuple", new Callback<List<Float>>() {
                    protected void onInput(List<Float> input){
                        System.out.println("Brightess: "+input.get(1));
                        System.out.println("Loudness: "+input.get(0));
                        System.out.println("Linear Regression Output: "+input.get(2));
                    }
                });
    }
    public void testVarMultiItemJSON(AssetManager assets, String jsonFile){
        System.out.println("TESTING VAR-MULTI WITH JSON");
        uqi.getData(MultiItem.fromJSON(loadJSONFromAsset(assets, jsonFile)), Purpose.TEST("TESTING VAR-MULTI WITH JSON"))
                .setField("tuple", MLOperators.tuple("loudness", "doubleLoudness", "illuminance",
                        "x", "y", "z"))
                .forEach("tuple", new Callback<List<Object>>() {
                    protected void onInput(List<Object> input){
                        System.out.println("Output of MultiItem");

                        System.out.println("Loudness: " + input.get(0) + "\n"
                                + "Double Loudness: " + input.get(1) + "\n"
                                + "Brightness: " + input.get(2) + "\n"
                                + "Gyroscope: " + input.subList(3, 5));
                    }
                });
    }

    public void testVarMultiItemJSONBuilder(){
        uqi.getData(MultiItem.fromJSON(new JSONmulti()
                .AUDIO(1000, new JSONmultiFeature[]{new JSONmultiFeature("AudioOperators.calcLoudness",
                        new Object[]{"audio_data"}, "loudness")})
                .LIGHT(1000, new JSONmultiFeature[]{new JSONmultiFeature("MultiOperators.getField",
                        new Object[]{"illuminance"}, "brightness")}).getJSON()), Purpose.TEST(""))
                .setField("tuple", MLOperators.tuple("loudness", "brightness"))
                .forEach("tuple", new Callback<List<Float>>() {
                    protected void onInput(List<Float> input){
                        System.out.println(input);
                    }
                });
    }
    public void testVarMultiItemPeriodic(){
        uqi.getData(MultiItem.periodic(2000,
                new FeatureProvider(Audio.record(1000),
                        new Feature(AudioOperators.calcLoudness("audio_data"), "loudness")),
                new FeatureProvider(Light.asUpdates(1000),
                        new Feature(MultiOperators.getField("illuminance"), "brightness")),
                new FeatureProvider(Gyroscope.asUpdates(1000),
                        new Feature(MultiOperators.getField("x"), "gyro-x"),
                        new Feature(MultiOperators.getField("y"), "gyro-y"),
                        new Feature(MultiOperators.getField("z"), "gyro-z"))),
                Purpose.TEST("Testing MultiItem"))
                .setField("tuple", MLOperators.tuple("loudness", "brightness",
                        "gyro-x", "gyro-y", "gyro-z"))
                .forEach("tuple", new Callback<List<Object>>() {
                    protected void onInput(List<Object> input){
                        System.out.println("Output of MultiItem");

                        System.out.println("Loudness: " + input.get(0) + "\n"
                                + "Brightness: " + input.get(1) + "\n"
                                + "Gyroscope: " + input.subList(2, 5));
                    }
                });
    }

    public void testVarMultiItemOnce(){
        uqi.getData(MultiItem.once(
                new FeatureProvider(Audio.record(1000),
                        new Feature(AudioOperators.calcLoudness("audio_data"),
                                                            "loudness")),
                new FeatureProvider(Light.asUpdates(1000),
                        new Feature(MultiOperators.getField("illuminance"),
                                                            "brightness"))),
                Purpose.TEST("Testing MultiItem"))
                .setField("tuple", MLOperators.tuple("loudness", "brightness"))
                .forEach("tuple", new Callback<List<Object>>() {
                    protected void onInput(List<Object> input){
                        System.out.println("Output of MultiItem");

                        System.out.println("Loudness: " + input.get(0) + "\n"
                                + "Brightness: " + input.get(1));
                    }
                });
    }



    public String loadJSONFromAsset(AssetManager assets, String filename) {
        String json = null;
        try {
            InputStream is = assets.open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public void testTFLiteInterpreter(AssetManager assets, Integer sensorOrientation){
        final int NUM_DETECTIONS = 10;
        // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
        // contains the location of detected boxes
        float[][][] outputLocations = new float[1][NUM_DETECTIONS][4];
        // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
        // contains the classes of detected boxes
        float[][] outputClasses = new float[1][NUM_DETECTIONS];
        // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
        // contains the scores of detected boxes
        float[][] outputScores = new float[1][NUM_DETECTIONS];
        // numDetections: array of shape [Batchsize]
        // contains the number of detected boxes
        float[] numDetections = new float[1];

        int inputSize = 300;

        Map<Integer, Object> outputMap = new HashMap<>();
        outputMap.put(0, outputLocations);
        outputMap.put(1, outputClasses);
        outputMap.put(2, outputScores);
        outputMap.put(3, numDetections);
        Interpreter tflite;
        try {
            tflite = new Interpreter(loadModelFile(assets, "detect.tflite"));
            //tflite = new Interpreter(loadModelFile(assets, "object_detection.tflite"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        uqi.getData(Image.takePhoto(), Purpose.TEST("Text TF Lite Object Detection"))
                .setField("bitmap", ImageOperators.getBitmap("image_data"))
                .setField("input", MLOperators.objectDetectionProcessor("bitmap", inputSize, true, sensorOrientation))
                .setField("output", MLOperators.tfLiteInferInterpreter("input", outputMap, tflite))
                .forEach("output", new Callback<Map<Integer, java.lang.Object>>() {
                    protected void onInput(Map<Integer, java.lang.Object> input){
                        Vector<String> labels = new Vector<String>();
                        InputStream labelsInput = null;
                        String labelFilename = "file:///android_asset/labelmap.txt";
                        //String labelFilename = "file:///android_asset/object_detection_labelmap.txt";
                        String actualFilename = labelFilename.split("file:///android_asset/")[1];
                        try {
                            labelsInput = assets.open(actualFilename);
                            BufferedReader br = null;
                            br = new BufferedReader(new InputStreamReader(labelsInput));
                            String line;
                            while ((line = br.readLine()) != null) {
                                labels.add(line);
                            }
                            br.close();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        ArrayList<String> classes = new ArrayList();
                        ArrayList<Float> scores = new ArrayList();
                        for (int i = 0; i < NUM_DETECTIONS; ++i) {
                            int labelOffset = 1;
                            classes.add(labels.get((int) outputClasses[0][i] + labelOffset));
                            scores.add(outputScores[0][i]);
                        }
                        System.out.println(classes);
                        System.out.println(scores);
                    }
                });


    }

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    public void testTFLiteImageRecognition(AssetManager assets, Integer sensorOrientation){
        int inputSize = 224;
        Vector<String> labels = new Vector<String>();
        InputStream labelsInput = null;
        String labelFilename = "file:///android_asset/labels.txt";
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        try {
            labelsInput = assets.open(actualFilename);
            BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[][] labelProbArray = new byte[1][labels.size()];

        Interpreter tflite;
        try {
            tflite = new Interpreter(loadModelFile(assets, "mobilenet_v1_1.0_224_quant.tflite"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Recognition> detectedObjects = new Vector<>();
        HashMap<Integer, Object> hashMap = new HashMap<>();
        hashMap.put(0, labelProbArray);
        try {
            detectedObjects = uqi.getData(Image.takePhoto(), Purpose.TEST(""))
                    .setField("bitmap", ImageOperators.getBitmap("image_data"))
                    .setField("input", MLOperators.objectDetectionProcessor("bitmap", inputSize,
                            true, sensorOrientation))
                    .setField("labelProbArray", MLOperators.field(labelProbArray))
                    .setField("output", MLOperators.tfLiteInferInterpreter("input", hashMap, tflite))
                    .setField("labels", MLOperators.field(labels))
                    .setField("recognized", MLOperators.imageRecognitionOutput("labelProbArray", "labels", 5))
                    .getFirst("recognized");
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(detectedObjects);
    }
    public void testTFLiteInterpreter2(AssetManager assets, Integer sensorOrientation){
        final int NUM_DETECTIONS = 10;
        // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
        // contains the location of detected boxes
        float[][][] outputLocations = new float[1][NUM_DETECTIONS][4];
        // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
        // contains the classes of detected boxes
        float[][] outputClasses = new float[1][NUM_DETECTIONS];
        // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
        // contains the scores of detected boxes
        float[][] outputScores = new float[1][NUM_DETECTIONS];
        // numDetections: array of shape [Batchsize]
        // contains the number of detected boxes
        float[] numDetections = new float[1];

        int inputSize = 300;

        Interpreter tflite;
        try {
            tflite = new Interpreter(loadModelFile(assets, "detect.tflite"));
            //tflite = new Interpreter(loadModelFile(assets, "object_detection.tflite"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Vector<String> labels = new Vector<String>();
        InputStream labelsInput = null;
        String labelFilename = "file:///android_asset/labelmap.txt";
        //String labelFilename = "file:///android_asset/object_detection_labelmap.txt";
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        try {
            labelsInput = assets.open(actualFilename);
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(labelsInput));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<String> outputArrs = Arrays.asList(new String[]{"outputLocations", "outputClasses",
                        "outputScores", "numDetections"});


        uqi.getData(Image.takePhoto(), Purpose.TEST("Text TF Lite Object Detection"))
                .setField("bitmap", ImageOperators.getBitmap("image_data"))
                .setField("input", MLOperators.objectDetectionProcessor("bitmap", inputSize,
                        true, sensorOrientation))
                .setField("outputLocations", MLOperators.field(outputLocations))
                .setField("outputClasses", MLOperators.field(outputClasses))
                .setField("outputScores", MLOperators.field(outputScores))
                .setField("numDetections", MLOperators.field(numDetections))
                .setField("output", MLOperators.tfLiteInferInterpreter("input",
                       outputArrs, tflite))
                .setField("labels", MLOperators.field(labels))
                .setField("recognized", MLOperators.objectDetectionRecognizer(outputArrs,
                        "labels", NUM_DETECTIONS, inputSize))
                .forEach("recognized", new Callback<List<Recognition>>() {
                    protected void onInput(List<Recognition> input){
                        System.out.println(input);
                    }
                });


    }

    public void testMerge() {
        uqi.getData(TestItem.asUpdates(10, 1.0, 1000), Purpose.TEST("Test merge"))
                .union(TestItem.asUpdates(50, 50.0, 5000))
                .debug();
    }

    public void testBlueToothUpdatesProvider() {
        uqi.getData(BluetoothDevice.getScanResults(), Purpose.FEATURE("blueTooth device")).debug();
    }

    public void testImage() {
//        uqi.getData(Image.readStorage(), Purpose.TEST("test"))
//                .setField("lat_lon", ImageOperators.getLatLon(Image.IMAGE_DATA))
//                .debug();
        uqi.getData(Image.takePhoto(), Purpose.UTILITY("taking picture."))
                .setField("imagePath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .setField("faceCount", ImageOperators.countFaces(Image.IMAGE_DATA))
                .setField("text", ImageOperators.extractText(Image.IMAGE_DATA))
//                .setField("hasCharacter", ImageOperators.hasCharacter(Image.IMAGE_DATA))
                .debug();
//                .ifPresent("imagePath", new Callback<String>() {
//                    @Override
//                    protected void onInput(String imagePath) {
//                        System.out.println(imagePath);
//                    }
//                    @Override
//                    protected void onFail(PSException exception) {
//                        exception.printStackTrace();
//                    }
//                });
    }

    public void testTakePhotoBg() {
        uqi.getData(Image.takePhotoBgPeriodic(0, 2000), Purpose.UTILITY("taking picture."))
                .limit(10)
                .setField("imagePath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .debug();
    }

    public void testAudio() {
        uqi.getData(Audio.recordPeriodic(1000, 1000), Purpose.HEALTH("monitoring sleep."))
                .setField("loudness", AudioOperators.calcLoudness("audio_data"))
                .forEach("loudness", new Callback<Double>() {
                    @Override
                    protected void onInput(Double input) {
                        System.out.println("Current loudness is " + input + " dB.");
                    }
                });

    }

    public void testReuse() {
        try {
            PStream stream = uqi
                    .getData(TestItem.getAllRandom(20, 100, 100), Purpose.TEST("test"))
                    .limit(100)
                    .reuse(3);
            int count = stream.count();
            System.out.println(String.format(Locale.getDefault(), "%d", count));
            int gt5Count = stream.filter(Comparators.gt(TestItem.X, 5)).count();
            System.out.println(String.format(Locale.getDefault(), "%d %d", count, gt5Count));
            int lte5Count = stream.logAs("3").filter(Comparators.lte(TestItem.X, 5)).logAs("4").count();
            System.out.println(String.format(Locale.getDefault(), "%d %d %d", count, gt5Count, lte5Count));
        } catch (PSException e) {
            e.printStackTrace();
        }

    }

    public void testLocation() {
        Globals.LocationConfig.useGoogleService = true;
        PStream locationStream = uqi.getData(Geolocation.asUpdates(1000, Geolocation.LEVEL_CITY), Purpose.TEST("test"))
                .setField("distorted_lat_lon", GeolocationOperators.distort(Geolocation.LAT_LON, 1000))
                .setField("distortion", GeolocationOperators.distanceBetween(Geolocation.LAT_LON, "distorted_lat_lon"))
                .reuse(2);

        locationStream.debug();
        locationStream.forEach("distorted_lat_lon", new Callback<LatLon>() {
            @Override
            protected void onInput(LatLon input) {
                System.out.println(input);
            }
        });

        try {
            Thread.sleep(100000);
            uqi.stopAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void testCurrentLocation() {
        Globals.LocationConfig.useGoogleService = true;
        try {
            LatLon latLon = uqi
                    .getData(Geolocation.asCurrent(Geolocation.LEVEL_CITY), Purpose.TEST("test"))
                    .logOverSocket("location")
                    .getFirst(Geolocation.LAT_LON);
            System.out.println(latLon.toString());
        } catch (PSException e) {
            e.printStackTrace();
        }

    }

    public void testCall() {
        uqi.getData(Call.asUpdates(), Purpose.TEST("test")).debug();
    }


    public void testSMS() {
        uqi.getData(Message.asIncomingSMS(), Purpose.TEST("test")).debug();
    }


    public void testEmailUpdates(){
        uqi.getData(Email.asGmailUpdates(15*60*1000), Purpose.TEST("test")).debug();
    }

    public void testEmailList(){
        uqi.getData(Email.asGmailHistory(System.currentTimeMillis()-Duration.hours(100),
                System.currentTimeMillis()-Duration.hours(50),
                100),Purpose.TEST("test")).debug();
    }

    // For testing
    public void testMockData() {
        Globals.DropboxConfig.accessToken = "access_token_here";
        Globals.DropboxConfig.leastSyncInterval = Duration.seconds(3);
        Globals.DropboxConfig.onlyOverWifi = false;

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .forEach(IOOperators.<Item>writeToFile("PrivacyStreams/testData.txt", true, true));

        uqi
                .getData(TestItem.asUpdates(20, 100, 500), Purpose.TEST("test"))
                .limit(100)
                .timeout(Duration.seconds(10))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TestItem.TIME_CREATED, Duration.seconds(2))))
                .localGroupBy("time_round")
                .setIndependentField("uuid", DeviceOperators.getDeviceId())
                .forEach(IOOperators.uploadToDropbox(new Function<Item, String>() {
                    @Override
                    public String apply(UQI uqi, Item input) {
                        return input.getValueByField("uuid") + "/mockItem/" + input.getValueByField(Item.TIME_CREATED) + ".json";
                    }
                }, true));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void testDeviceState() {
        Purpose purpose = Purpose.TEST("test");
        uqi
                .getData(EmptyItem.asUpdates(5000), purpose)
                .setIndependentField("contact_list", Contact.getAll().compound(Collectors.toItemList()))
                .setIndependentField("wifi_ap_list", uqi.getData(WifiAp.getScanResults(), purpose).getValueGenerator(Collectors.toItemList()))
                .setIndependentField("bluetooth_list", BluetoothDevice.getScanResults().compound(Collectors.toItemList()))
                .setIndependentField("uuid", DeviceOperators.getDeviceId())
                .limit(3)
                .debug();
    }

    public void testDumpAccEvents() {
        new UQI(this.context).getData(AccEvent.asUpdates(), Purpose.TEST("Test"))
                .forEach(IOOperators.<Item>writeToFile("accEvent.txt", true, true));
    }

    /*
     * Getting a stream of notifications and printing
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void testNotification() {
        uqi.getData(Notification.asUpdates(), Purpose.TEST("test")).debug();
    }

    public void testWifiUpdates(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            uqi.getData(WifiAp.getScanResults(), Purpose.FEATURE("wifi updates")).debug();
        }
    }

    public void testBrowserHistoryUpdates(){
        uqi.getData(BrowserVisit.asUpdates(), Purpose.FEATURE("browser history")).debug();
    }

    public void testBrowserSearchUpdates(){
        uqi.getData(BrowserSearch.asUpdates(), Purpose.FEATURE("browser search")).debug();
    }

    public void testAccEvents(){
        uqi.getData(AccEvent.asUpdates(), Purpose.TEST("AccEvent"))
                .inFixedInterval(1000)
                .keepChanges()
                .logAs("accEvent")
                .idle();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void testIMUpdates(){
        uqi.getData(Message.asUpdatesInIM(),Purpose.FEATURE("im updates")).debug();
    }

    // get a count of the #contacts in contact list
    void testContacts() {
        try {
            int count = uqi
                    .getData(Contact.getAll(), Purpose.FEATURE("estimate how popular you are."))
                    .count();
            System.out.println(count);

            uqi
                    .getData(Call.getLogs(), Purpose.SOCIAL("finding your closest contact."))
                    .filter(recent("timestamp", Duration.days(365)))
                    .groupBy("contact")
                    .setGroupField("#calls", count())
                    .sortBy("#calls")
                    .reverse()
                    .ifPresent("contact", new Callback<String>() {
                        @Override
                        protected void onInput(String contact) {
                            System.out.println("Most-called contact: " + contact);
                        }

                        @Override
                        protected void onFail(PSException e) {
                            System.out.println(e.getMessage());
                        }
                    });

        } catch (PSException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // get recent called 10 contacts’ names
    public void getRecentCalledNames(int n) {
        try {
            List<String> recentCalledPhoneNumbers = uqi
                    .getData(Call.getLogs(), Purpose.FEATURE("getData recent called phone numbers"))
                    .logAs("1")
                    .sortBy(Call.TIMESTAMP)
                    .logAs("2")
                    .limit(n)
                    .asList(Call.CONTACT);
            System.out.println(recentCalledPhoneNumbers);
            List<String> recentCalledNames = uqi
                    .getData(Contact.getAll(), Purpose.FEATURE("getData names of recent called phone numbers"))
                    .filter(ListOperators.intersects(Contact.PHONES, recentCalledPhoneNumbers.toArray()))
                    .asList(Contact.NAME);
            System.out.println(recentCalledNames);
        }
        catch (PSException e) {
            e.printStackTrace();
        }
    }

    // get a count of calls since 31Oct2015
    int getCallCountSince() throws PSException {
        return uqi
                .getData(Call.getLogs(), Purpose.FEATURE("know how many calls you made"))
                .filter(TimeOperators.since(Call.TIMESTAMP, TimeUtils.fromFormattedString("yyyy-MM-dd", "2015-10-31")))
                .count();
    }

    void testDeviceStateChangeUpdates(){
        uqi.getData(DeviceEvent.asUpdates(), Purpose.FEATURE("device states")).debug();
    }

    boolean isAtHome() throws PSException {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return uqi
//                    .getData(WifiAp.getScanResults(), Purpose.FEATURE("know whether you are at home."))
//                    .filter(Comparators.eq(WifiAp.CONNECTED, true))
//                    .filter(WifiAPOperators.atHome(WifiAp.SSID))
//                    .count()==1;
//        }
        return false;
    }

    void callbackWhenReceivesMessage(String appName, Callback<String> messageCallback){
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE(""));
    }

    // get the intent when enter an area, the callback will be invoked when the use enters or exits an area
//    void callbackWhenEntersArea(double x, double y, double r, Callback<Boolean> enterAreaCallback) {
//        uqi
//                .getData(Geolocation.asUpdates(LocationManager.GPS_PROVIDER, 10, 10), Purpose.FEATURE("know when you enter an area"))
//                .setField("inCircle", GeolocationOperators.inCircle(Geolocation.COORDINATES, x,y,r))
//                .onChange("inCircle", enterAreaCallback);
//    }

    // handle two-factor auth Message message
    void getTwoFactorAuthSMS(String serverPhoneNum, Callback<String> messageCallback) {
        uqi
                .getData(Message.asIncomingSMS(), Purpose.FEATURE("Two-factor authentication"))
                .filter(Comparators.eq(Message.CONTACT, serverPhoneNum))
                .filter(Comparators.eq(Message.TYPE, Message.TYPE_RECEIVED))
                .ifPresent(Message.CONTENT, messageCallback);
    }

    // get location and distort 100 meters for advertisement
//    void passLocationToAd() throws PSException {
//        List<Double> coordinates = uqi
//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.ADS("targeted advertisement"))
//                .output(GeolocationOperators.distort(Geolocation.LAT_LON, 100),);
//    }
//
//    // get postcode of asLastKnown location
//    String getPostcode() throws PSException {
//        return uqi

//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.FEATURE("get postcode for nearby search"));

//                .getData(Geolocation.asLastKnown(Geolocation.LEVEL_CITY), Purpose.FEATURE("get postcode for nearby search"))

////                .output(GeolocationOperators.(Geolocation.COORDINATES));
//    }

    // knowing if a person is making more or less calls than normal
    boolean isMakingMoreCallsThanNormal() throws PSException {
        int callCountLastWeek = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get how many calls you made recently"))
                .filter(recent(Call.TIMESTAMP, Duration.days(7)))
                .count();
        double callFrequencyLastWeek = (double) callCountLastWeek / 7;
        int callCountLastYear = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get how many calls you made normally"))
                .filter(recent(Call.TIMESTAMP, Duration.days(365)))
                .count();
        double callFrequencyLastYear = (double) callCountLastYear / 365;
        return callFrequencyLastWeek > callFrequencyLastYear;
    }

    // getting all the photo metadata (but not photos)
    List<Map<String, String>> getAllPhotoMetadata() throws PSException {
        return uqi
                .getData(Image.readStorage(), Purpose.FEATURE("get metadata of the photos in storage"))
                .setField("metadata", ImageOperators.getExif(Image.IMAGE_DATA))
                .asList("metadata");
    }

    // getting how loud it is over the past m seconds,  every n minutes
    void getAverageLoudnessEveryNSeconds(int m, int n, Callback<Double> loudnessCallback) {
        uqi
                .getData(Audio.recordPeriodic(Duration.seconds(m), Duration.seconds(n)),
                        Purpose.FEATURE("how loud it is periodically"))
                .setField("loudness", AudioOperators.calcLoudness(Audio.AUDIO_DATA))
                .forEach("loudness", loudnessCallback);
    }

    // calculating sentiment across all Message
//    double getAverageSentimentOfSMS() throws PSException {

////        return uqi
////                .getData(Message.getAllSMS(), Purpose.FEATURE("calculate the sentiment across all Message messages"));

//        return uqi
//                .getData(Message.getAllSMS(), Purpose.FEATURE("calculate the sentiment across all Message messages"))

////                .setField("sentiment", StringOperators.sentiment(Message.TEXT))
////                .outputItems(StatisticOperators.average("sentiment"));
//    }

    // figure out place where person spends the most time (ie home)
//    String getPlaceSpentMostTime() throws PSException {
//        return uqi
//                .getData(Geolocation.asHistory(), Purpose.FEATURE("get the place you spent the most time"))
//                .setField("geo_tag", GeolocationOperators.asGeotag(Geolocation.COORDINATES))
//                .localGroupBy("geo_tag")
//                .setGroupField("time_spent", StatisticOperators.range(Geolocation.EVENT_TIME))
//                .sortBy("time_spent")
//                .reverse()
//                .getFirst()
//                .getField("geo_tag");
//    }


    // hard, calculate total number of calls and length of calls per person in call log
    void getTotalNumberOfCallsPerPerson() throws PSException {
        // each Map element is like {"phone_number": "xxxxxxx", "num_of_calls": 10, "length_of_calls": 30000}
        List<Item> totalNumberOfCallsPerPerson = uqi
                .getData(Call.getLogs(), Purpose.FEATURE("get the tie relationship with people"))
                .groupBy(Call.CONTACT)
                .setGroupField("num_of_calls", StatisticOperators.count())
                .setGroupField("length_of_calls", sum(Call.DURATION))
                .project(Call.CONTACT, "num_of_calls", "length_of_calls")
                .asList();
        Callback<Item> callback = new Callback<Item>() {
            @Override
            protected void onInput(Item input) {
            }
        };


        uqi.getData(Call.getLogs(), Purpose.SOCIAL("Infer social relationship"))
                .filter(recent(Call.TIMESTAMP, 365*24*60*60*1000L))
                .groupBy(Call.CONTACT)
                .setGroupField("#calls", count())
                .setGroupField("Dur.calls", sum(Call.DURATION))
                .setField("hashed_phone", sha1(Call.CONTACT))
                .project("hashed_phone", "#calls", "Dur.calls")
                .forEach(callback);


    }


    // hash the names or phone#s in Message or call logs, so we can get data like above while mitigating privacy concerns
    List<String> getHashedPhoneNumbersInSMS() throws PSException {
        return uqi
                .getData(Message.getAllSMS(), Purpose.FEATURE("get hashed phone numbers."))
                .setField("hashed_phone_number", sha1(Message.CONTACT))
                .asList("hashed_phone_number");
    }

    // only get location data when accelerometer motion is above a threshold
    // TODO need stream fusion
    void getLocationWhenMoving() {
    }

    // hard, when plugged in get data xyz at rate R, but when not plugged in get data xy at rate R'
    // TODO need stream fusion
    void upgradeGranularityWhenPlugged() {
    }

    // hard, build a sleep monitor that combines streams of data from microphone loudness, location, accelerometer, light, etc
    // TODO need stream fusion
    void sleepMonitor() {
    }

}
