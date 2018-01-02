//package io.github.privacystreams;
//
//import android.content.Context;
//
//import io.github.privacystreams.audio.Audio;
//import io.github.privacystreams.commons.statistic.StatisticOperators;
//import io.github.privacystreams.core.Callback;
//import io.github.privacystreams.core.PStream;
//import io.github.privacystreams.core.UQI;
//import io.github.privacystreams.core.purposes.Purpose;
//import io.github.privacystreams.image.Image;
//import io.github.privacystreams.location.Geolocation;
//import io.github.privacystreams.location.GeolocationOperators;
//
///**
// * Some show cases of PrivacyStreams
// */
//public class ResearchUseCases {
//    private UQI uqi;
//
//    public ResearchUseCases(Context context) {
//        uqi = new UQI(context);
//    }
//
//    /**
//     * paper: From Awareness to Repartee: Sharing Location within Social Groups
//     * in: CHI 2008
//     * Connecto displays context and location information amongst small groups of friends.
//     * Specifically, it collects semantic location and the length of time a person has been
//     * at their asLastKnown location.
//     */
//    void Connecto_CHI_08() {
//        uqi
//                .getData(Geolocation.asUpdates(1000, Geolocation.LEVEL_BUILDING), Purpose.FEATURE("displaying location amongst your friends"))
//                .setField("semantic_loc", GeolocationOperators.geotag(Geolocation.LAT_LON))
//                .localGroupBy("semantic_loc")
//                .setGroupField("duration", StatisticOperators.range(Geolocation.TIMESTAMP))
//                .list();
//    }
//
//    /**
//     * paper: Unobtrusive Sleep Monitoring using Smartphones
//     * in: PervasiveHealth 2013
//     * BES infers sleep duration using the fusion model of light intensity,
//     * duration of phone lock, duration of stationary status, duration of silence.
//     */
//    void BES_PervasiveHealth_2013() {
//        Purpose purpose = Purpose.FEATURE("sleep monitoring");
//        PStream lightStream = uqi
//                .getData(Light.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", Light.ILLUMINANCE);
//
//        PStream phoneLockStream = uqi
//                .getData(DeviceState.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", DeviceState.LOCKED);
//
//        PStream stationaryStream = uqi
//                .getData(AccelMotion.asUpdates(), purpose)
//                .setField("timestamp", round(Motion.TIMESTAMP, Duration.seconds(60)))
//                .localGroupBy("timestamp")
//                .setGroupField("isStationary", underThreshold(Motion.values, 0));
//
//        PStream silenceStream = uqi
//                .getData(Microphone.asUpdates(Duration.seconds(60), Duration.seconds(60)), purpose)
//                .setField("timestamp", round(Audio.TIMESTAMP, Duration.seconds(60)))
//                .map("loudness", calculateLoudness(Audio.URI))
//                .map(setField("isSlient", underThreshold(Motion.values, 0));
//
//        PStream fusionStream = uqi
//                .fusionStream("timestamp", lightStream, phoneLockStream, stationaryStream, silenceStream);
//    }
//
//    /**
//     * paper: Unobtrusive Sleep Monitoring using Smartphones
//     * in: SenSys 2012
//     * IODetector detects whether the environment is indoor or outdoor through
//     * the fusion model of light intensity, cellular signal strength and magnetic field intensity.
//     * Specifically, it checks whether the light intensity is greater than a threshold.
//     */
//    void IODetector_SenSys_2012() {
//        Purpose purpose = Purpose.feature("sleep monitoring");
//        PStream lightStream = uqi
//                .getData(Light.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", Light.ILLUMINANCE);
//
//        PStream cellurSignalStream = uqi
//                .getData(Cellular.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", Cellular.SIGNAL_STRENGTH);
//
//        PStream magneticIntensityStream = uqi
//                .getData(MagnetFieldSensor.asUpdates(), purpose)
//                .map(setField("timestamp", round(EnvSensor.TIMESTAMP, Duration.seconds(60))))
//                .localGroupBy("timestamp")
//                .setGroupField("magnet_field_intensity", average(EnvSensor.values));
//
//        PStream fusionStream = uqi
//                .fusionStream("timestamp", lightStream, cellurSignalStream, magneticIntensityStream);
//    }
//
//    /**
//     * paper: Automatically Characterizing Places with Opportunistic CrowdSensing using Smartphones
//     * in: UbiComp 2012
//     * CrowdSense@Place categorizes places using opportunstically collected images and
//     audio clips crowdsourced in smartphone users. Specifically, it only samples images
//     and audio clips once user starts a foreground activity.
//     */
//    void CrowdSense_UbiComp_2012() {
//        final Purpose purpose = Purpose.feature("opportunstically collecting images and audio");
//
//        Callback callback = new Callback() {
//            @Override
//            public void invoke(Object input) {
//                Thread.sleep(10*1000);
//                UQI.getData(Image.takePhoto(), purpose);
//                UQI.getData(Audio.record(), purpose);
//            }
//        };
//
//        UQI
//                .getData(AppEvent.asUpdates(), purpose)
//                .filter(AppEvent.TYPE, AppEvent.Type.START)
//                .forEach(callback);
//    }
//
//    /**
//     * paper: MoodScope: Building a Mood Sensor in Smartphone Usage Patterns
//     * in: MobiSys 2013
//     * MoodScope is a mood inference engine that applies mood models to smartphone user data
//     including emails, application usage sessions, web browsing getAllSMS, and unique clustered
//     location records. Specifically, it gets the social interaction through the number of exchanges,
//     the duration of phone calls, and number of words of text messages and emails with 10 most
//     frequently interacted contacts in every three days.
//     */
//    void MoodScope_MobiSys_2013() {
//        Purpose purpose = Purpose.feature("inferring mood");
//        UQI
//                .getData(Call.getLogs(), purpose)
//                .setField("timestamp_3days", round(Call.TIMESTAMP, Duration.days(3)))
//                .localGroupBy("timestamp_3days")
//                .setGroupField("duration", sum(Call.DURATION));
//
//        UQI
//                .getData(Message.getAllSMS(), purpose)
//                .groupBy(Message.CONTACT);
//
//    }
//
//    /**
//     * paper: When Attention is not Scarce - Detecting Boredom from Mobile Phone Usage
//     * in: UbiComp 2015
//     * Specifically, when collecting last communication activity,
//     * Broapp calculates the time since last incoming phone call, last notification,
//     * last outgoing phone call and last read/received/sent Message; while measuring contexts,
//     * it gets access to status about audio connection, charging, light, proximity
//     * (in terms of covered by screen or not), asLastKnown ringer mode, and semantic location;
//     * it measures usage intensity using battery info in terms of battery change during
//     * a period of time, number of bytes received and sent during a period of time and
//     * time spent in communication apps; it also measures externally trigger usage data
//     * by calculating number of notifications, name and the app category of last notification;
//     * finally it measures most used app name as well as the app category in a given period of time,
//     */
//    void Broapp_UbiComp_2015() {
//
//    }
//
//    /**
//     * paper: Discovering different kinds of smartphone users through their application usage behaviors
//     * in: UbiComp 2016
//     * This paper clusters different user groups based on smartphone usages. Specifically, it collects up to 10
//     most recent app names every hour.
//     */
//    void UserClassification_UbiComp_2016() {
//
//    }
//
//    /**
//     * paper: Discovering different kinds of smartphone users through their application usage behaviors
//     * in: AAAI 2014
//     * we looked at getting summaries of activity info to assess depression in people.
//     */
//    void depression_AAAI_2014() {
//
//    }
//
//}
