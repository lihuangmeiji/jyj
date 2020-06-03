package com.idougong.jyj.common.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wujiajun on 17/4/11.
 */

public class CookieInterceptor implements Interceptor
{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header(CookieHead.USER_NAME.getHead(), CookieHead.USER_NAME.getValue())
                .header(CookieHead.CLIENT_TYPE.getHead(), CookieHead.CLIENT_TYPE.getValue())
                .header("use-personnel",EnvConstant.isInsTypes ? "isolate": "noIsolate")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
