package com.dicosta.gpioclient.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SerializerProvider {

    private Gson mGson;

    @Inject
    SerializerProvider() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        mGson = gsonBuilder.create();
    }

    public Gson getGson() {
        return mGson;
    }
}
