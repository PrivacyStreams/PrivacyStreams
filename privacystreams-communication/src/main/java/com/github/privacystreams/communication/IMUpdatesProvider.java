package com.github.privacystreams.communication;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Created by fanglinchen on 1/31/17.
 */

public class IMUpdatesProvider extends MultiItemStreamProvider {
    MultiItemStream mStream;
    private static final String OPERATOR = "$message_updates";

    @Subscribe
    public void onEvent(Item message){
        if(message instanceof Message){
            if (mStream != null && !mStream.isClosed()){
                mStream.write(message);
            }
            else{
                EventBus.getDefault().unregister(this);
            }
        }
    }

    @Override
    protected void provide(MultiItemStream output) {
        EventBus.getDefault().register(this);
        mStream=output;
    }

}
