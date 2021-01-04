package com.example.jobsdev.maincontent.projectcompany

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectsCompanyApiService {

    @GET("project/getProjectByCnId/{cnId}")
    suspend fun getProjectByCnId(@Path("cnId") cnId : String?) : ProjectResponse

    @Multipart
    @POST("project")
    suspend fun addNewProject(@Part image : MultipartBody.Part,
                              @Part("projectName") projectName : RequestBody,
                              @Part("projectDesc") projectDesc : RequestBody,
                              @Part("projectDeadline") projectDeadline : RequestBody,
                              @Part("companyId") companyId : RequestBody
    ) : AddProjectResponse
}