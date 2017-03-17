package edu.cmu.chimps.love_study;

/**
 * Created by fanglinchen on 2/24/17.
 */

public class Constants {

    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "edu.cmu.chimps.love_study.action.startforeground";
    }

    public interface URL {
        public static String DAILY_EMA_URL = "http://cmu.ca1.qualtrics.com/SE/?SID=SV_afzB4tiW2nCPPlX";
        public static String END_OF_THE_DAY_EMA_URL = "http://cmu.ca1.qualtrics.com/SE/?SID=SV_1z5d5docWOrxtpr";
        public static String WEEKLY_EMA_URL = "http://cmu.ca1.qualtrics.com/SE/?SID=SV_1R1WR6DtDL5qYYJ";

        public static String KEY_SURVEY_URL = "qualtrics_survey_url";
    }

}
