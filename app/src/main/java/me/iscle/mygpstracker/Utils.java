package me.iscle.mygpstracker;

public class Utils {

    public static LocationSource getLocationSource(String source) {
        if (source != null) {
            final int parsedSource = Integer.parseInt(source);
            if (parsedSource == 0 || parsedSource == 2) {
                return LocationSource.LBS;
            } else if (parsedSource == 3) {
                return LocationSource.WIFI;
            }
        }

        return LocationSource.GPS;
    }

    public enum LocationSource {
        GPS,
        LBS,
        WIFI
    }
}
