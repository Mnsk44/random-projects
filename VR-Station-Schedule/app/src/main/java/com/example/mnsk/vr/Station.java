package com.example.mnsk.vr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Station-class generated from JSONArray
 * with jsonschema2pojo.org
 * @author Teemu Männikkö
 */


public class Station {

    @SerializedName("passengerTraffic")
    @Expose
    private Boolean passengerTraffic;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("stationName")
    @Expose
    private String stationName;
    @SerializedName("stationShortCode")
    @Expose
    private String stationShortCode;
    @SerializedName("stationUICCode")
    @Expose
    private Integer stationUICCode;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public Boolean getPassengerTraffic() {
        return passengerTraffic;
    }

    public void setPassengerTraffic(Boolean passengerTraffic) {
        this.passengerTraffic = passengerTraffic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public Integer getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(Integer stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}