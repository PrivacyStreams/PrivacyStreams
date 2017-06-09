package com.github.privacystreams.communication;

import com.google.api.services.gmail.Gmail;

/**
 * This is the provider that can query the time from a certain time period, which is for one time using.
 */

class GmailListProvider extends BaseGmailProvider{

    GmailListProvider(long begin,long end,int maxResult){ //When using it the begin and end should be in milisecond
        super();
        mBegin = begin/1000;
        mEnd = end/1000;
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
            new MakeRequestTask().execute(buildTimeQuery(mBegin,mEnd));
        }
    }

    /**
     * When the app just got the authorization and permission from the activity, it goes to this callback.
     */
    @Override
    public void onSuccess(Gmail service) {
        super.onSuccess(service);
        new MakeRequestTask().execute(buildTimeQuery(mBegin,mEnd));
    }


}