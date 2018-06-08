package com.dicosta.gpioclient.contracts;

import com.dicosta.gpioclient.domain.Light;

import java.util.List;

/**
 * Created by diego on 24/01/18.
 */

public interface DeviceView {
    void setLights(List<Light> lightsList);
}
