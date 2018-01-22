package com.dicosta.gpioclient.contracts;

import com.dicosta.gpioclient.domain.Light;

import java.util.List;

/**
 * Created by diego on 19/01/18.
 */

public interface LightsView {
    void setLights(List<Light> lightsList);
}
