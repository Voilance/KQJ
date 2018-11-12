package com.biketomotor.kqj.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SignTask extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = "TagSignTask";
    private static final int PORT = 2313;
    private String msg;

    public SignTask(String m) {
        this.msg = m;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try (
                Socket socket = new Socket(strings[0], SignTask.PORT);
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ) {
            objectOutputStream.writeObject(this.msg);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground:" + e.toString());
        }
        return false;
    }
}
