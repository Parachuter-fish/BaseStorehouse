package com.caoyujie.basestorehouse.network.http;

import com.caoyujie.basestorehouse.network.http.api.ApiService;
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
 * 知乎日报RetrofitClient
 * 此类为了测试demo使用,正式开发中一般不存在多个域名,用一个RetrofitClient即可
 * api:
 * 知乎日报列头页GET: http://news-at.zhihu.com/api/4/news/latest
 * 知乎日报列表更多GET: http://news-at.zhihu.com/api/4/news/before/{date}  date从头页中拿取
 * 知乎日报列表页详情GET: http://news-at.zhihu.com/api/4/news/{id}          id从列表页中id拿取
 */
public class ZhihuRetrofitClient {
    private static ZhihuRetrofitClient INSTANCE;
    private static int DEFAULT_TIMEOUT = 5;
    public static final String BASE_URL = "http://news-at.zhihu.com/";
    private Retrofit retrofit;
    private ApiService apiService;

    private ZhihuRetrofitClient() {
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

    public static ZhihuRetrofitClient getInstance() {
        if (INSTANCE == null) {
            synchronized (ZhihuRetrofitClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ZhihuRetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    public void get(String path, BaseSubcriber subscriber) throws UnsupportedEncodingException {
        apiService.executeGET(path)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
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
