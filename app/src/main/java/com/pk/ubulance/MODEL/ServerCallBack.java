package com.pk.ubulance.Model;

import org.json.JSONException;

/**
 * Created by Pravin on 10/8/16.
 * Project: Ubulance
 */

public interface ServerCallBack {
    void onSuccess(Object result) throws JSONException;
    void onFailure(Boolean bool);
}
