package com.example.jobsdev.retfrofit

import com.google.gson.annotations.SerializedName

data class GeneralResponse(val success : Boolean, val message : String)

data class DetailEngineerByAcIdResponse(val success : Boolean, val message : String, val data : Engineer) {
    data class Engineer(@SerializedName("en_id") val engineerId : String?,
                        @SerializedName("ac_id") val accountId : Int?,
                        @SerializedName("ac_name") val accountName : String?,
                        @SerializedName("ac_email") val accountEmail : String?,
                        @SerializedName("ac_phone_number") val accountPhoneNumber : String?,
                        @SerializedName("en_job_title") val enJobTitle : String?,
                        @SerializedName("en_job_type") val enJobType : String?,
                        @SerializedName("en_location") val enLocation : String?,
                        @SerializedName("en_description") val enDescription : String?,
                        @SerializedName("en_profile_pict") val enProfilePict : String?
    )
}

data class DetailCompanyByAcIdResponse(val success : Boolean, val message : String, val data : Company) {
    data class Company(@SerializedName("cn_id") val companyId : String?,
                       @SerializedName("ac_id") val accountId : Int?,
                       @SerializedName("ac_name") val accountName : String?,
                       @SerializedName("ac_email") val accountEmail : String?,
                       @SerializedName("ac_phone_number") val accountPhoneNumber : String?,
                       @SerializedName("cn_company") val companyName : String?,
                       @SerializedName("cn_position") val position : String?,
                       @SerializedName("cn_fields") val fields : String?,
                       @SerializedName("cn_city") val companyCity : String?,
                       @SerializedName("cn_description") val cnDescription : String?,
                       @SerializedName("cn_instagram") val instagram : String?,
                       @SerializedName("cn_linkedin") val linkedin : String?,
                       @SerializedName("cn_profile_pict") val cnProfilePict : String?
    )
}

data class GetSkillByEnIdResponse(val success : Boolean, val message : String, val data : ArrayList<Skill?>) {
    data class Skill(@SerializedName("sk_id") val skId : Int?,
                     @SerializedName("en_id") val enId : Int?,
                     @SerializedName("sk_skill_name") val skillName : String?
    )
}

data class GetExperienceByEnIdResponse(val success : Boolean, val message : String, val data : ArrayList<Experience?>) {
    data class Experience(@SerializedName("ex_id") val exId : Int?,
                          @SerializedName("en_id") val enId : Int?,
                          @SerializedName("ex_position") val exPosition : String?,
                          @SerializedName("ex_company") val exCompany : String?,
                          @SerializedName("ex_start_date") val exStartDate : String?,
                          @SerializedName("ex_end_date") val exEndDate : String?,
                          @SerializedName("ex_description") val exDesc : String?
    )
}