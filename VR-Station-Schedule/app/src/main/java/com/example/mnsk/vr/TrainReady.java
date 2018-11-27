package com.example.mnsk.vr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * TrainReady-class generated from JSONArray
 * with jsonschema2pojo.org
 * @author Teemu Männikkö
 */


public class TrainReady {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}