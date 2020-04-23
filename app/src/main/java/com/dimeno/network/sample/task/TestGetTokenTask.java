package com.dimeno.network.sample.task;

import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.task.GetTask;

/**
 * TestGetTokenTask
 * Created by wangzhen on 2020/4/23.
 */
public class TestGetTokenTask extends GetTask {
    public <EntityType> TestGetTokenTask(RequestCallback<EntityType> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {
        put("userName", params[0]);
        put("secret", params[1]);
    }

    @Override
    public String getApi() {
        return "https://jyai.birdbot.cn/jyapi/getToken.do";
    }
}
