package com.szd.rxjava_retrofit;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JY on 2016/11/14.
 */

public interface WeatherService {
    //TODO 将apikey修改为自己在百度Api中申请的key
    @Headers({"apikey:*********"})
    @GET("currentweather")
    Observable<WeatherEntity> getWeather(@Query("location") String city);
}
