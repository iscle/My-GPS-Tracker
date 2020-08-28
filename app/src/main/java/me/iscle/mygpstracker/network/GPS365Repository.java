package me.iscle.mygpstracker.network;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import me.iscle.mygpstracker.network.callback.PositionCallback;
import me.iscle.mygpstracker.network.deserializer.PositionResponseDeserializer;
import me.iscle.mygpstracker.network.response.PositionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPS365Repository {
    private static final String TAG = "GPS365Repository";

    private static final String GPS365_URL_1 = "http://120.76.28.239/";
    private static final String GPS365_URL_2 = "http://www.365gps.com/";
    private static final String GPS365_URL_3 = "http://120.76.241.191/";
    private static final String GPS365_URL_4 = "http://www.365gps.net/";

    private static final String HW = "apk";
    private static final String EXP = "1";
    private static final String VER = "3.69";
    private static final String APP = "n365";
    private static final String TM;
    private static GPS365Repository instance;
    private final GPS365Service service;

    static {
        try {
            //noinspection CharsetObjectCanBeUsed
            TM = URLEncoder.encode(new String((Build.MANUFACTURER + "-" + Build.VERSION.SDK_INT + " " + Build.MODEL + " " + Locale.getDefault().toString().replace("_#Hant", "").replace("zh_", "").replace("_#Hans", "")).getBytes(), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Something went terribly wrong...");
        }
    }

    private GPS365Repository() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(PositionResponse.class, new PositionResponseDeserializer())
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GPS365_URL_1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(GPS365Service.class);
    }

    public static GPS365Repository getInstance() {
        if (instance == null) {
            synchronized (GPS365Repository.class) {
                if (instance == null) {
                    instance = new GPS365Repository();
                }
            }
        }

        return instance;
    }

    public void imeiLogin(String imei, String password, final PositionCallback callback) {
        service.getPosition(imei, password).enqueue(new Callback<PositionResponse>() {
            @Override
            public void onResponse(Call<PositionResponse> call, Response<PositionResponse> response) {
                Log.d(TAG, "onResponse: " + response.toString());
                if (response.isSuccessful()) {
                    PositionResponse positionResponse = response.body();
                    if ("Error".equals(positionResponse.result)) {
                        callback.onError(PositionCallback.LoginError.PASSWORD_ERROR);
                    } else {
                        callback.onSuccess(positionResponse);
                    }
                } else {
                    callback.onError(PositionCallback.LoginError.SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<PositionResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError(PositionCallback.LoginError.NETWORK_ERROR);
            }
        });
    }
}
