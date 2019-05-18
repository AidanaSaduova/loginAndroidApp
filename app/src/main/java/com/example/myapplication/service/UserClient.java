package com.example.myapplication.service;

import io.swagger.client.model.UserLoginApiForm;
import io.swagger.client.model.UserViewModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//creating a new client service to call a microservice api
public interface UserClient {

    @POST("users/login.json")
    Call<UserViewModel> login(@Body UserLoginApiForm user);
}
