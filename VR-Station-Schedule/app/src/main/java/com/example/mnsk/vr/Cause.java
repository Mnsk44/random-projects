package com.example.mnsk.vr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Cause-class generated from JSONArray
 * with jsonschema2pojo.org
 * @author Teemu Männikkö
 */


public class Cause {

    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("detailedCategoryCode")
    @Expose
    private String detailedCategoryCode;
    @SerializedName("thirdCategoryCode")
    @Expose
    private String thirdCategoryCode;
    @SerializedName("categoryCodeId")
    @Expose
    private Integer categoryCodeId;
    @SerializedName("detailedCategoryCodeId")
    @Expose
    private Integer detailedCategoryCodeId;
    @SerializedName("thirdCategoryCodeId")
    @Expose
    private Integer thirdCategoryCodeId;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDetailedCategoryCode() {
        return detailedCategoryCode;
    }

    public void setDetailedCategoryCode(String detailedCategoryCode) {
        this.detailedCategoryCode = detailedCategoryCode;
    }

    public String getThirdCategoryCode() {
        return thirdCategoryCode;
    }

    public void setThirdCategoryCode(String thirdCategoryCode) {
        this.thirdCategoryCode = thirdCategoryCode;
    }

    public Integer getCategoryCodeId() {
        return categoryCodeId;
    }

    public void setCategoryCodeId(Integer categoryCodeId) {
        this.categoryCodeId = categoryCodeId;
    }

    public Integer getDetailedCategoryCodeId() {
        return detailedCategoryCodeId;
    }

    public void setDetailedCategoryCodeId(Integer detailedCategoryCodeId) {
        this.detailedCategoryCodeId = detailedCategoryCodeId;
    }

    public Integer getThirdCategoryCodeId() {
        return thirdCategoryCodeId;
    }

    public void setThirdCategoryCodeId(Integer thirdCategoryCodeId) {
        this.thirdCategoryCodeId = thirdCategoryCodeId;
    }

}