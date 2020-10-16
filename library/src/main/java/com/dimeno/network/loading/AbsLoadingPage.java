package com.dimeno.network.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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

    public AbsLoadingPage(@NonNull View originalView) {
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
                mReplaceView.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        parent.removeView(mReplaceView);
                    }
                });
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
        finishLoad();
    }

    protected abstract int layoutId();

    protected abstract void onViewCreated(View view);
}
