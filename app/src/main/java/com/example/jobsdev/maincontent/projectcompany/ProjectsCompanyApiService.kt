package com.example.jobsdev.maincontent.projectcompany

import retrofit2.http.GET

interface ProjectsCompanyApiService {

    @GET("project/getProjectByCnId/39")
    suspend fun getProjectByCnId() : ProjectResponse
}