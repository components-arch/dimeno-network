package com.dimeno.network.task;

import com.dimeno.network.base.BaseTask;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.type.RequestType;

/**
 * upload task
 * Created by wangzhen on 2020/4/16.
 */
public abstract class UploadTask extends BaseTask {
    public <EntityType> UploadTask(RequestCallback<EntityType> callback) {
        super(callback, RequestType.UPLOAD);
    }
}
