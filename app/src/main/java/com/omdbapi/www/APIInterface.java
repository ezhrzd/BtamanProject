package com.omdbapi.www;

import com.omdbapi.www.models.Details;
import com.omdbapi.www.models.MainListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("?apikey&s")
    Call<MainListResponse> getMainList(@Query("apikey") String key, @Query("s") String type);

    @GET("?apikey&i")
    Call<Details> getDetails(@Query("apikey") String key, @Query("i") String i);

}
