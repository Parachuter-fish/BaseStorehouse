package com.caoyujie.basestorehouse.network.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
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
 */
public interface ApiService {

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
}
