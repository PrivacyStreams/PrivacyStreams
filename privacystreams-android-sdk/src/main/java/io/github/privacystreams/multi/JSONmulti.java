package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioOperators;
import io.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.utils.Assertions;

public class JSONmulti {
    public class mFeature {
        private String operatorName;
        private Object[] operatorParams;
        private String featureName;

        public mFeature(String operatorName, Object[] operatorParams, String featureName){
            this.operatorName = operatorName;
            this.operatorParams = operatorParams;
            this.featureName = featureName;
        }

        public Feature getFeature(){
            Function operator = null;
            switch(operatorName){
                case "add":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.add((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "castToInt":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ArithmeticOperators.castToInt((String)operatorParams[0]);
                    break;
                case "castToLong":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ArithmeticOperators.castToLong((String)operatorParams[0]);
                    break;
                case "divide":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.divide((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "mode":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.mode((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "multiply":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.multiply((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "roundDown":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.roundDown((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "roundUp":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.roundUp((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "sub":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.sub((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "calcLoudness":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.calcLoudness((String)operatorParams[0]);
                    break;
                case "convertAmplitudeToLoudness":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.convertAmplitudeToLoudness((String)operatorParams[0]);
                    break;
                case "getAmplitudeSamples":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getAmplitudeSamples((String)operatorParams[0]);
                    break;
                case "getFilepath":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getFilepath((String)operatorParams[0]);
                    break;
                case "getMaxAmplitude":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getMaxAmplitude((String)operatorParams[0]);
                    break;
                case "hasHumanVoice":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.hasHumanVoice((String)operatorParams[0]);
                    break;
                case "field":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = MultiOperators.getField((String)operatorParams[0]);
                    break;
                default:
                    System.out.println("Unable to match operator");
                    Assertions.isTrue(operatorName + " operator not supported.", false);
            }
            return new Feature(operator, featureName);
        }
    }

    public abstract class mItem {
        private mFeature[] features;
        public mItem(mFeature[] features){
            this.features = features;
        }
        public Feature[] getActualFeatures(){
            Feature[] fs = new Feature[features.length];
            for(int i = 0; i < features.length; i++){
                fs[i] = features[i].getFeature();
            }
            return fs;
        }
        public abstract FeatureProvider getFeatureProvider();
    }
    public class AudioItem extends mItem{
        private long duration;
        public AudioItem(long duration, mFeature[] features){
            super(features);
            this.duration = duration;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.audio.Audio.record(duration), getActualFeatures());
        }
    }

    public class LightItem extends mItem{
        private int sensorDelay;
        public LightItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Light.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class GyroscopeItem extends mItem{
        private int sensorDelay;
        public GyroscopeItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Gyroscope.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    private long interval;
    private AudioItem Audio;
    private LightItem Light;
    private GyroscopeItem Gyroscope;

    public JSONmulti(){};

    public FeatureProvider[] getFeatureProviders(){
        List<FeatureProvider> fps = new ArrayList<>();
        if(Audio != null){
            System.out.println("Exist Audio");
            fps.add(Audio.getFeatureProvider());
        }
        if(Light != null){
            System.out.println("Exist Light");
            fps.add(Light.getFeatureProvider());
        }
        if(Gyroscope != null){
            System.out.println("Exist Gyroscope");
            fps.add(Gyroscope.getFeatureProvider());
        }
        System.out.println("FeatureProviders: " + fps);
        return fps.toArray(new FeatureProvider[fps.size()]);
    }

    public long getInterval(){
        return interval;
    }

}
