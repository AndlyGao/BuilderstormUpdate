/**
 * Copyright (C) 2018 iDEA foundation pvt.,Ltd
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.builderstrom.user.data.retrofit.api;

import android.text.TextUtils;

import com.builderstrom.user.utils.BuilderStormApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anilsinghania on 03-12-2018.
 */

public class RestClient {
    private static final String HTTP_PREFIX = "https://";
    private static final String DOMAIN_NAME = ".builderstorm.com/";
    private static Retrofit retrofit;
    private static ApiWebInterface webInterface;

    public static ApiWebInterface createService() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(getBaseImageUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient()).build();
        webInterface = retrofit.create(ApiWebInterface.class);

        return webInterface;
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.readTimeout(40, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS);
        /* logging interceptor */
        okHttpClient.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        okHttpClient.addInterceptor(chain -> {
            Request request = chain.request();
            String token = BuilderStormApplication.mPrefs.getAuthToken();
            Request.Builder builder1 = request.newBuilder().header("authToken", token);
            Request tokenRequest = builder1.build();
            return chain.proceed(tokenRequest);
        });

        return okHttpClient.build();
    }


    private static String getDevBaseUrl() {
        return TextUtils.concat(HTTP_PREFIX, "dev", DOMAIN_NAME).toString();
    }

    public static String getBaseImageUrl() {
        return TextUtils.concat(HTTP_PREFIX, BuilderStormApplication.mPrefs.getBaseSite(), DOMAIN_NAME).toString();
    }

    public static Retrofit getRetrofitInstance() {
        return retrofit;
    }


}
