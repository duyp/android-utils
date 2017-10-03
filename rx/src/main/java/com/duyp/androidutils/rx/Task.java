package com.duyp.androidutils.rx;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by phamd on 9/29/2017.
 * Class consist of functions to run a task on background thread based on Rx {@link Completable}
 */

public class Task {

    // action to be run
    @NonNull
    private final Action action;

    // action run after operation completed
    private Action onComplete;

    // true if should observe on main thread
    private boolean observeMainThread;

    // Scheduler on which action run, default is a new thread (background)
    @NonNull
    private Scheduler scheduler = Schedulers.newThread();

    public Task(@NonNull Action action) {
        this.action = action;
        observeMainThread = false;
    }

    // action will be observed on main thread
    public Task observeMainThread() {
        observeMainThread = true;
        return this;
    }

    public Task observeMainThread(boolean observeMainThread) {
        this.observeMainThread = observeMainThread;
        return this;
    }

    /**
     * @param onComplete action on complete
     */
    public Task onComplete(@NonNull Action onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    /**
     * @param scheduler which action will be run on
     */
    public Task onScheduler(@NonNull Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * Convert a task to a {@link Completable}
     * @param safe true if should ignored all exception
     * @return completable
     */
    public Completable toCompletable(boolean safe) {
        Completable completable = Completable.create(e -> {
            Log.d("task", "performing task on " + Thread.currentThread().toString());
            if (safe) {
                try {
                    action.run();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            } else {
                action.run();
            }
            e.onComplete();
        });
        if (observeMainThread) {
            completable = completable.observeOn(AndroidSchedulers.mainThread());
        }
        return completable.subscribeOn(scheduler);
    }

    /**
     * Run the action
     */
    public void run() {
        run(false);
    }

    /**
     * Run the action safely (all {@link Exception} will be ignored)
     */
    public void runSafely() {
        run(true);
    }

    /**
     * Run the action
     * @param safe true if should ignore exceptions
     */
    private void run(boolean safe) {
        Completable completable = toCompletable(safe);
        if (onComplete != null) {
            completable.subscribe(onComplete);
        } else {
            completable.subscribe();
        }
    }

    /**
     * Do a task on a new thread (background thread)
     * DON'T create too much task by this function, this might cause thread count exceed Exception
     * @param action action to be run
     */
    public static void run(@NonNull Action action) {
        new Task(action).run();
    }

    /**
     * Do a task on a new thread (background thread)
     * DON'T create too much task by this function, this might cause thread count exceed Exception
     * @param action action to be run (upstream)
     * @param onComplete complete action (downstream)
     * @param observeMainThread true if should observe on main thread
     */
    public static void run(@NonNull Action action, @NonNull Action onComplete, boolean observeMainThread) {
        new Task(action).onComplete(onComplete).observeMainThread(observeMainThread).run();
    }

    /**
     * Do a task safely on a new thread (background thread) (All exception will be ignored)
     * DON'T create too much task by this function, this might cause thread count exceed Exception
     * @param action action to be run
     */
    public static void runSafely(@NonNull Action action) {
        new Task(action).runSafely();
    }

    /**
     * Do a task safely on a new thread (background thread) (All exception will be ignored)
     * DON'T create too much task by this function, this might cause thread count exceed Exception
     * @param action action to be run (upstream)
     * @param onComplete complete action (downstream)
     * @param observeMainThread true if should observe on main thread
     */
    public static void runSafely(@NonNull Action action, @NonNull Action onComplete, boolean observeMainThread) {
        new Task(action).onComplete(onComplete).observeMainThread(observeMainThread).runSafely();
    }
}
