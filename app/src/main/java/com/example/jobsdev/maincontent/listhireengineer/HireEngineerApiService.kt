package com.example.jobsdev.maincontent.listhireengineer

import retrofit2.http.GET

interface HireEngineerApiService {

    @GET("hire/getHireByEnId/30")
    suspend fun getHireByEngineerId() : ListHireEngineerResponse
}