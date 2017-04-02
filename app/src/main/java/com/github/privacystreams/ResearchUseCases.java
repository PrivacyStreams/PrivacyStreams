package com.github.privacystreams;//package xyz.ylimit.personaldataapp;
//
//import xyz.ylimit.personaldataapp.privacystreams.UQI;
//import xyz.ylimit.personaldataapp.privacystreams.collectors.location.Locations;
//import xyz.ylimit.personaldataapp.privacystreams.collectors.statistic.StatisticOperators;
//import xyz.ylimit.personaldataapp.privacystreams.generic.Callback;
//import xyz.ylimit.personaldataapp.privacystreams.generic.MStream;
//import xyz.ylimit.personaldataapp.privacystreams.providers.app.AppEvent;
//import xyz.ylimit.personaldataapp.privacystreams.providers.audio.Audio;
//import xyz.ylimit.personaldataapp.privacystreams.providers.call.Call;
//import xyz.ylimit.personaldataapp.privacystreams.providers.environment.LightEnv;
//import xyz.ylimit.personaldataapp.privacystreams.providers.location.Geolocation;
//import xyz.ylimit.personaldataapp.privacystreams.providers.motion.AccelMotion;
//import xyz.ylimit.personaldataapp.privacystreams.providers.sms.Message;
//import xyz.ylimit.personaldataapp.privacystreams.providers.system.DeviceState;
//import xyz.ylimit.personaldataapp.privacystreams.purposes.Purpose;
//import xyz.ylimit.personaldataapp.privacystreams.utils.time.Duration;
//
//import static java.lang.Thread.sleep;
//import static xyz.ylimit.personaldataapp.privacystreams.transformations.map.Mappers.setField;
//
///**
// * Some show cases of PrivacyStreams
// */
//public class ResearchUseCases {
//
//    public ResearchUseCases() {
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
//        UQI
//                .getDataItems(Geolocation.asUpdates(), Purpose.feature("displaying location amongst your friends"))
//                .map(setField("semantic_loc", Locations.geotag(Geolocation.COORDINATES)))
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
//        Purpose purpose = Purpose.feature("sleep monitoring");
//        MStream lightStream = UQI
//                .getDataItems(LightEnv.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", LightEnv.ILLUMINANCE);
//
//        MStream phoneLockStream = UQI
//                .getDataItems(DeviceState.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", DeviceState.LOCKED);
//
//        MStream stationaryStream = UQI
//                .getDataItems(AccelMotion.asUpdates(), purpose)
//                .map(setField("timestamp", round(Motion.TIMESTAMP, Duration.seconds(60))))
//                .localGroupBy("timestamp")
//                .setGroupField("isStationary", underThreshold(Motion.values, 0));
//
//        MStream silenceStream = UQI
//                .getDataItems(Microphone.asUpdates(Duration.seconds(60), Duration.seconds(60)), purpose)
//                .map(setField("timestamp", round(Audio.TIMESTAMP, Duration.seconds(60))))
//                .map("loudness", calculateLoudness(Audio.URI))
//                .map(setField("isSlient", underThreshold(Motion.values, 0));
//
//        MStream fusionStream = UQI
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
//        MStream lightStream = UQI
//                .getDataItems(LightEnv.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", LightEnv.ILLUMINANCE);
//
//        MStream cellurSignalStream = UQI
//                .getDataItems(Cellular.asUpdates(Duration.seconds(60)), purpose)
//                .project("timestamp", Cellular.SIGNAL_STRENGTH);
//
//        MStream magneticIntensityStream = UQI
//                .getDataItems(MagnetFieldSensor.asUpdates(), purpose)
//                .map(setField("timestamp", round(EnvSensor.TIMESTAMP, Duration.seconds(60))))
//                .localGroupBy("timestamp")
//                .setGroupField("magnet_field_intensity", average(EnvSensor.values));
//
//        MStream fusionStream = UQI
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
//        Purpose purpose = Purpose.feature("opportunstically collecting images and audio");
//
//        Callback callback = new Callback() {
//            @Override
//            public void invoke(Object input) {
//                sleep(Duration.seconds(10));
//                UQI.getDataItem(Camera.take(), xxx);
//                UQI.getDataItem(Audio.record(), xxx);
//            }
//        };
//
//        UQI
//                .getDataItems(AppEvent.asUpdates(), purpose)
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
//                .getDataItems(Call.getLogs(), purpose)
//                .map(setField("timestamp_3days", round(Call.TIMESTAMP, Duration.days(3))))
//                .localGroupBy("timestamp_3days")
//                .setGroupField("duration", sum(Call.DURATION));
//
//        UQI
//                .getDataItems(Message.getAllSMS(), purpose)
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
//
//
//}
