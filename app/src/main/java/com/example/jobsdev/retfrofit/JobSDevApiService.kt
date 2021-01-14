package com.example.jobsdev.retfrofit

import com.example.jobsdev.login.LoginResponse
import com.example.jobsdev.maincontent.listengineer.ListEngineerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface JobSDevApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("accountEmail") email : String,
                             @Field("accountPassword") password : String) : LoginResponse

    @FormUrlEncoded
    @PUT("account/{acId}")
    suspend fun updateAccountByAcId(@Path("acId") acId : Int?,
                                    @Field("accountName") accountName : String?,
                                    @Field("accountPhoneNumber") accountPhoneNumber : String?,
                                    @Field("accountPassword") accountPassword : String?
    ) : GeneralResponse

    @GET("engineer/{acId}")
    suspend fun getEngineerByAcId(@Path("acId") acId : String?) : DetailEngineerByAcIdResponse

    @GET("engineer/{acId}")
    suspend fun getDetailEngineerByAcId(@Path("acId") acId : String?) : DetailEngineerByAcIdResponse


    @Multipart
    @PUT("engineer/{enId}")
    suspend fun updateEngineerById(@Path("enId") enId : Int?,
                                   @Part("en_job_title") jobTitle : RequestBody,
                                   @Part("en_job_type") jobType : RequestBody,
                                   @Part("en_location") location : RequestBody,
                                   @Part("en_description") enDesc : RequestBody
    ) : GeneralResponse

    @Multipart
    @PUT("engineer/{enId}")
    suspend fun updateProfilePictEngineer(@Path("enId") enId : Int?,
                                   @Part image : MultipartBody.Part
    ) : GeneralResponse

    @GET("company/{acId}")
    suspend fun getCompanyByAcId(@Path("acId") cnId : String?) : DetailCompanyByAcIdResponse

    @Multipart
    @PUT("company/{cnId}")
    suspend fun updateCompany(@Path("cnId") cnId : Int?,
                              @Part("cn_position") position : RequestBody,
                              @Part("cn_fields") fields : RequestBody,
                              @Part("cn_city") location : RequestBody,
                              @Part("cn_description") description : RequestBody,
                              @Part("cn_instagram") instagram : RequestBody,
                              @Part("cn_linkedin") linkedin : RequestBody,
                              @Part("cn_company") companyName : RequestBody
    ) : GeneralResponse

    @Multipart
    @PUT("company/{cnId}")
    suspend fun updateProfilePictCompany(@Path("cnId") cnId : Int?,
                              @Part image : MultipartBody.Part
    ) : GeneralResponse





    @GET("engineer")
    suspend fun searchEngineer(@Query("search") search: String? = null,
                               @Query("limit") limit: Int? = null,
                               @Query("page") page: Int? = null
    ): ListEngineerResponse

    @GET("engineer/filter")
    suspend fun filterEngineer(@Query("limit") limit: Int? = null,
                               @Query("page") page: Int? = null,
                               @Query("filter") filter: Int? = null
    ): ListEngineerResponse




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
                             @Part image : MultipartBody.Part,
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

    @Multipart
    @PUT("portfolio/{portfolioId}")
    suspend fun updatePortfolioImage(@Path("portfolioId") portfolioId : Int,
                                @Part image : MultipartBody.Part
    ) : GeneralResponse

    @DELETE("portfolio/{portfolioId}")
    suspend fun deletePortfolioByPrId(@Path("portfolioId") portfolioId: Int?) : GeneralResponse
}