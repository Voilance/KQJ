package com.biketomotor.kqj.Thread;

import android.util.Log;

import com.biketomotor.kqj.Class.User;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SignThread {
    private static final String TAG = "TagSignThread";
    private static final int PORT = 2313;

    private boolean connectFlag;
    private SignThreadListener listener;

    public interface SignThreadListener {
        void onFinished(String m);
    }

    public void setConnectFlag(boolean flag) {
        this.connectFlag = flag;
    }

    public void setListener(SignThreadListener listener) {
        this.listener = listener;
    }

    public void sign(final String IP) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connectFlag) {
                    try (
                            Socket socket = new Socket(IP, PORT);
                            OutputStream outputStream = socket.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    ) {
                        objectOutputStream.writeObject(User.getAccount());
                        if (listener != null) {
                            listener.onFinished("已签到");
                        }
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.e(TAG, "sing:" + e.toString());
                        if (listener != null) {
                            listener.onFinished("签到中断");
                        }
                        break;
                    }
                }
            }
        }).start();
    }
}
