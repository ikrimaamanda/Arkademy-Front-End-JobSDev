package com.example.jobsdev.login

import retrofit2.http.*

interface AuthApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("accountEmail") email : String,
                            @Field("accountPassword") password : String) : LoginResponse

    @GET("engineer/{acId}")
    suspend fun getEngineerByAcId(@Path("acId") acId : Int) : DetailEngineerByAcIdResponse

//    @GET("company/{acId}")
//    suspend fun getCompanyByAcId(@Path("acId") acId : Int) :
}