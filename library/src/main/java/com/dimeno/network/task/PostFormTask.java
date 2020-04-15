package com.dimeno.network.task;

import com.dimeno.network.base.BaseTask;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.type.RequestType;

/**
 * post form task
 * Created by wangzhen on 2020/4/15.
 */
public abstract class PostFormTask extends BaseTask {
    public <EntityType> PostFormTask(RequestCallback<EntityType> callback) {
        super(callback, RequestType.POST_FORM);
    }
}
