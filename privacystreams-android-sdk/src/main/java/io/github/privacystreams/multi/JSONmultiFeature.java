package io.github.privacystreams.multi;

import io.github.privacystreams.audio.AudioOperators;
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
import io.github.privacystreams.device.DeviceOperators;
import io.github.privacystreams.image.ImageOperators;
import io.github.privacystreams.io.IOOperators;
import io.github.privacystreams.location.GeolocationOperators;
import io.github.privacystreams.utils.Assertions;

public class JSONmultiFeature {
    private String operatorName;
    private Object[] operatorParams;
    private String featureName;

    public JSONmultiFeature(String operatorName, Object[] operatorParams, String featureName){
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
