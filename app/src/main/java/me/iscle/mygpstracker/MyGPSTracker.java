package me.iscle.mygpstracker;

import android.app.Application;

public class MyGPSTracker extends Application {
    private Device currentDevice;

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }
}
