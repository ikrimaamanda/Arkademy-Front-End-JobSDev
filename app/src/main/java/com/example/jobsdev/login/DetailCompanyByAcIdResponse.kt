package com.example.jobsdev.login

import com.google.gson.annotations.SerializedName

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