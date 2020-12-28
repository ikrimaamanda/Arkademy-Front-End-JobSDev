package com.example.jobsdev.maincontent.listengineer

import com.example.jobsdev.maincontent.hireengineer.ItemSkillHireEngineerDataClass
import com.google.gson.annotations.SerializedName

data class ListEngineerResponse(val success : String, val message : String, val data : List<Engineer>) {
    data class Engineer(@SerializedName("en_id") val engineerId : String,
                       @SerializedName("ac_id") val accountId : String,
                       @SerializedName("ac_name") val accountName : String,
                       @SerializedName("ac_email") val accountEmail : String,
                       @SerializedName("ac_phone_number") val accountPhoneNumber : String,
                       @SerializedName("en_job_title") val engineerJobTitle : String,
                       @SerializedName("en_job_type") val engineerJobType : String,
                       @SerializedName("en_location") val engineerLocation : String,
                        @SerializedName("en_description") val engineerDescription : String,
                        @SerializedName("en_profile_pict") val engineerProfilePict : String,
                        @SerializedName("en_skill") val skillEngineer : List<ItemSkillHireEngineerDataClass>
    )
}