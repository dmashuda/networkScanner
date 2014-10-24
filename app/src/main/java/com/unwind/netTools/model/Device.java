package com.unwind.netTools.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dan on 10/24/14.
 */
public class Device{
    private String ipAddress;
    private String deviceName;
    private String macAddress;

    public Device(String ipAddress, String macAddress, String deviceName) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.deviceName = deviceName;
    }

    public Device() {
        this.ipAddress = String.valueOf("");
        this.macAddress = String.valueOf("");
        this.deviceName = String.valueOf("");
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
