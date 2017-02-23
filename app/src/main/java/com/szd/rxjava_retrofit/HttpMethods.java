package com.szd.rxjava_retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JY on 2016/11/14.
 */

public class HttpMethods {

    private static final long DEFAULT_TIMEOUT = 5000;
    Retrofit mRetrofitWeather;
    Retrofit mRetrofitLocation;
    WeatherService mWeatherService;
    LocationService mLocationService;
    public static final String WEATHER_URL = "http://apis.baidu.com/thinkpage/weather_api_full/";
    public static final String LOCATION_URL ="http://api.map.baidu.com/";
//    geocoder?location=31.407452,121.490523&output=json&key=6eea93095ae93db2c77be9ac910ff311";

    private HttpMethods() {

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofitWeather = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WEATHER_URL)
                .build();

        mWeatherService = mRetrofitWeather.create(WeatherService.class);

        mRetrofitLocation = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(LOCATION_URL)
                .build();

        mLocationService = mRetrofitLocation.create(LocationService.class);

    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void getWeather(Subscriber<WeatherEntity> subscriber, String city) {
        mWeatherService.getWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
