package com.caoyujie.basestorehouse.network.http;

import com.caoyujie.basestorehouse.commons.utils.StringUtils;
import com.caoyujie.basestorehouse.commons.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by caoyujie on 16/12/15.
 * 订阅者基类
 */
public abstract class BaseSubcriber<T> extends Subscriber<ResponseBody> {

    private Class<T> clx;

    public BaseSubcriber(Class<T> clx){
        this.clx = clx;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.shortToast("请求出错");
        e.printStackTrace();
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        InputStream inputStream = responseBody.byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while((line = reader.readLine()) != null){
                stringBuffer.append(line);
            }
            //解析数据之后回调给观察者
            if(!StringUtils.isEmpty(line = stringBuffer.toString())) {
                T data = parseResult(line);
                onResult(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析字符串
     */
    private T parseResult(String resultStr){
        return new Gson().fromJson(resultStr,clx);
    }


    /**
     * 返回处理结果
     */
    protected abstract void onResult(T result);
}
