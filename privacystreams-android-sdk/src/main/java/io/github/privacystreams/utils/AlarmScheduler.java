package io.github.privacystreams.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

/**
 * A scheduler based on alarm manager.
 */

public abstract class AlarmScheduler {
    private PendingIntent mAlarmIntent;
    private AlarmManager am;
    private BroadcastReceiver mReceiver;

    private Context ctx;

    public AlarmScheduler(Context ctx, String actionToken) {
        this.ctx = ctx;
        am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(actionToken), 0);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                run();
            }
        };
        ctx.registerReceiver(mReceiver, new IntentFilter(actionToken));
    }

    protected abstract void run();

    public final void schedule(long delayMillis) {
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delayMillis, mAlarmIntent);
    }

    public final void destroy() {
        am.cancel(mAlarmIntent);
        ctx.unregisterReceiver(mReceiver);
    }
}
