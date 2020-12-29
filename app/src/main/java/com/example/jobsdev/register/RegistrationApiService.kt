package com.example.jobsdev.register

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegistrationApiService {

    @FormUrlEncoded
    @POST("account/registration")
    suspend fun registrationEngineerReq(@Field("accountName") name : String,
                                @Field("accountEmail") email : String,
                                @Field("accountPhoneNumber") phoneNumber : String,
                                @Field("accountPassword") password : String,
                                @Field("accountLevel") level : Int
    ) : RegistrationResponse

    @FormUrlEncoded
    @POST("account/registration")
    suspend fun registrationCompanyReq(@Field("accountName") name : String,
                                       @Field("accountEmail") email : String,
                                       @Field("accountPhoneNumber") phoneNumber : String,
                                       @Field("accountPassword") password : String,
                                       @Field("accountLevel") level : Int,
                                       @Field("companyName") comPanyName : String,
                                       @Field("companyPosition") position : String
    ) : RegistrationResponse
}