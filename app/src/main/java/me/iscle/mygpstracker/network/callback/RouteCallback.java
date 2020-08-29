package me.iscle.mygpstracker.network.callback;

import me.iscle.mygpstracker.model.RoutePoint;

public interface RouteCallback {
    void onSuccess(RoutePoint[] response);
    void onError(RouteError error);

    enum RouteError {
        NETWORK_ERROR,
        SERVER_ERROR,
        PASSWORD_ERROR
    }
}
