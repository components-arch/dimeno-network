package com.dimeno.network.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dimeno.network.provider.ContextProvider;

/**
 * LifecycleManager
 * Created by wangzhen on 2020/4/16.
 */
public class LifecycleManager {
    private static ViewLifecycleObserver mViewObserver;

    /**
     * let activity lifecycle manage binding request calls
     */
    public static void registerActivities() {
        Context ctx = ContextProvider.sContext;
        if (ctx instanceof Application) {
            ((Application) ctx).registerActivityLifecycleCallbacks(new ActivityLifecycleObserver());
        }
    }

    /**
     * let the view lifecycle manage binding request calls
     *
     * @param view view
     */
    public static void registerView(View view) {
        if (view != null) {
            if (mViewObserver == null) {
                mViewObserver = new ViewLifecycleObserver();
            }
            view.removeOnAttachStateChangeListener(mViewObserver);
            view.addOnAttachStateChangeListener(mViewObserver);
        }
    }

    private static class ActivityLifecycleObserver implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            CallManager.get().cancel(activity);
        }
    }

    private static class ViewLifecycleObserver implements View.OnAttachStateChangeListener {

        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            CallManager.get().cancel(v);
        }
    }
}
