package com.example.jobsdev.maincontent.listengineer

import retrofit2.http.GET

interface EngineerApiService {

    @GET("engineer")
    suspend fun getAllEngineer() : ListEngineerResponse
}