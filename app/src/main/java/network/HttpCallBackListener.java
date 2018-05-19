package network;

/**
 * Created by Voilance on 2018/5/19.
 */

public interface HttpCallBackListener {

    void onFinish(String response);

    void onError(Exception exception);
}
