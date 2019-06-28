package io.github.privacystreams.multi;

import java.util.TimerTask;


class NewMultiItemTimerTask extends TimerTask {
    private NewMultiItemPeriodic nmip;

    NewMultiItemTimerTask(NewMultiItemPeriodic nmip){
        this.nmip = nmip;
    }
    public void run(){
        nmip.recordOnce();
    }

}
