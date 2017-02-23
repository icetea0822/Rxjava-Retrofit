package com.szd.rxjava_retrofit;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.szd.rxjava_retrofit.HttpMethods.LOCATION_URL;
import static com.szd.rxjava_retrofit.HttpMethods.WEATHER_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAnalytics mFirebaseAnalytics;

    Retrofit mRetrofitWeather;
    Retrofit mRetrofitLocation;
    WeatherService mWeatherService;
    LocationService mLocationService;
    Retrofit mRetrofitDownload;
    DownLoadService mDownloadService;

    @Bind(R.id.tv)
    TextView tv;

    @Bind(R.id.btn)
    Button btn;

    @Bind(R.id.btn2)
    Button btn2;

    @Bind(R.id.btn3)
    Button btn3;


    String musicUrl = "http://7xscpv.com1.z0.glb.clouddn.com/";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseCrash.log("Activity created");
        FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");

        ButterKnife.bind(this);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();


        mRetrofitWeather = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WEATHER_URL)
                .client(client)
                .build();
        mWeatherService = mRetrofitWeather.create(WeatherService.class);

        mRetrofitLocation = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(LOCATION_URL)
                .client(client)
                .build();
        mLocationService = mRetrofitLocation.create(LocationService.class);

        mRetrofitDownload = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(musicUrl)
                .client(client)
                .build();
        mDownloadService = mRetrofitDownload.create(DownLoadService.class);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public class Notification extends FirebaseMessagingService{
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            Log.d(TAG, "onMessageReceived: "+remoteMessage.getNotification().getBody().toString());
        }
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn:
                getWeather();
                break;
            case R.id.btn2:
                mLocationService.getLoation("31.407452,119.490523"
                        , "json", "6eea93095ae93db2c77be9ac910ff311")
                        .flatMap(new Func1<LocationEntity, Observable<WeatherEntity>>() {
                            @Override
                            public Observable<WeatherEntity> call(LocationEntity locationEntity) {
                                Log.d(TAG, "Location: " + locationEntity.getResult().getFormatted_address());
                                return mWeatherService.getWeather(locationEntity.getResult().getAddressComponent().getCity());
                            }
                        })
                        .map(new Func1<WeatherEntity, SpannableString>() {
                            @Override
                            public SpannableString call(WeatherEntity weatherEntity) {
                                Log.d(TAG, "Weather: " + weatherEntity.getResults().get(0).getNow());

                                String text = weatherEntity.getResults().get(0).getLocation().getName() + ":"
                                        + weatherEntity.getResults().get(0).getNow().getText()
                                        + " 温度:" + weatherEntity.getResults().get(0).getNow().getTemperature() + "●";
                                Log.d(TAG, "call: "+ text);
                                SpannableString spannableString = new SpannableString(text);

                                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2.0f);
                                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                                UnderlineSpan underlineSpan = new UnderlineSpan();
                                SuperscriptSpan superscriptSpan = new SuperscriptSpan();

                                spannableString.setSpan(relativeSizeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(strikethroughSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(underlineSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(superscriptSpan, text.length() - 1, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(foregroundColorSpan, text.length() - 1, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(foregroundColorSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                return spannableString;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<SpannableString>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted: " + "完成数据获取");
                            }

                            @Override
                            public void onError(Throwable e) {
                                tv.setText(e.toString());
                                FirebaseCrash.report(e);

                            }


                            @Override
                            public void onNext(SpannableString s) {
                                tv.setText(s);
                                Log.d(TAG, "onNext: " + s + "Thread:" + Thread.currentThread());
                            }
                        });
                break;
            case R.id.btn3:
                mDownloadService.getFile()
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<ResponseBody, File>() {
                            @Override
                            public File call(ResponseBody responseBody) {
                                Log.d(TAG, "call: " + responseBody.contentType());
                                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/123.mp3");
                                InputStream is = responseBody.byteStream();
                                OutputStream os = null;
                                try {
                                    os = new BufferedOutputStream(new FileOutputStream(file, false));
                                    byte data[] = new byte[1024];
                                    int len;
                                    while ((len = is.read(data, 0, 1024)) != -1) {
                                        os.write(data, 0, len);
                                    }
                                    os.close();
                                    is.close();
                                    return file;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        })
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<File>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                Log.d(TAG, "onStart: 开始下载");
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                FirebaseCrash.report(e);
                            }

                            @Override
                            public void onNext(File file) {
                                Log.d(TAG, "onNext: " + file.getName());
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                try {
                                    mediaPlayer.setDataSource(file.getPath());
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    FirebaseCrash.report(e);
                                }
                            }
                        });
                break;
        }
    }


    /**
     * 普通单次请求
     */
    @Deprecated
    private void getWeather() {
        HttpMethods httpMethods = HttpMethods.getInstance();
        httpMethods.getWeather(new Subscriber<WeatherEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WeatherEntity weatherEntity) {
                String text = weatherEntity.getResults().get(0).getNow().getText();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onNext: " + text);
                tv.setText(text);
                Log.d(TAG, "onNext: " + "text");
            }
        }, "beijing");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
