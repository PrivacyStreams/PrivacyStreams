/*******************************************************************************
 * Copyright 2011 The Regents of the University of California
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.github.privacystreams.collector.reminders;

import java.util.Calendar;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTime {
    private int mHour;
    private int mMinute;

    public SimpleTime() {
        Calendar now = Calendar.getInstance();

        mHour = now.get(Calendar.HOUR_OF_DAY);
        mMinute = now.get(Calendar.MINUTE);
    }

    public SimpleTime(SimpleTime time) {
        this(time.getHour(), time.getMinute());
    }

    public SimpleTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    public void copy(SimpleTime time) {
        mHour = time.getHour();
        mMinute = time.getMinute();
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    public String toString(boolean isAmPm) {

        if (isAmPm) {
            String amPm = "am";
            if (mHour >= 12) {
                amPm = "pm";
            }

            int hour = mHour;
            if (hour > 12) {
                hour -= 12;
            } else if (hour == 0) {
                hour = 12;
            }

            return new Formatter().format("%d:%02d " + amPm, hour, mMinute).toString();
        } else {
            return new Formatter().format("%d:%02d", mHour, mMinute).toString();
        }
    }

    public int differenceInMinutes(SimpleTime time) {
        return Math.abs((time.getHour() * 60 + time.getMinute())
                - (mHour * 60 + mMinute));
    }

    public boolean isAfter(SimpleTime time) {

        int thisTime = mHour * 60 + mMinute;
        int givenTime = time.getHour() * 60 + time.getMinute();

        if (thisTime > givenTime) {
            return true;
        }

        return false;
    }

    public boolean isBefore(SimpleTime time) {

        int thisTime = mHour * 60 + mMinute;
        int givenTime = time.getHour() * 60 + time.getMinute();

        if (thisTime < givenTime) {
            return true;
        }

        return false;
    }

    public boolean equals(Object time) {
        if (time instanceof SimpleTime) {
            SimpleTime t = (SimpleTime) time;

            if (t.getHour() == mHour && t.getMinute() == mMinute) {
                return true;
            }
        }

        return false;
    }

    public boolean loadString(String time) {

        Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        Matcher matcher = pattern.matcher(time);
        if (!matcher.matches()) {
            return false;
        }

        String[] timeTokens = time.split(":");
        mHour = Integer.parseInt(timeTokens[0]);
        mMinute = Integer.parseInt(timeTokens[1]);

        return true;
    }

    public String toString() {
        return this.toString(true);
    }
}
