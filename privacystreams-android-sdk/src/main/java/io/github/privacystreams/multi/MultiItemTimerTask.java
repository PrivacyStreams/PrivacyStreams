package io.github.privacystreams.multi;

import java.util.TimerTask;


class MultiItemTimerTask extends TimerTask {
    private MultiItemPeriodic nmip;

    MultiItemTimerTask(MultiItemPeriodic nmip){
        this.nmip = nmip;
    }
    public void run(){
        nmip.recordOnce();
    }

}
