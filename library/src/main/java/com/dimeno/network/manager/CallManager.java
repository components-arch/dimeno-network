package com.dimeno.network.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * call manager
 * Created by wangzhen on 2020/4/15.
 */
public class CallManager {
    private static CallManager instance = new CallManager();

    private Map<Object, Set<Call>> mCalls = new HashMap<>();

    public static CallManager get() {
        return instance;
    }

    /**
     * add call with tag
     *
     * @param tag  tag
     * @param call call
     */
    public void add(Object tag, Call call) {
        Set<Call> calls = mCalls.get(tag);
        if (calls == null) {
            calls = Collections.synchronizedSet(new HashSet<Call>());
            mCalls.put(tag, calls);
        }
        calls.add(call);
    }

    /**
     * remove calls with tag
     *
     * @param tag tag
     */
    public void remove(Object tag) {
        mCalls.remove(tag);
    }

    /**
     * remove single call with tag
     *
     * @param tag  tag
     * @param call call
     */
    public void removeCall(Object tag, Call call) {
        Set<Call> calls = mCalls.get(tag);
        if (calls != null && !calls.isEmpty()) {
            calls.remove(call);
            if (calls.isEmpty()) {
                remove(tag);
            }
        }
    }

    /**
     * cancel calls by tag
     *
     * @param tag tag
     */
    public void cancel(Object tag) {
        Set<Call> calls = mCalls.get(tag);
        if (calls != null && !calls.isEmpty()) {
            for (Call call : calls) {
                call.cancel();
            }
            calls.clear();
        }
        mCalls.remove(tag);
    }
}
