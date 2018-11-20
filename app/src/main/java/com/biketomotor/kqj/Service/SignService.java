package com.biketomotor.kqj.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class SignService extends IntentService {
    private static final String TAG = "TagSignService";

    public class SignServiceBinder extends Binder {
        public SignService getService() {
            return SignService.this;
        }
    }

    public interface SignServiceListener {
        void onConnect(String m);
        void onDisconnect(String m);
        void onFinish();
    }

    private static final int PORT = 2313;
    private String msg;
    private boolean connectFlag;
    private SignServiceListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SignServiceBinder();
    }

    public SignService() {
        super("SignService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (connectFlag) {
            Log.e(TAG, "while");
            try (
                    ServerSocket sSocket = new ServerSocket();
            ) {
                Log.e(TAG, "try0");
                sSocket.setReuseAddress(true);
                sSocket.bind(new InetSocketAddress(PORT));
                try (
                        Socket cSocket = sSocket.accept();
                        InputStream inputStream = cSocket.getInputStream();
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                ) {
                    Log.e(TAG, "try1");
                    msg = (String)objectInputStream.readObject();
                    if (listener != null) {
                        listener.onConnect(msg);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "onHandleIntent:" + e.toString());
                if (listener != null) {
                    listener.onDisconnect(null);
                }
                break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listener != null) {
            listener.onFinish();
        }
    }

    public void setConnectFlag(boolean flag) {
        this.connectFlag = flag;
    }

    public void setListener(SignServiceListener listener) {
        this.listener = listener;
    }
}
