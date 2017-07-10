package com.github.privacystreams.document;

import com.google.api.services.drive.Drive;

/**
 * This is the provider that can query the time from a certain time period,
 * which is for one time using.
 */

public class DriveListProvider extends BaseGoogleDriveProvider {
    DriveListProvider(long beginTime, long endTime, int maxResult, int resultNum) {
        super();
        mBegin = beginTime;
        mEnd = endTime;
        mMaxResult = maxResult;
        mResultNum = resultNum;
    }

    /**
     * For queries in all other times later on, when the app does not need to get
     * authorization and permission from the activity all over again.
     */
    protected void provide() {
        super.provide();
        if (authorized) {
            new FetchDriveTask().execute();
        }
    }

    /**
     * When the app just got the authorization and permission from the activity, it goes to this callback.
     */
    @Override
    public void onSuccess(Drive drive) {
        super.onSuccess(drive);
        new FetchDriveTask().execute();
    }
}
