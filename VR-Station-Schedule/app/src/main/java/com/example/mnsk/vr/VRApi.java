package com.example.mnsk.vr;

import com.google.gson.JsonArray;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface for connecting to VR REST-API
 * @author Teemu Männikkö
 */

public interface VRApi {

    @GET("api/v1/metadata/stations")
    Call<JsonArray> getStations();

    @GET("api/v1/live-trains/station/{stationcode}?arriving_trains=20&departing_trains=20&include_nonstopping=false")
    Call<JsonArray> getTrains(@Path("stationcode") String stationcode);
}
