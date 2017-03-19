package edu.cmu.chimps.love_study;

import android.os.Handler;

/**
 * Performs a task repeatedly until stopped. Can be started and stopped multiple times.
 */
public abstract class RepeatingTask implements Runnable {

    private Handler handler;

    protected int frequency;

    /**
     * Constructs the task using a new handler.
     * @param frequency running frequency in milliseconds
     */
    public RepeatingTask(int frequency) {
        this(frequency, new Handler());
    }

    /**
     * Constructs the task with a specified handler.
     * @param frequency running frequency in milliseconds
     * @param handler   handler to post tasks on
     */
    public RepeatingTask(int frequency, Handler handler) {
        this.frequency = frequency;
        this.handler = handler;
    }

    protected abstract void doWork();

    /**
     * Starts the task.
     */
    @Override
    public void run() {
        stop();
        doWork();
        handler.postDelayed(this, frequency);
    }

    /**
     * Stops the task.
     */
    public void stop() {
        handler.removeCallbacks(this);
    }

}