package com.dicosta.gpioclient.viewmodel;

/**
 * Created by diego on 23/01/18.
 */

public class ScanResultViewModel {

    private String mMacAddress;
    private String mName;
    private int mRSSI;

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getRSSI() {
        return mRSSI;
    }

    public void setRSSI(int mRSSI) {
        this.mRSSI = mRSSI;
    }

    public static class Builder {

        private String macAddress;
        private String name;
        private int rssi;

        public Builder setMacAddress(String macAddress) {
            this.macAddress = macAddress;
            return this;
        }

        public Builder setName(String name) {
            this.name= name;
            return this;
        }

        public Builder setRssi(int rssi) {
            this.rssi = rssi;
            return this;
        }

        public ScanResultViewModel build() {
            ScanResultViewModel retValue = new ScanResultViewModel();

            retValue.setMacAddress(macAddress);
            retValue.setName(name);
            retValue.setRSSI(rssi);

            return retValue;
        }
    }
}
