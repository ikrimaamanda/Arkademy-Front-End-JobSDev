package com.example.jobsdev.retfrofit

import com.example.jobsdev.login.LoginResponse
import okhttp3.RequestBody
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

    @DELETE("experience/{exId}")
    suspend fun deleteExperienceByExId(@Path("exId") exId : Int?) : GeneralResponse





    @Multipart
    @POST("portfolio")
    suspend fun addPortfolio(@Part("prAppName") prAppName : RequestBody,
                             @Part("prDesc") prDesc : RequestBody,
                             @Part("prLinkPub") prLinkPub : RequestBody,
                             @Part("prLinkRepo") prLinkRepo : RequestBody,
                             @Part("prWorkplace") prWorkplace : RequestBody,
                             @Part("prType") prType : RequestBody,
                             @Part("enId") enId : RequestBody
    ) : GeneralResponse

    @GET("portfolio/getPortfolioByEnId/{enId}")
    suspend fun getListPortfolioByEnId(@Path("enId") enId : Int?) : GetPortfolioByEnIdResponse

    @Multipart
    @PUT("portfolio/{portfolioId}")
    suspend fun updatePortfolio(@Path("portfolioId") portfolioId : Int,
                            @Part("pr_app_name") prAppName : RequestBody,
                             @Part("pr_description") prDesc : RequestBody,
                             @Part("pr_link_pub") prLinkPub : RequestBody,
                             @Part("pr_link_repo") prLinkRepo : RequestBody,
                             @Part("pr_workplace") prWorkplace : RequestBody,
                             @Part("pr_type") prType : RequestBody
    ) : GeneralResponse

    @DELETE("portfolio/{portfolioId}")
    suspend fun deletePortfolioByPrId(@Path("portfolioId") portfolioId: Int?) : GeneralResponse
}