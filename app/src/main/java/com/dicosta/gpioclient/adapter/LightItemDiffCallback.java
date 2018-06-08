package com.dicosta.gpioclient.adapter;

import android.support.v7.util.DiffUtil;

import com.dicosta.gpioclient.domain.Light;

import java.util.List;

public class LightItemDiffCallback extends DiffUtil.Callback {

    List<Light> mOldLights;
    List<Light> mNewLights;

    public LightItemDiffCallback(List<Light> newLights, List<Light> oldLights) {
        mOldLights = oldLights;
        mNewLights = newLights;
    }

    @Override
    public int getOldListSize() {
        return mOldLights.size();
    }

    @Override
    public int getNewListSize() {
        return mNewLights.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldLights.get(oldItemPosition).getId() == mNewLights.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Light newLight = mNewLights.get(newItemPosition);
        Light oldLight = mOldLights.get(oldItemPosition);

        return newLight.getState().equals(oldLight.getState()) &&
                newLight.getColor().equals(oldLight.getColor()) &&
                newLight.getName().equals(oldLight.getName());
    }
}
