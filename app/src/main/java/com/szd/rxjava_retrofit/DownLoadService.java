package com.szd.rxjava_retrofit;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by JY on 2016/12/8.
 */

public interface DownLoadService {
    @GET("Jordy-Dur%20Dur%20D%27e%CC%82tre%20Be%CC%81be%CC%81.mp3")
    Observable<ResponseBody> getFile();
}
