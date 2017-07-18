package io.github.privacystreams.communication;

import com.google.api.services.gmail.Gmail;

/**
 * This is the provider that can query the time from a certain time period, which is for one time using.
 */

class GmailHistoryProvider extends BaseGmailProvider {

    /**
     * beginTime and endTime are all in ms.
     * maxResult denotes the max number of
     * return email allowed for one query.
     */

    GmailHistoryProvider(long beginTime, long endTime, int maxResult) {
        super();
        mBegin = beginTime / 1000;
        mEnd = endTime / 1000;
        mMaxResult = maxResult;
    }

    /**
     * For queries in all other times later on, when the app does not need to get
     * authorization and permission from the activity all over again.
     */

    @Override
    protected void provide() {
        super.provide();
        if(authorized){
            new FetchEmailTask().execute(buildTimeQuery(mBegin, mEnd));
        }
    }

    /**
     * When the app just got the authorization and permission from the activity, it goes to this callback.
     */
    @Override
    public void onSuccess(Gmail service) {
        super.onSuccess(service);
        new FetchEmailTask().execute(buildTimeQuery(mBegin, mEnd));
    }

}
