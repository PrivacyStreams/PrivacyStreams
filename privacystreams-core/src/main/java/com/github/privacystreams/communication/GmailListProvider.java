package com.github.privacystreams.communication;

import com.github.privacystreams.core.exceptions.PSException;
import com.google.api.services.gmail.Gmail;

/**
 * This is the provider that can query the time from a certain time period, which is for one time using.
 */

class GmailListProvider extends BaseGmailProvider{

    GmailListProvider(long after,long before,int maxResult){
        super();
        mAfter = after;
        mBefore = before;
        mMaxResult = maxResult;
    }

    @Override
    protected void provide() {
        super.provide();
        /**
         * For queries in all other times later on, when the app does not need to get authorization and permission from the
         * activity all over again.
         */

        if(authorized){
            new MakeRequestTask().execute(buildTimeQuery(mAfter,mBefore));
        }
    }

    /**
     * When the app just got the authorization and permission from the activity, it goes to this callback.
     */
    @Override
    public void onSuccess(Gmail service) {
        mService = service;
        new MakeRequestTask().execute(buildTimeQuery(mAfter,mBefore));
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Gmail canceled."));
    }

}