package com.example.jobsdev.register

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("ac_id") val accountId : Int?,
                    @SerializedName("ac_name") val accountName : String?,
                    @SerializedName("ac_email") val accountEmail : String?,
                    @SerializedName("ac_phone_number") val phoneNumber : String?,
                    @SerializedName("ac_level") val accountLevel : Int?,
                    @SerializedName("ac_created_at") val accountCreateAt : String?
                    )
}