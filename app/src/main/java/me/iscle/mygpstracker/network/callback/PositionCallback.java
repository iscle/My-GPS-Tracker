package me.iscle.mygpstracker.network.callback;

import me.iscle.mygpstracker.network.response.PositionResponse;

public interface PositionCallback {
    void onSuccess(PositionResponse response);
    void onError(LoginError error);

    enum LoginError {
        NETWORK_ERROR,
        SERVER_ERROR,
        PASSWORD_ERROR
    }
}
