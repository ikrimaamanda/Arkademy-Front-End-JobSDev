package com.example.jobsdev.maincontent.projectcompany

import com.example.jobsdev.retfrofit.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectsCompanyApiService {

    @GET("project/getProjectByCnId/{cnId}")
    suspend fun getProjectByCnId(@Path("cnId") cnId : String?) : ProjectResponse

    @Multipart
    @POST("project")
    suspend fun addNewProject(@Part("projectName") projectName : RequestBody,
                              @Part("projectDesc") projectDesc : RequestBody,
                              @Part("projectDeadline") projectDeadline : RequestBody,
                              @Part image : MultipartBody.Part,
                              @Part("cnId") companyId : RequestBody
    ) : AddProjectResponse

    @Multipart
    @PUT("project/{projectId}")
    suspend fun updateProjectById(@Path("projectId") projectId : Int?,
                                  @Part("pj_project_name") projectName : RequestBody,
                                  @Part("pj_description") projectDesc : RequestBody,
                                  @Part("pj_deadline") projectDeadline : RequestBody,
                                  @Part image : MultipartBody.Part
    ) : GeneralResponse

    @DELETE("project/{projectId}")
    suspend fun deleteProjectById(@Path("projectId") projectId : Int?) : GeneralResponse
}