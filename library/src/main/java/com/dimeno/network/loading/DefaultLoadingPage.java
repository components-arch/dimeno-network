package com.dimeno.network.loading;

import android.view.View;

import com.dimeno.network.R;

/**
 * default loading page
 * Created by wangzhen on 2020/10/16.
 */
public final class DefaultLoadingPage extends AbsLoadingPage {

    private View mContainerLoading;
    private View mContainerError;

    public DefaultLoadingPage(View pageView) {
        super(pageView);
    }

    @Override
    protected int layoutId() {
        return R.layout.layout_loading_page;
    }

    @Override
    protected void onViewCreated(View view) {
        mContainerLoading = view.findViewById(R.id.container_loading);
        mContainerError = view.findViewById(R.id.container_error);
        mContainerError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading();
                retry();
            }
        });
        startLoading();
    }

    public void startLoading() {
        mContainerLoading.setVisibility(View.VISIBLE);
        mContainerError.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        mContainerLoading.setVisibility(View.GONE);
        mContainerError.setVisibility(View.VISIBLE);
    }
}
