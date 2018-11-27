package com.example.mnsk.vr;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Train-class generated from JSONArray
 * with jsonschema2pojo.org
 * @author Teemu Männikkö
 */

public class Train {

    @SerializedName("trainNumber")
    @Expose
    private Integer trainNumber;
    @SerializedName("departureDate")
    @Expose
    private String departureDate;
    @SerializedName("operatorUICCode")
    @Expose
    private Integer operatorUICCode;
    @SerializedName("operatorShortCode")
    @Expose
    private String operatorShortCode;
    @SerializedName("trainType")
    @Expose
    private String trainType;
    @SerializedName("trainCategory")
    @Expose
    private String trainCategory;
    @SerializedName("commuterLineID")
    @Expose
    private String commuterLineID;
    @SerializedName("runningCurrently")
    @Expose
    private Boolean runningCurrently;
    @SerializedName("cancelled")
    @Expose
    private Boolean cancelled;
    @SerializedName("version")
    @Expose
    private Long version;
    @SerializedName("timetableType")
    @Expose
    private String timetableType;
    @SerializedName("timetableAcceptanceDate")
    @Expose
    private String timetableAcceptanceDate;
    @SerializedName("timeTableRows")
    @Expose
    private List<TimeTableRow> timeTableRows = null;

    public Integer getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(Integer operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public String getOperatorShortCode() {
        return operatorShortCode;
    }

    public void setOperatorShortCode(String operatorShortCode) {
        this.operatorShortCode = operatorShortCode;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    public Boolean getRunningCurrently() {
        return runningCurrently;
    }

    public void setRunningCurrently(Boolean runningCurrently) {
        this.runningCurrently = runningCurrently;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(String timetableType) {
        this.timetableType = timetableType;
    }

    public String getTimetableAcceptanceDate() {
        return timetableAcceptanceDate;
    }

    public void setTimetableAcceptanceDate(String timetableAcceptanceDate) {
        this.timetableAcceptanceDate = timetableAcceptanceDate;
    }

    public List<TimeTableRow> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

}