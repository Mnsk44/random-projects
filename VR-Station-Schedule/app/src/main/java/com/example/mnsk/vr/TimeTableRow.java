package com.example.mnsk.vr;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * TimeTableRow-class generated from JSONArray
 * with jsonschema2pojo.org
 * @author Teemu Männikkö
 */


public class TimeTableRow {

    @SerializedName("stationShortCode")
    @Expose
    private String stationShortCode;
    @SerializedName("stationUICCode")
    @Expose
    private Integer stationUICCode;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("trainStopping")
    @Expose
    private Boolean trainStopping;
    @SerializedName("commercialStop")
    @Expose
    private Boolean commercialStop;
    @SerializedName("commercialTrack")
    @Expose
    private String commercialTrack;
    @SerializedName("cancelled")
    @Expose
    private Boolean cancelled;
    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime;
    @SerializedName("actualTime")
    @Expose
    private String actualTime;
    @SerializedName("differenceInMinutes")
    @Expose
    private Integer differenceInMinutes;
    @SerializedName("causes")
    @Expose
    private List<Cause> causes = null;
    @SerializedName("trainReady")
    @Expose
    private TrainReady trainReady;
    @SerializedName("liveEstimateTime")
    @Expose
    private String liveEstimateTime;
    @SerializedName("estimateSource")
    @Expose
    private String estimateSource;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getTrainStopping() {
        return trainStopping;
    }

    public void setTrainStopping(Boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public Boolean getCommercialStop() {
        return commercialStop;
    }

    public void setCommercialStop(Boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public String getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getDifferenceInMinutes() {
        return differenceInMinutes;
    }

    public void setDifferenceInMinutes(Integer differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(List<Cause> causes) {
        this.causes = causes;
    }

    public TrainReady getTrainReady() {
        return trainReady;
    }

    public void setTrainReady(TrainReady trainReady) {
        this.trainReady = trainReady;
    }

    public String getLiveEstimateTime() {
        return liveEstimateTime;
    }

    public void setLiveEstimateTime(String liveEstimateTime) {
        this.liveEstimateTime = liveEstimateTime;
    }

    public String getEstimateSource() {
        return estimateSource;
    }

    public void setEstimateSource(String estimateSource) {
        this.estimateSource = estimateSource;
    }

}