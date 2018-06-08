package com.dicosta.gpioclient.net;

import com.dicosta.gpioclient.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class HttpServiceFactory {

    private Retrofit mRetrofit;

    @Inject
    HttpServiceFactory(HttpClientProvider httpClientProvider, SerializerProvider serializerProvider) {
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(serializerProvider.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.URL)
                .client(httpClientProvider.getHTTPClient())
                .build();
    }

    public <T> T create(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }
}
