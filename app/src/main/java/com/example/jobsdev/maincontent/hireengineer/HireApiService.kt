package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectResponse
import retrofit2.http.*

interface HireApiService{

    @FormUrlEncoded
    @POST("hire")
    suspend fun addHire(@Field("enId") engineerId : Int,
                        @Field("projectId") projectId : Int,
                        @Field("hirePrice") hirePrice : String,
                        @Field("hireMessage") hireMessage : String
    ) : HireResponse

    @GET("hire/project/{pjId}")
    suspend fun getListHireByProjectId(@Path("pjId") pjId : String?) : HireByProjectResponse
}