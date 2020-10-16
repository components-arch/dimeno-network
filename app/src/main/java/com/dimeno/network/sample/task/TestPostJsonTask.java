package com.dimeno.network.sample.task;

import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.task.PostJsonTask;

/**
 * TestPostJsonTask
 * Created by wangzhen on 2020/4/16.
 */
public class TestPostJsonTask extends PostJsonTask {
    public <EntityType> TestPostJsonTask(RequestCallback<EntityType> callback) {
        super(callback);
    }

    @Override
    public String getApi() {
        return "http://192.168.10.111:8080/wangzhen/plugin/plugin.json";
    }
}
