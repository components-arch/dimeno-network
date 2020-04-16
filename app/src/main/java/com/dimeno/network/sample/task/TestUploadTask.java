package com.dimeno.network.sample.task;

import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.task.UploadTask;

/**
 * TestUploadTask
 * Created by wangzhen on 2020/4/16.
 */
public class TestUploadTask extends UploadTask {
    public <EntityType> TestUploadTask(RequestCallback<EntityType> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {
        putFile("sourceFile", (String) params[0]);
    }

    @Override
    public String getApi() {
        return "http://49.233.208.223:8086/jyapi/audioFile2text.do";
    }
}
