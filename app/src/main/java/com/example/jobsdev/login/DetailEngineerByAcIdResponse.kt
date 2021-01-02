package com.example.jobsdev.login

import com.google.gson.annotations.SerializedName

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