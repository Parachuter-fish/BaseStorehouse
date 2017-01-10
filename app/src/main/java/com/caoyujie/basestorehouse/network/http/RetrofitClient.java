package com.caoyujie.basestorehouse.network.http;

import com.caoyujie.basestorehouse.mvp.bean.Movie;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by caoyujie on 16/12/14.
 */
public class RetrofitClient {
    private static RetrofitClient INSTANCE;
    private static int DEFAULT_TIMEOUT = 5;
    public static final String BASE_URL = "https://api.douban.com/";
    private Retrofit retrofit;
    private ApiService apiService;

    private RetrofitClient() {
        //构造一个OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Gson gson = new Gson();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    public void get(String path, Map<String, String> headParams, BaseSubcriber subscriber) throws UnsupportedEncodingException {
        apiService.executeGET(path, headParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void post(String path, Map<String, String> headParams, Map<String, String> dataParams
            , Subscriber<ResponseBody> subscriber) {
        apiService.excutePOST(path, headParams, dataParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
