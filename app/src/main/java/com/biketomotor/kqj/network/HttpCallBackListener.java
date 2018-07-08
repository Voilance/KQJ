package com.biketomotor.kqj.network;

public interface HttpCallBackListener {

    //  服务器成功响应请求
    void success(String response);
    //  网络操作出现错误
    void error(Exception exception);
}
