package com.example.jobsdev.maincontent.projectcompany

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProjectsCompanyApiService {

    @GET("project/getProjectByCnId/39")
    suspend fun getProjectByCnId() : ProjectResponse

    @Multipart
    @POST("project")
    suspend fun addNewProject(@Part("projectName") projectName : RequestBody,
                              @Part("projectDesc") projectDesc : RequestBody,
                              @Part("projectDeadline") projectDeadline : RequestBody,
                              @Part projectImage : MultipartBody.Part,
                              @Part("companyId") companyId : RequestBody
    ) : ProjectResponse
}