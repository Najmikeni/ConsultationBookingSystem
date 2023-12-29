package com.example.groupproject557.remote;


import com.example.groupproject557.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @FormUrlEncoded
    @POST("api/users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @POST("api/users/login")
    Call<User> loginEmail(@Field("email") String username, @Field("password") String password);

    @GET("api/users?role=admin")
    Call<List<User>> getAllLect(@Header("api-key") String api_key);

    @GET("api/users/{id}")
    Call<User> getUserbyID(@Header("api-key") String api_key, @Path("id") int id);

    @POST("api/users")
    Call<User> registerUser(@Header("api-key") String apiKey, @Body User users);


}
