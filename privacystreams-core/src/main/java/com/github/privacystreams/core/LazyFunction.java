package com.github.privacystreams.core;

import android.content.Context;

/**
 * Created by yuanchun on 01/12/2016.
 * An LazyFunction applies in background.
 *
 * Subclass must implement:
 * initOutput method which initializes the output object and returns in UI thread.
 * applyInBackground method which produce values to output in background.
 *
 * Subclass may implement:
 * 1. onStart method which is invoked when the function starts evaluating;
 * 2. onStop method which is invoked when the function stops evaluating,
 *    onStop is invoked before onFinish/onCancel,
 *    onStop should be mainly used for recycling objects.
 * 3. onFinish method which is invoked when the function is finished
 * 4. onCancel method which is invoked when the function is cancelled
 */

public abstract class LazyFunction<T1, T2> extends Function<T1, T2> {
    private FunctionEvaluator evaluator;
    private T1 input;
    private T2 output;
    private UQI uqi;
    private Context context;
    private volatile boolean isApplied = false;

    protected LazyFunction() {
        this.evaluator = new FunctionEvaluator();
    }

    public <Ttemp> LazyFunction<T1, Ttemp> compound(LazyFunction<T2, Ttemp> function) {
        return new LazyCompoundFunction<>(this, function);
    }

    protected final boolean isStarted() {
        return this.evaluator.isStarted;
    }
    protected final boolean isCancelled() {
        return this.evaluator.isCancelled();
    }
    protected final boolean isFinished() {
        return this.evaluator.isFinished;
    }
    protected final boolean isStopped() {
        return this.isCancelled() || this.isFinished();
    }

    public T2 apply(UQI uqi, T1 input) {
        this.uqi = uqi;
        this.context = uqi.getContext();
        this.input = input;
        this.output = this.initOutput(input);
        this.isApplied = true;
        return output;
    }

    public void evaluate() {
        if (!this.isApplied) return;
        this.evaluator.start();
    }

    public final void cancel() {
        this.evaluator.cancel();
    }
    public final void interrupt() {
        this.evaluator.interrupt();
    }

    protected UQI getUQI() {
        return this.uqi;
    }
    protected Context getContext() {
        return this.context;
    }

    protected abstract T2 initOutput(T1 input);
    protected abstract void applyInBackground(T1 input, T2 output);

    protected void onStart(T1 input, T2 output) {}
    protected void onStop(T1 input, T2 output) {}
    protected void onFinish(T1 input, T2 output) {}
    protected void onCancel(T1 input, T2 output) {}

//    private class FunctionEvaluator extends AsyncTask<Void, Void, Void> {
//        private boolean isStarted;
//        private boolean isFinished;
//
//        FunctionEvaluator() {
//            isStarted = false;
//            isFinished = false;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            onStart(input, output);
//            isStarted = true;
//            applyInBackground(input, output);
//            onStop(input, output);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            onFinish(input, output);
//            isFinished = true;
//        }
//
//        @Override
//        protected void onCancelled() {
//            onCancel(input, output);
//        }
//    }

    private class FunctionEvaluator extends Thread {
        private volatile boolean isStarted;
        private volatile boolean isFinished;
        private volatile boolean isCancelled;

        FunctionEvaluator() {
            isStarted = false;
            isFinished = false;
            isCancelled = false;
        }

        @Override
        public void run() {
            onStart(input, output);
            isStarted = true;
            applyInBackground(input, output);
            if (this.isCancelled) onCancel(input, output);
            else onFinish(input, output);
            onStop(input, output);
        }

        void cancel() {
            this.isCancelled = true;
        }

        boolean isCancelled() {
            return this.isCancelled;
        }
    }

}
