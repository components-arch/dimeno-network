package com.dimeno.network.sample.task;

import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.task.GetTask;

/**
 * TestGetTask
 * Created by wangzhen on 2020/4/15.
 */
public class TestGetTask extends GetTask {

    public <EntityType> TestGetTask(RequestCallback<EntityType> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {

    }

    @Override
    public String getApi() {
        return "http://192.168.188.199:8080/wangzhen/plugin/plugin.json";
    }
}
