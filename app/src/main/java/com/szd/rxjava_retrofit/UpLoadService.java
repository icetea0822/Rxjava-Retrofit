package com.szd.rxjava_retrofit;

import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by JY on 2016/12/8.
 */

public interface UpLoadService {
    @POST()
    Observable<String> upload(@Part("file")ResponseBody file);
}
