package com.biketomotor.kqj.network;

public interface HttpsCallBackListener {
    void success(String response);
    void error(Exception exception);
}
