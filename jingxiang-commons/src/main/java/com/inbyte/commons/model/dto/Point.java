package com.inbyte.commons.model.dto;

import java.io.Serializable;

public class Point implements Serializable {
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    public Point() {
    }

    /**
     * 创建经维度坐标
     *
     * @param latitude 纬度
     * @param longitude 经度
     */
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}