package com.example.jobsdev.maincontent.listhireengineer

import retrofit2.http.GET

interface HireEngineerApiService {

    @GET("hire/getHireByEnId/27")
    suspend fun getHireByEngineerId() : ListHireEngineerResponse
}