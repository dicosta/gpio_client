package com.dicosta.gpioclient.net;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Singleton
public final class HttpClientProvider {

    private OkHttpClient mClient;

    @Inject
    HttpClientProvider() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(logging);

        mClient = client.build();
    }

    public OkHttpClient getHTTPClient() {
        return mClient;
    }
}
