package com.dimeno.network.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimeno.network.base.Task;

/**
 * abstract loading page
 * Created by wangzhen on 2020/10/16.
 */
public abstract class AbsLoadingPage implements LoadingPage {
    private final View mOriginalView;
    private View mReplaceView;
    private int mPageViewIndex;
    private ViewGroup parent;
    private Task task;

    private int mDuration = 300;
    private long mDelay = 0;

    public AbsLoadingPage(View originalView) {
        this.mOriginalView = originalView;
        apply();
    }

    private void apply() {
        if (mOriginalView != null && mOriginalView.getParent() != null) {
            parent = (ViewGroup) mOriginalView.getParent();
            onViewCreated(mReplaceView = LayoutInflater.from(parent.getContext()).inflate(layoutId(), parent, false));
            mPageViewIndex = parent.indexOfChild(mOriginalView);
            parent.removeView(mOriginalView);
            parent.addView(mReplaceView, mPageViewIndex, mOriginalView.getLayoutParams());
        }
    }

    protected void finishLoad() {
        if (mReplaceView != null && mReplaceView.getParent() instanceof ViewGroup) {
            if (mOriginalView.getParent() instanceof ViewGroup) {
                ((ViewGroup) mOriginalView.getParent()).removeView(mOriginalView);
            }
            parent = (ViewGroup) mReplaceView.getParent();
            if (parent != null) {
                parent.addView(mOriginalView, mPageViewIndex);

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        mOriginalView.setAlpha(fraction);
                        mReplaceView.setAlpha(1 - fraction);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        parent.removeView(mReplaceView);
                    }
                });
                animator.setDuration(mDuration);
                animator.start();
            }
        }
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
    }

    protected void retry() {
        if (this.task != null) {
            this.task.retry();
        }
    }

    @Override
    public void onSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishLoad();
            }
        }, mDelay);
    }

    @Override
    public void onError() {
        if (mOriginalView != null)
            onLoadError();
    }

    public LoadingPage setDelay(long delay) {
        if (delay > 0) {
            mDelay = delay;
        }
        return this;
    }

    public LoadingPage setDuration(int duration) {
        if (duration >= 0) {
            mDuration = duration;
        }
        return this;
    }

    protected abstract int layoutId();

    protected abstract void onViewCreated(View view);

    protected abstract void onLoadError();
}
