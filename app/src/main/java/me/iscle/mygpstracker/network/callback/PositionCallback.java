package me.iscle.mygpstracker.network.callback;

import me.iscle.mygpstracker.network.response.PositionResponse;

public interface PositionCallback {
    void onSuccess(PositionResponse response);
    void onError(PositionError error);

    enum PositionError {
        NETWORK_ERROR,
        SERVER_ERROR,
        PASSWORD_ERROR
    }
}
