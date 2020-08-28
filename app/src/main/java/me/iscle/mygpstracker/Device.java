package me.iscle.mygpstracker;

public class Device {
    private final String imei;
    private String password;

    public Device(String imei, String password) {
        this.imei = imei;
        this.password = password;
    }

    public String getImei() {
        return imei;
    }

    public String getPassword() {
        return password;
    }
}
