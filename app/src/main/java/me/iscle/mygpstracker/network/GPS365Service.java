package me.iscle.mygpstracker.network;

import me.iscle.mygpstracker.model.RoutePoint;
import me.iscle.mygpstracker.network.response.PositionResponse;
import me.iscle.mygpstracker.network.response.SpeedResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GPS365Service {

    @GET("n365_ilist.php")
    Call<PositionResponse> getPosition(@Query("imei") String imei, @Query("pw") String pw);

    @GET("n365_groute.php")
    Call<RoutePoint[]> getRoute(@Query("imei1") String imei, @Query("sd") String startDate, @Query("ed") String endDate, @Query("map") String map);

    @GET("n365_route.php")
    Call<SpeedResponse> getSpeed(@Query("imei") String imei);

}
