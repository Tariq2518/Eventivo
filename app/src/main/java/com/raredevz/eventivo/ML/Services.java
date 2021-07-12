package com.raredevz.eventivo.ML;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Services
{
    //**************************************************************
    @Headers("Accept: application/json")
    @POST("execute?api-version=2.0&details=true")
    Call<ResponseBody>
    getNotification(@Body JsonObject jsonBody, @Header("Authorization") String token);
    //**************************************************************

}
