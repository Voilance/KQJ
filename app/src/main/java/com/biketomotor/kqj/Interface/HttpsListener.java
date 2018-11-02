package com.biketomotor.kqj.Interface;

public interface HttpsListener {

    void onSuccess(final String response);

    void onFailure(Exception exception);
}
