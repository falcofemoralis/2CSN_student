package com.BSLCommunity.CSN_student.APIs;

import com.BSLCommunity.CSN_student.Models.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {
    String API_URL = "http://192.168.1.3/api/users/";

    @GET("login")
    Call<User> login(@Query("nickname") String nickname, @Query("password") String password);

    @FormUrlEncoded
    @POST(".")
    Call<User> registration(@Field("nickname") String nickname, @Field("password") String password, @Field("group") String groupName);

    @PUT(".")
    Call<Void> updateUserData(@Header("token") String token, @Body JsonObject data);
}
