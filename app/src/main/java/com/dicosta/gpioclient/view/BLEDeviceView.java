package com.dicosta.gpioclient.view;

import com.dicosta.gpioclient.domain.Light;

import java.util.List;

public interface BLEDeviceView extends MVPView {
    void setLights(List<Light> lightsList);
}
