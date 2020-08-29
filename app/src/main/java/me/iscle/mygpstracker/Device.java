package me.iscle.mygpstracker;

public class Device {
    private final String imei;
    private String name;
    private String password;

    public Device(String imei, String name, String password) {
        this.imei = imei;
        this.name = name;
        this.password = password;
    }

    public String getImei() {
        return imei;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
