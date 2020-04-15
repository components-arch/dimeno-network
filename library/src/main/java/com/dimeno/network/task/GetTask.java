package com.dimeno.network.task;

import com.dimeno.network.base.BaseTask;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.type.RequestType;

/**
 * get task
 * Created by wangzhen on 2020/4/15.
 */
public abstract class GetTask extends BaseTask {
    public <EntityType> GetTask(RequestCallback<EntityType> callback) {
        super(callback, RequestType.GET);
    }
}
