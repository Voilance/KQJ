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

public class SignService extends IntentService {
    private static final String TAG = "TagSignService";

    public class SignServiceBinder extends Binder {
        public SignService getService() {
            return SignService.this;
        }
    }

    public interface SignServiceListener {
        void onFinish(String m);
    }

    private static final int PORT = 2313;
    private String msg;
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
        try (ServerSocket sSocket = new ServerSocket()) {
            sSocket.setReuseAddress(true);
            sSocket.bind(new InetSocketAddress(PORT));
            try (
                    Socket cSocket = sSocket.accept();
                    InputStream inputStream = cSocket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ) {
                msg = (String)objectInputStream.readObject();
            }
        } catch (Exception e) {
            Log.e(TAG, "onHandleIntent:" + e.toString());
        } finally {
            if (listener != null) {
                listener.onFinish(msg);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setListener(SignServiceListener listener) {
        this.listener = listener;
    }
}
