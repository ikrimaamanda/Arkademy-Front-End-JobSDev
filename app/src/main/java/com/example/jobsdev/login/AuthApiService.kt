package com.example.jobsdev.login

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("accountEmail") email : String,
                            @Field("accountPassword") password : String) : LoginResponse
}