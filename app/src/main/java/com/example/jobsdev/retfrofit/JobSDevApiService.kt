package com.example.jobsdev.retfrofit

import com.example.jobsdev.login.LoginResponse
import retrofit2.http.*

interface JobSDevApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("accountEmail") email : String,
                             @Field("accountPassword") password : String) : LoginResponse

    @GET("engineer/{acId}")
    suspend fun getEngineerByAcId(@Path("acId") enId : String?) : DetailEngineerByAcIdResponse


    @GET("company/{acId}")
    suspend fun getCompanyByAcId(@Path("acId") cnId : String?) : DetailCompanyByAcIdResponse





    @FormUrlEncoded
    @POST("skill")
    suspend fun addSkill(@Field("skillName") skillName : String?,
                         @Field("enId") enId : Int?
    ) : GeneralResponse

    @GET("skill/getSkillByEnId/{enId}")
    suspend fun getSkillByEnId(@Path("enId") enId : Int?) : GetSkillByEnIdResponse

    @FormUrlEncoded
    @PUT("skill/{skId}")
    suspend fun updateSkillName(@Path("skId") skId : Int?,
                                @Field("skillName") skillName : String?
    ) : GeneralResponse

    @DELETE("skill/{skId}")
    suspend fun deleteSkillBySkId(@Path("skId") skId : Int?) : GeneralResponse




    @FormUrlEncoded
    @POST("experience")
    suspend fun addExperience(@Field("exPosition") exPosition : String?,
                              @Field("exCompany") exCompany : String?,
                              @Field("exStartDate") exStartDate : String?,
                              @Field("exEndDate") exEndDate : String?,
                              @Field("exDesc") exDesc : String?,
                              @Field("enId") enId : Int?
    ) : GeneralResponse

    @GET("experience/getExperienceByEnId/{enId}")
    suspend fun getListExperienceByEnId(@Path("enId") enId : Int?) : GetExperienceByEnIdResponse

    @FormUrlEncoded
    @PUT("experience/{exId}")
    suspend fun updateExperienceByExId(@Path("exId") exId : Int?,
                                        @Field("ex_position") exPosition : String?,
                                       @Field("ex_company") exCompany : String?,
                                       @Field("ex_start_date") exStartDate : String?,
                                       @Field("ex_end_date") exEndDate : String?,
                                       @Field("ex_description") exDesc : String?
    ) : GeneralResponse




    @GET("portfolio/getPortfolioByEnId/{enId}")
    suspend fun getListPortfolioByEnId(@Path("enId") enId : Int?) : GetPortfolioByEnIdResponse
}