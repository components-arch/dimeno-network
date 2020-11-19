package com.dimeno.network.loading;

import com.dimeno.network.base.Task;

/**
 * loading page callback
 * Created by wangzhen on 2020/10/16.
 */
public interface LoadingPage {
    /**
     * hold a task instance
     *
     * @param task task
     */
    void setTask(Task task);

    /**
     * load success
     */
    void onSuccess();

    /**
     * error occurs
     */
    void onError();

    /**
     * set end delay
     *
     * @param delay millis seconds
     */
    LoadingPage setDelay(long delay);

    /**
     * set end animation duration
     *
     * @param duration millis seconds
     */
    LoadingPage setDuration(int duration);
}
