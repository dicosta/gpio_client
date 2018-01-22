package com.dicosta.gpioclient.api;

import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.domain.LightState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by diego on 18/01/18.
 */

public interface LightAPI {

    @GET("api/pins")
    Observable<List<Light>> getAllLights();

    @PUT("api/pins/{id}")
    Completable writeLight(@Path("id") int lightId, @Body LightState lightState);
}
