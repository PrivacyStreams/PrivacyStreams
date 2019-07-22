package io.github.privacystreams.multi;

import java.util.TimerTask;


class VarMultiItemTimerTask extends TimerTask {
    private VarMultiItemPeriodic nmip;

    VarMultiItemTimerTask(VarMultiItemPeriodic nmip){
        this.nmip = nmip;
    }
    public void run(){
        nmip.recordOnce();
    }

}
