package com.example.jobsdev.maincontent.listhireengineer

import retrofit2.http.GET
import retrofit2.http.Path

interface HireEngineerApiService {

    @GET("hire/getHireByEnId/{enId}")
    suspend fun getHireByEngineerId(@Path("enId") enId : String?) : ListHireEngineerResponse
}