package com.example.jobsdev.retfrofit

import com.example.jobsdev.login.LoginResponse
import retrofit2.http.*

interface JobSDevApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("accountEmail") email : String,
                             @Field("accountPassword") password : String) : LoginResponse

    @GET("engineer/{acId}")
    suspend fun getEngineerByAcId(@Path("acId") enId : String?) : DetailEngineerByAcIdResponse


    @GET("company/{acId}")
    suspend fun getCompanyByAcId(@Path("acId") cnId : String?) : DetailCompanyByAcIdResponse










    @GET("skill/getSkillByEnId/{enId}")
    suspend fun getSkillByEnId(@Path("enId") enId : Int?) : GetSkillByEnIdResponse
}