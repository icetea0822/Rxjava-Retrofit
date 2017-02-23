package com.szd.rxjava_retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JY on 2016/12/6.
 */

public interface LocationService {
    @GET("geocoder")
    Observable<LocationEntity> getLoation(@Query("location") String location,
                                          @Query("output") String output, @Query("key") String key

    );

}
