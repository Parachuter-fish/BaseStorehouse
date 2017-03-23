package com.caoyujie.basestorehouse.network.http.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by caoyujie on 16/12/14.
 *
 * api:
 * 知乎日报列头页GET: http://news-at.zhihu.com/api/4/news/latest
 * 知乎日报列表更多GET: http://news-at.zhihu.com/api/4/news/before/{date}  date从头页中拿取
 * 知乎日报列表页详情GET: http://news-at.zhihu.com/api/4/news/{id}          id从列表页中id拿取
 * 美女列表GET: http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&offset=0
 */
public interface ApiService {
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";

    @GET
    Observable<ResponseBody> executeGET(@Url String path);

    @GET("{headParams}")
    Observable<ResponseBody> executeGET(
            @Path("headParams") String headParams,
            @QueryMap Map<String,String> dataParams
    );

   /* @GET
    Observable<ResponseBody> executeGET(
            @Url String headParams,
            @QueryMap Map<String,String> dataParams
    );*/

    @POST("{path}")
    Observable<ResponseBody> excutePOST(
            @Path("headParams") String path,
            @QueryMap Map<String,String> headParams,
            @FieldMap Map<String,String> dataParams
    );

    /**
     *  美女列表
     * @param params
     * @return
     */
    @Headers(CACHE_CONTROL_NETWORK)
    @GET("recommend/getChanListNews")
    Observable<ResponseBody> getMeinvList(
            @QueryMap Map<String,String> params
    );
}
