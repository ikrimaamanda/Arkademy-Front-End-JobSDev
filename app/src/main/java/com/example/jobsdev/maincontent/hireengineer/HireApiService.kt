package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface HireApiService{

    @FormUrlEncoded
    @POST("hire")
    suspend fun addHire(@Field("enId") engineerId : Int,
                        @Field("projectId") projectId : Int,
                        @Field("hirePrice") hirePrice : String,
                        @Field("hireMessage") hireMessage : String
    ) : HireResponse

    @GET("hire/project/18")
    suspend fun getListHireByProjectId() : HireByProjectResponse
}