package com.caoyujie.basestorehouse.network.http;

import com.caoyujie.basestorehouse.mvp.bean.Movie;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by caoyujie on 16/12/14.
 */
public interface ApiService {
    @GET("{headParams}")
    Observable<ResponseBody> executeGET(
            @Path("headParams") String headParams,
            @QueryMap Map<String,String> dataParams
    );

    @POST("{path}")
    Observable<ResponseBody> excutePOST(
            @Path("headParams") String path,
            @QueryMap Map<String,String> headParams,
            @FieldMap Map<String,String> dataParams
    );
}
