package io.github.privacystreams.multi;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.accessibility.BrowserVisit;
import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioOperators;
import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import io.github.privacystreams.commons.comparison.Comparators;
import io.github.privacystreams.commons.item.ItemOperators;
import io.github.privacystreams.commons.items.ItemsOperators;
import io.github.privacystreams.commons.list.ListOperators;
import io.github.privacystreams.commons.logic.LogicOperators;
import io.github.privacystreams.commons.statistic.StatisticOperators;
import io.github.privacystreams.commons.string.StringOperators;
import io.github.privacystreams.commons.time.TimeOperators;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.device.BluetoothDevice;
import io.github.privacystreams.device.DeviceEvent;
import io.github.privacystreams.device.DeviceOperators;
import io.github.privacystreams.image.ImageOperators;
import io.github.privacystreams.io.IOOperators;
import io.github.privacystreams.location.GeolocationOperators;
import io.github.privacystreams.sensor.AirPressure;
import io.github.privacystreams.sensor.Gyroscope;
import io.github.privacystreams.sensor.Light;
import io.github.privacystreams.sensor.RelativeHumidity;
import io.github.privacystreams.sensor.RotationVector;
import io.github.privacystreams.sensor.StepCounter;
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
                case "ArithmeticOperators.add":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.add((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "ArithmeticOperators.castToInt":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ArithmeticOperators.castToInt((String)operatorParams[0]);
                    break;
                case "ArithmeticOperators.castToLong":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ArithmeticOperators.castToLong((String)operatorParams[0]);
                    break;
                case "ArithmeticOperators.divide":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.divide((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "ArithmeticOperators.mode":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.mode((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "ArithmeticOperators.multiply":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.multiply((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "ArithmeticOperators.roundDown":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.roundDown((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "ArithmeticOperators.roundUp":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.roundUp((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "ArithmeticOperators.sub":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ArithmeticOperators.sub((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "AudioOperators.calcLoudness":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.calcLoudness((String)operatorParams[0]);
                    break;
                case "AudioOperators.convertAmplitudeToLoudness":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.convertAmplitudeToLoudness((String)operatorParams[0]);
                    break;
                case "AudioOperators.getAmplitudeSamples":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getAmplitudeSamples((String)operatorParams[0]);
                    break;
                case "AudioOperators.getFilepath":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getFilepath((String)operatorParams[0]);
                    break;
                case "AudioOperators.getMaxAmplitude":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.getMaxAmplitude((String)operatorParams[0]);
                    break;
                case "AudioOperators.hasHumanVoice":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = AudioOperators.hasHumanVoice((String)operatorParams[0]);
                    break;
                case "Comparators.eq":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = Comparators.eq((String)operatorParams[0], operatorParams[1]);
                    break;
                case "Comparators.gt":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = Comparators.gt((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "Comparators.gte":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = Comparators.gte((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "Comparators.lt":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = Comparators.lt((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "Comparators.lte":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Number>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = Comparators.lte((String)operatorParams[0], (Number)operatorParams[1]);
                    break;
                case "Comparators.ne":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = Comparators.ne((String)operatorParams[0], operatorParams[1]);
                    break;
                case "DeviceOperators.getDeviceId":
                    operator = DeviceOperators.getDeviceId();
                    break;
                case "DeviceOperators.isWifiConnected":
                    operator = DeviceOperators.isWifiConnected();
                    break;
                case "GeolocationOperators.distanceBetween":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = GeolocationOperators.distanceBetween((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "GeolocationOperators.distort":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = GeolocationOperators.distort((String)operatorParams[0], (Double)operatorParams[1]);
                    break;
                case "GeolocationOperators.inCircle":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 4);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[3]);
                    operator = GeolocationOperators.inCircle((String)operatorParams[0], (Double)operatorParams[1], (Double)operatorParams[2], (Double)operatorParams[3]);
                    break;
                case "GeolocationOperators.inSquare":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 5);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[3]);
                    Assertions.<Double>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[4]);
                    operator = GeolocationOperators.inSquare((String)operatorParams[0], (Double)operatorParams[1], (Double)operatorParams[2], (Double)operatorParams[3], (Double)operatorParams[4]);
                    break;
                case "IOOperators.uploadToDropbox":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<Boolean>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Function f;
                    String fi;
                    try{
                        f = (Function)operatorParams[0];
                        operator = IOOperators.uploadToDropbox(f, (Boolean)operatorParams[1]);
                    }catch(ClassCastException e){
                        fi = (String)operatorParams[0];
                        operator = IOOperators.uploadToDropbox(fi, (Boolean)operatorParams[1]);
                    }
                    break;
                case "IOOperators.writeToFile":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 3);
                    Assertions.<Boolean>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<Boolean>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    Function fu;
                    String fil;
                    try{
                        fu = (Function)operatorParams[0];
                        operator = IOOperators.writeToFile(fu, (Boolean)operatorParams[1], (Boolean)operatorParams[2]);
                    }catch(ClassCastException e){
                        fil = (String)operatorParams[0];
                        operator = IOOperators.writeToFile(fil, (Boolean)operatorParams[1], (Boolean)operatorParams[2]);
                    }
                    break;
                case "ImageOperators.blur":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.blur((String)operatorParams[0]);
                    break;
                case "ImageOperators.countFaces":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.countFaces((String)operatorParams[0]);
                    break;
                case "ImageOperators.extractText":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.extractText((String)operatorParams[0]);
                    break;
                case "ImageOperators.getBitmap":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.getBitmap((String)operatorParams[0]);
                    break;
                case "ImageOperators.getExif":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.getExif((String)operatorParams[0]);
                    break;
                case "ImageOperators.getFilepath":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.getFilepath((String)operatorParams[0]);
                    break;
                case "ImageOperators.getLatLon":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.getLatLon((String)operatorParams[0]);
                    break;
                case "ImageOperators.hasCharacter":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.hasCharacter((String)operatorParams[0]);
                    break;
                case "ImageOperators.hasFace":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ImageOperators.hasFace((String)operatorParams[0]);
                    break;
                case "ItemOperators.containsField":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.containsField((String) operatorParams[0]);
                    break;
                case "ItemOperators.excludeFields":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.excludeFields((String) operatorParams[0]);
                    break;
                case "ItemOperators.getField":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.getField((String) operatorParams[0]);
                    break;
                case "ItemOperators.getSubItem":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.getSubItem((String) operatorParams[0]);
                    break;
                case "ItemOperators.includeFields":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.includeFields((String) operatorParams[0]);
                    break;
                case "ItemOperators.isFieldIn":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Object[]>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.isFieldIn((String) operatorParams[0], (Object[])operatorParams[1]);
                    break;
                case "ItemOperators.setField":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    try{
                        Function function = (Function)operatorParams[1];
                        operator = ItemOperators.setField((String)operatorParams[0], (Function)operatorParams[1]);
                    }catch(ClassCastException e){
                        operator = ItemOperators.setField((String)operatorParams[0], operatorParams[1]);
                    }
                    break;
                case "ItemOperators.setGroupField":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ItemOperators.setGroupField((String)operatorParams[0], (Function)operatorParams[1]);
                    break;
                case "ItemOperators.setIndependentField":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = ItemOperators.setIndependentField((String)operatorParams[0], (Function)operatorParams[1]);
                    break;
                case "ItemOperators.wrapSubStreamFunction":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.wrapSubStreamFunction((Function)operatorParams[0]);
                    break;
                case "ItemOperators.wrapValueGenerator":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemOperators.wrapValueGenerator((Function)operatorParams[0]);
                    break;
                case "ItemsOperators.asFieldList":
                    if(operatorParams.length == 1){
                        Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                        operator = ItemsOperators.asFieldList((String)operatorParams[0]);
                    }
                    else{
                        operator = ItemsOperators.asFieldList();
                    }
                    break;
                case "ItemsOperators.getItemWithLeast":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemsOperators.getItemWithLeast((String)operatorParams[0]);
                    break;
                case "ItemsOperators.getItemWithMax":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemsOperators.getItemWithMax((String)operatorParams[0]);
                    break;
                case "ItemsOperators.getItemWithMin":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemsOperators.getItemWithMin((String)operatorParams[0]);
                    break;
                case "ItemsOperators.getItemWithMost":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ItemsOperators.getItemWithMost((String)operatorParams[0]);
                    break;
                case "ListOperators.contains":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.contains((String)operatorParams[0], operatorParams[1]);
                    break;
                case "ListOperators.count":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.count((String)operatorParams[0]);
                    break;
                case "ListOperators.intersects":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Object[]>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.intersects((String)operatorParams[0], (Object[])operatorParams[1]);
                    break;
                case "ListOperators.max":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.max((String)operatorParams[0]);
                    break;
                case "ListOperators.mean":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.mean((String)operatorParams[0]);
                    break;
                case "ListOperators.median":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.median((String)operatorParams[0]);
                    break;
                case "ListOperators.min":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.min((String)operatorParams[0]);
                    break;
                case "ListOperators.mode":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.mode((String)operatorParams[0]);
                    break;
                case "ListOperators.range":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.range((String)operatorParams[0]);
                    break;
                case "ListOperators.rms":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.rms((String)operatorParams[0]);
                    break;
                case "ListOperators.sum":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.sum((String)operatorParams[0]);
                    break;
                case "ListOperators.variance":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = ListOperators.variance((String)operatorParams[0]);
                    break;
                case "LogicOperators.and":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = LogicOperators.and((Function)operatorParams[0], (Function)operatorParams[1]);
                    break;
                case "LogicOperators.not":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = LogicOperators.not((Function)operatorParams[0]);
                    break;
                case "LogicOperators.or":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Function>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = LogicOperators.or((Function)operatorParams[0], (Function)operatorParams[1]);
                    break;
                case "StatisticOperators.count":
                    if(operatorParams.length == 1){
                        Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                        operator = StatisticOperators.count((String)operatorParams[0]);
                    }
                    else{
                        operator = StatisticOperators.count();
                    }
                    break;
                case "StatisticOperators.max":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.max((String)operatorParams[0]);
                    break;
                case "StatisticOperators.mean":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.mean((String)operatorParams[0]);
                    break;
                case "StatisticOperators.median":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.median((String)operatorParams[0]);
                    break;
                case "StatisticOperators.min":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.min((String)operatorParams[0]);
                    break;
                case "StatisticOperators.mode":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.mode((String)operatorParams[0]);
                    break;
                case "StatisticOperators.range":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.range((String)operatorParams[0]);
                    break;
                case "StatisticOperators.rms":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.rms((String)operatorParams[0]);
                    break;
                case "StatisticOperators.sum":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.sum((String)operatorParams[0]);
                    break;
                case "StatisticOperators.variance":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StatisticOperators.variance((String)operatorParams[0]);
                    break;
                case "StringOperators.contains":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = StringOperators.contains((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "StringOperators.elide":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 3);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Integer>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<Integer>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    operator = StringOperators.elide((String)operatorParams[0], (Integer)operatorParams[1], (Integer)operatorParams[2]);
                    break;
                case "StringOperators.indexOf":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = StringOperators.indexOf((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "StringOperators.length":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StringOperators.length((String)operatorParams[0]);
                    break;
                case "StringOperators.md5":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StringOperators.md5((String)operatorParams[0]);
                    break;
                case "StringOperators.replace":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 3);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    operator = StringOperators.replace((String)operatorParams[0], (String)operatorParams[1], (String)operatorParams[2]);
                    break;
                case "StringOperators.sha1":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StringOperators.sha1((String)operatorParams[0]);
                    break;
                case "StringOperators.sha256":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 1);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    operator = StringOperators.sha256((String)operatorParams[0]);
                    break;
                case "StringOperators.subString":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 3);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Integer>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    Assertions.<Integer>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[2]);
                    operator = StringOperators.subString((String)operatorParams[0], (Integer)operatorParams[1], (Integer)operatorParams[2]);
                    break;
                case "TimeOperators.formatTime":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = TimeOperators.formatTime((String)operatorParams[0], (String)operatorParams[1]);
                    break;
                case "TimeOperators.getCurrentTime":
                    operator = TimeOperators.getCurrentTime();
                    break;
                case "TimeOperators.recent":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Long>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = TimeOperators.recent((String)operatorParams[0], (Long)operatorParams[1]);
                    break;
                case "TimeOperators.since":
                    Assertions.isTrue("Operator Params list for "+ operatorName + " is of wrong size.", operatorParams.length == 2);
                    Assertions.<String>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[0]);
                    Assertions.<Long>cast("Operator Params list for "+ operatorName + " contains wrong types of objects.", operatorParams[1]);
                    operator = TimeOperators.since((String)operatorParams[0], (Long)operatorParams[1]);
                    break;
                case "MultiOperators.getField":
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

    public class AccelerationItem extends mItem{
        private int sensorDelay;
        public AccelerationItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Acceleration.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class AirPressureItem extends mItem{
        private int sensorDelay;
        public AirPressureItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.AirPressure.asUpdates(sensorDelay), getActualFeatures());
        }
    }


    public class AmbientTemperatureItem extends mItem{
        private int sensorDelay;
        public AmbientTemperatureItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.AmbientTemperature.asUpdates(sensorDelay), getActualFeatures());
        }
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

    public class BluetoothDeviceItem extends mItem{
        public BluetoothDeviceItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.BluetoothDevice.getScanResults(), getActualFeatures());
        }
    }

    public class BrowserSearchItem extends mItem{
        public BrowserSearchItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.accessibility.BrowserSearch.asUpdates(), getActualFeatures());
        }
    }

    public class BrowserVisitItem extends mItem{
        public BrowserVisitItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.accessibility.BrowserVisit.asUpdates(), getActualFeatures());
        }
    }

    public class CalendarEventItem extends mItem{
        public CalendarEventItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.calendar.CalendarEvent.getAll(), getActualFeatures());
        }
    }

    public class CallLogItem extends mItem{
        public CallLogItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Call.getLogs(), getActualFeatures());
        }
    }

    public class CallUpdatesItem extends mItem{
        public CallUpdatesItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Call.asUpdates(), getActualFeatures());
        }
    }

    public class ContactItem extends mItem{
        public ContactItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Contact.getAll(), getActualFeatures());
        }
    }

    public class DeviceEventItem extends mItem{
        public DeviceEventItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.DeviceEvent.asUpdates(), getActualFeatures());
        }
    }

    public class DeviceStateItem extends mItem{
        private long interval;
        private int mask;
        public DeviceStateItem(long interval, int mask, mFeature[] features){
            super(features);
            this.interval = interval;
            this.mask = mask;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.DeviceState.asUpdates(interval, mask), getActualFeatures());
        }
    }

    public class EmailHistoryItem extends mItem{
        private long afterTime;
        private long beforeTime;
        private int maxNumberOfResults;
        public EmailHistoryItem(long afterTime, long beforeTime, int maxNumberOfResults, mFeature[] features){
            super(features);
            this.afterTime = afterTime;
            this.beforeTime = beforeTime;
            this.maxNumberOfResults = maxNumberOfResults;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Email.asGmailHistory(afterTime, beforeTime, maxNumberOfResults), getActualFeatures());
        }
    }

    public class EmailUpdatesItem extends mItem{
        private long frequency;
        public EmailUpdatesItem(long frequency, mFeature[] features){
            super(features);
            this.frequency = frequency;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Email.asGmailUpdates(frequency), getActualFeatures());
        }
    }

    public class EmptyItem extends mItem{
        private long interval;
        public EmptyItem(long interval, mFeature[] features){
            super(features);
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.EmptyItem.asUpdates(interval), getActualFeatures());
        }
    }

    public class GeolocationUpdatesItem extends mItem{
        private long interval;
        private String level;
        public GeolocationUpdatesItem(long interval, String level, mFeature[] features){
            super(features);
            this.interval = interval;
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asUpdates(interval, level), getActualFeatures());
        }
    }

    public class GeolocationCurrentItem extends mItem{
        private String level;
        public GeolocationCurrentItem(String level, mFeature[] features){
            super(features);
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asCurrent(level), getActualFeatures());
        }
    }

    public class GeolocationLastKnownItem extends mItem{
        private String level;
        public GeolocationLastKnownItem(String level, mFeature[] features){
            super(features);
            this.level = level;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.location.Geolocation.asLastKnown(level), getActualFeatures());
        }
    }

    public class GravityItem extends mItem{
        private int sensorDelay;
        public GravityItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.Gravity.asUpdates(sensorDelay), getActualFeatures());
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

    public class ImageCameraItem extends mItem{
        public ImageCameraItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhoto(), getActualFeatures());
        }
    }

    public class ImageStorageItem extends mItem{
        public ImageStorageItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.readStorage(), getActualFeatures());
        }
    }

    public class ImageBgItem extends mItem{
        private int cameraId;
        public ImageBgItem(int cameraId, mFeature[] features){
            super(features);
            this.cameraId = cameraId;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhotoBg(cameraId), getActualFeatures());
        }
    }

    public class ImageBgPeriodicItem extends mItem{
        private int cameraId;
        private long interval;
        public ImageBgPeriodicItem(int cameraId, long interval, mFeature[] features){
            super(features);
            this.cameraId = cameraId;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.image.Image.takePhotoBgPeriodic(cameraId, interval), getActualFeatures());
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

    public class LinearAccelerationItem extends mItem{
        private int sensorDelay;
        public LinearAccelerationItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.LinearAcceleration.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class MessageUpdatesIMItem extends mItem{
        public MessageUpdatesIMItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.asUpdatesInIM(), getActualFeatures());
        }
    }

    public class MessageIncomingSMSItem extends mItem{
        public MessageIncomingSMSItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.asIncomingSMS(), getActualFeatures());
        }
    }

    public class MessageAllSMSItem extends mItem{
        public MessageAllSMSItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.communication.Message.getAllSMS(), getActualFeatures());
        }
    }

    public class NotificationItem extends mItem{
        public NotificationItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.notification.Notification.asUpdates(), getActualFeatures());
        }
    }

    public class RelativeHumidityItem extends mItem{
        private int sensorDelay;
        public RelativeHumidityItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.RelativeHumidity.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class RotationVectorItem extends mItem{
        private int sensorDelay;
        public RotationVectorItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.RotationVector.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class StepCounterItem extends mItem{
        private int sensorDelay;
        public StepCounterItem(int sensorDelay, mFeature[] features){
            super(features);
            this.sensorDelay = sensorDelay;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.sensor.StepCounter.asUpdates(sensorDelay), getActualFeatures());
        }
    }

    public class TestUpdatesFromItem extends mItem{
        private List testObjects;
        private long interval;
        public TestUpdatesFromItem(List testObjects, long interval, mFeature[] features){
            super(features);
            this.testObjects = testObjects;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.asUpdatesFrom(testObjects, interval), getActualFeatures());
        }
    }

    public class TestUpdatesItem extends mItem{
        private int maxInt;
        private double maxDouble;
        private long interval;
        public TestUpdatesItem(int maxInt, double maxDouble, long interval, mFeature[] features){
            super(features);
            this.maxInt = maxInt;
            this.maxDouble = maxDouble;
            this.interval = interval;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.asUpdates(maxInt, maxDouble, interval), getActualFeatures());
        }
    }

    public class TestAllFromItem extends mItem{
        private List testObjects;
        public TestAllFromItem(List testObjects, mFeature[] features){
            super(features);
            this.testObjects = testObjects;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.getAllFrom(testObjects), getActualFeatures());
        }
    }

    public class TestGetAllRandomItem extends mItem{
        private int maxInt;
        private double maxDouble;
        private int count;
        public TestGetAllRandomItem(int maxInt, double maxDouble, int count, mFeature[] features){
            super(features);
            this.maxInt = maxInt;
            this.maxDouble = maxDouble;
            this.count = count;
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.core.items.TestItem.getAllRandom(maxInt, maxDouble, count), getActualFeatures());
        }
    }

    public class WifiItem extends mItem{
        public WifiItem(mFeature[] features){
            super(features);
        }
        public FeatureProvider getFeatureProvider(){
            return new FeatureProvider(io.github.privacystreams.device.WifiAp.getScanResults(), getActualFeatures());
        }
    }

    private long interval;

    private AccelerationItem Acceleration;
    private AirPressureItem AirPressure;
    private AmbientTemperatureItem AmbientTemperature;
    private AudioItem Audio;
    private BluetoothDeviceItem BluetoothDevice;
    private BrowserSearchItem BrowserSearch;
    private BrowserVisitItem BrowserVisit;
    private CalendarEventItem CalendarEvent;
    private CallUpdatesItem CallUpdates;
    private CallLogItem CallLog;
    private ContactItem Contact;
    private DeviceEventItem DeviceEvent;
    private DeviceStateItem DeviceState;
    private EmailHistoryItem EmailHistory;
    private EmailUpdatesItem EmailUpdates;
    private EmptyItem EmptyItem;
    private GeolocationUpdatesItem GeolocationUpdates;
    private GeolocationLastKnownItem GeolocationLastKnown;
    private GeolocationCurrentItem GeolocationCurrent;
    private GravityItem Gravity;
    private GyroscopeItem Gyroscope;
    private ImageBgItem ImageBg;
    private ImageBgPeriodicItem ImageBgPeriodic;
    private ImageCameraItem ImageCamera;
    private ImageStorageItem ImageStorage;
    private LightItem Light;
    private LinearAccelerationItem LinearAcceleration;
    private MessageAllSMSItem MessageAllSMS;
    private MessageIncomingSMSItem MessageIncomingSMS;
    private MessageUpdatesIMItem MessageUpdatesIM;
    private NotificationItem Notification;
    private RelativeHumidityItem RelativeHumidity;
    private RotationVectorItem RotationVector;
    private StepCounterItem StepCounter;
    private TestAllFromItem TestAllFrom;
    private TestGetAllRandomItem TestGetAllRandom;
    private TestUpdatesFromItem TestUpdatesFrom;
    private TestUpdatesItem TestUpdates;
    private WifiItem WifiAp;

    public JSONmulti(){};

    public FeatureProvider[] getFeatureProviders(){
        List<FeatureProvider> fps = new ArrayList<>();
        if(Acceleration != null){
            System.out.println("Exist Acceleration");
            fps.add(Acceleration.getFeatureProvider());
        }
        if(AirPressure != null){
            System.out.println("Exist Air Pressure");
            fps.add(AirPressure.getFeatureProvider());
        }
        if(AmbientTemperature != null){
            System.out.println("Exist Ambient Temperature");
            fps.add(AmbientTemperature.getFeatureProvider());
        }
        if(Audio != null){
            System.out.println("Exist Audio");
            fps.add(Audio.getFeatureProvider());
        }
        if(BluetoothDevice != null){
            System.out.println("Exist Bluetooth Device");
            fps.add(BluetoothDevice.getFeatureProvider());
        }
        if(BrowserSearch != null){
            System.out.println("Exist Browser Search");
            fps.add(BrowserSearch.getFeatureProvider());
        }
        if(BrowserVisit != null){
            System.out.println("Exist Browser Visit");
            fps.add(BrowserVisit.getFeatureProvider());
        }
        if(CalendarEvent != null){
            System.out.println("Exist Calendar Event");
            fps.add(CalendarEvent.getFeatureProvider());
        }
        if(CallUpdates != null){
            System.out.println("Exist Call Updates");
            fps.add(CallUpdates.getFeatureProvider());
        }
        if(CallLog != null){
            System.out.println("Exist Call Log");
            fps.add(CallLog.getFeatureProvider());
        }
        if(Contact != null){
            System.out.println("Exist Contact");
            fps.add(Contact.getFeatureProvider());
        }
        if(DeviceEvent != null){
            System.out.println("Exist Device Event");
            fps.add(DeviceEvent.getFeatureProvider());
        }
        if(DeviceState != null){
            System.out.println("Exist Device State");
            fps.add(DeviceState.getFeatureProvider());
        }
        if(EmailHistory != null){
            System.out.println("Exist Email History");
            fps.add(EmailHistory.getFeatureProvider());
        }
        if(EmailUpdates != null){
            System.out.println("Exist Email Updates");
            fps.add(EmailUpdates.getFeatureProvider());
        }
        if(this.EmptyItem!= null){
            System.out.println("Exist Empty Item");
            fps.add(this.EmptyItem.getFeatureProvider());
        }
        if(GeolocationCurrent != null){
            System.out.println("Exist Geolocation Current");
            fps.add(GeolocationCurrent.getFeatureProvider());
        }
        if(GeolocationLastKnown != null){
            System.out.println("Exist Geolocation Last Known");
            fps.add(GeolocationLastKnown.getFeatureProvider());
        }
        if(GeolocationUpdates != null){
            System.out.println("Exist Geolocation Updates");
            fps.add(GeolocationUpdates.getFeatureProvider());
        }
        if(Gravity != null){
            System.out.println("Exist Gravity");
            fps.add(Gravity.getFeatureProvider());
        }
        if(Gyroscope != null){
            System.out.println("Exist Gyroscope");
            fps.add(Gyroscope.getFeatureProvider());
        }
        if(ImageBg != null){
            System.out.println("Exist Image Bg");
            fps.add(ImageBg.getFeatureProvider());
        }
        if(ImageBgPeriodic != null){
            System.out.println("Exist Image Bg Periodic");
            fps.add(ImageBgPeriodic.getFeatureProvider());
        }
        if(ImageCamera != null){
            System.out.println("Exist Image Camera");
            fps.add(ImageCamera.getFeatureProvider());
        }
        if(ImageStorage != null){
            System.out.println("Exist Image Storage");
            fps.add(ImageStorage.getFeatureProvider());
        }
        if(Light != null){
            System.out.println("Exist Light");
            fps.add(Light.getFeatureProvider());
        }
        if(LinearAcceleration != null){
            System.out.println("Exist Linear Acceleration");
            fps.add(LinearAcceleration.getFeatureProvider());
        }
        if(MessageAllSMS != null){
            System.out.println("Exist Message All SMS");
            fps.add(MessageAllSMS.getFeatureProvider());
        }
        if(MessageUpdatesIM != null){
            System.out.println("Exist Message Updates IM");
            fps.add(MessageUpdatesIM.getFeatureProvider());
        }
        if(MessageIncomingSMS != null){
            System.out.println("Exist Message Incoming SMS");
            fps.add(MessageIncomingSMS.getFeatureProvider());
        }
        if(Notification != null){
            System.out.println("Exist Notification");
            fps.add(Notification.getFeatureProvider());
        }
        if(RelativeHumidity != null){
            System.out.println("Exist Relative Humidity");
            fps.add(RelativeHumidity.getFeatureProvider());
        }
        if(RotationVector != null){
            System.out.println("Exist Rotation Vector");
            fps.add(RotationVector.getFeatureProvider());
        }
        if(StepCounter != null){
            System.out.println("Exist Step Counter");
            fps.add(StepCounter.getFeatureProvider());
        }
        if(TestAllFrom != null){
            System.out.println("Exist Test All From");
            fps.add(TestAllFrom.getFeatureProvider());
        }
        if(TestGetAllRandom != null){
            System.out.println("Exist Test Get All Random");
            fps.add(TestGetAllRandom.getFeatureProvider());
        }
        if(TestUpdatesFrom != null){
            System.out.println("Exist Test Updates From");
            fps.add(TestUpdatesFrom.getFeatureProvider());
        }
        if(TestUpdates != null){
            System.out.println("Exist Test Updates");
            fps.add(TestUpdates.getFeatureProvider());
        }
        if(WifiAp != null){
            System.out.println("Exist Wifi Ap");
            fps.add(WifiAp.getFeatureProvider());
        }
        System.out.println("FeatureProviders: " + fps);
        return fps.toArray(new FeatureProvider[fps.size()]);
    }

    public long getInterval(){
        return interval;
    }

}
